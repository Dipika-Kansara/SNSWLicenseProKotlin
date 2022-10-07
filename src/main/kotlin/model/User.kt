package model

import ObjectIdAsStringSerializer
import com.fasterxml.jackson.annotation.JsonIgnore
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId

@Serializable
data class User(
    val email : String,
    val password: String,
    val roles: List<String> = listOf(),
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<User> = newId(),
    val firstName : String   ="",
    val lastName : String = "",
    val dob : Long = 0,
    val address : String = "",
    val mobile : Long = 0,
){
    @get:JsonIgnore
    val age : Int
        get() {
            val zoneId = ZoneId.of("Australia/Sydney")
            var birthDate = Instant.ofEpochMilli(dob).atZone(zoneId).toLocalDate()
            val now = LocalDate.now(zoneId)
            return if (birthDate != null && now != null) Period.between(birthDate, now).years else 0;
        }

    fun dto() = UserDTO(this)
}