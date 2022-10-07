package model

import ObjectIdAsStringSerializer
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import java.time.Duration


@Serializable
class LearnerLicenceDTO{

    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<LearnerLicence>
    val issued:Long
    val issuedBy: String
    val userId : String
    val logEntries: MutableList<LogEntryDTO>
    val total:TimeUnitDTO
    val remaining: TimeUnitDTO
    val night: TimeUnitDTO
    constructor(licence: LearnerLicence){
        _id = licence._id
        issued = licence.issued
        issuedBy = licence.issuedBy
        userId = licence.userId
        var licenceTotal : Long = 0
        var nightTotal : Long = 0


        logEntries = licence.logEntries.map {
//            val duration = TimeUnitDTO(it.duration)
//            val bonus = TimeUnitDTO(it.bonus)
//            val total = TimeUnitDTO(it.total)
//            val night = TimeUnitDTO(it.night)

            licenceTotal += it.total.toSeconds()
            nightTotal += it.night.toSeconds()

            LogEntryDTO(it)

        }.toMutableList()
        val ltd = Duration.ofSeconds(licenceTotal)
        val nightltd = Duration.ofSeconds(nightTotal)
        val remainingLtd = Duration.ofSeconds((120 *60 *60 ).toLong() - licenceTotal)

        total = TimeUnitDTO(ltd)
        remaining = TimeUnitDTO(remainingLtd)
        night = TimeUnitDTO(nightltd)


    }
}




