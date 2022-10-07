package model

import ObjectIdAsStringSerializer
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

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

