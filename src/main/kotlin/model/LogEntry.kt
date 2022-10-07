package model

import ObjectIdAsStringSerializer
import com.fasterxml.jackson.annotation.JsonIgnore
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

@Serializable
data class LogEntry(
    val start: Long,
    val end: Long,
    val instructor:Boolean,
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<LearnerLicence> = newId()
){
    @get:JsonIgnore
    val duration : Duration
        get(){
            return Duration.ofMillis(end - start)
        }
    @get:JsonIgnore
    val bonus : Duration
        get(){
            return Duration.ofMillis(if(instructor) duration.toMillis() * 2 else 0)
        }
    @get:JsonIgnore
    val total : Duration
        get() {
            return Duration.ofMillis(duration.toMillis() + bonus.toMillis())
        }

    @get:JsonIgnore
    val night : Duration
        get() {
            val zoneId = ZoneId.of("Australia/Sydney")
            var index = Instant.ofEpochMilli(start).atZone(zoneId).toLocalDateTime()
            val finish = Instant.ofEpochMilli(end).atZone(zoneId).toLocalDateTime()
            var night : Long = 0
            while(index.isBefore(finish)){
                val midnight = index.plusDays(1).toLocalDate().atStartOfDay(zoneId).toLocalDateTime()

                val dayEnd = if(midnight.isBefore(finish)) midnight else finish

                if(index.hour < 6){
                    val morningEnd = if(dayEnd.hour > 0 && dayEnd.hour < 6) {
                        index.withHour(dayEnd.hour).withMinute(dayEnd.minute).withSecond(dayEnd.second)
                    }
                    else{
                        index.withHour(6).withMinute(0).withSecond(0)
                    }
                    val es =  morningEnd.toEpochSecond(zoneId.rules.getOffset(morningEnd));
                    val ss = index.toEpochSecond(zoneId.rules.getOffset(index))
                    night += es - ss
                }

                if(dayEnd.hour > 18 || dayEnd.hour == 0){

                    val eveningStart = if(index.hour > 18) index else index.withHour(18).withMinute(0).withSecond(0)
                    val es =  dayEnd.toEpochSecond(zoneId.rules.getOffset(dayEnd));
                    val ss = eveningStart.toEpochSecond(zoneId.rules.getOffset(eveningStart))
                    night += es - ss
                }
                index = midnight
            }
            return Duration.ofSeconds(night)
        }

}