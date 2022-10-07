package model

import ObjectIdAsStringSerializer
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
class LogEntryDTO {
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<LearnerLicence>
    val start: Long
    val end: Long
    val instructor: Boolean
    val duration: TimeUnitDTO
    val bonus: TimeUnitDTO
    val total: TimeUnitDTO
    val night: TimeUnitDTO

    constructor(entry: LogEntry){
        _id = entry._id
        start = entry.start
        end = entry.end
        instructor = entry.instructor
        duration = TimeUnitDTO(entry.duration)
        bonus = TimeUnitDTO(entry.bonus)
        total = TimeUnitDTO(entry.total)
        night = TimeUnitDTO(entry.night)
    }
}