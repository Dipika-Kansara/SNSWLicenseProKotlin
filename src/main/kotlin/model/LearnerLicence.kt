package model

import ObjectIdAsStringSerializer
import com.fasterxml.jackson.annotation.JsonIgnore
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.time.Duration

@Serializable
data class LearnerLicence(
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<LearnerLicence> = newId(),
    val issued:Long = System.currentTimeMillis(),
    val issuedBy: String = "",
    val userId : String,
    val logEntries: MutableList<LogEntry> = mutableListOf()
){
    fun dto() : LearnerLicenceDTO= LearnerLicenceDTO(this)
}

@Serializable
data class LogEntry(
    val start: Long,
    val end: Long,
    val instructor:Boolean
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
}

@Serializable
class LearnerLicenceDTO{

    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<LearnerLicence>
    val issued:Long
    val issuedBy: String
    val userId : String
    val logEntries: MutableList<LogEntryDTO>
    val total:TimeUnitDTO

    constructor(licence: LearnerLicence){
        _id = licence._id
        issued = licence.issued
        issuedBy = licence.issuedBy
        userId = licence.userId
        var licenceTotal : Long = 0
        logEntries = licence.logEntries.map {
            val duration = TimeUnitDTO(it.duration)
            val bonus = TimeUnitDTO(it.bonus)
            val total = TimeUnitDTO(it.total)
            licenceTotal += it.total.toSeconds()
            LogEntryDTO(it.start,it.end,it.instructor,duration,bonus,total)
        }.toMutableList()
        val ltd = Duration.ofSeconds(licenceTotal)
        total = TimeUnitDTO(ltd)
    }
}
@Serializable
data class LogEntryDTO(
    val start: Long,
    val end: Long,
    val instructor:Boolean,
    val duration : TimeUnitDTO,
    val bonus : TimeUnitDTO,
    val total : TimeUnitDTO
)

@Serializable
class TimeUnitDTO {
    val hours: Long
    val minutes: Long
    val seconds: Long
    constructor(d:Duration){
        hours = d.seconds / 3600;
        minutes = (d.seconds  % 3600) / 60;
        seconds = d.seconds  % 60;
    }
}


