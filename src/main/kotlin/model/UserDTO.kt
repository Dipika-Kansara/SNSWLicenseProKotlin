package model

import ObjectIdAsStringSerializer
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id


@Serializable
class UserDTO{
    val email:String
    val roles : List<String>
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<User>
    val firstName : String
    val lastName : String
    val dob : Long
    val age: Int
    val address : String
    val mobile : Long



    constructor(user: User){
        email = user.email
        roles = user.roles
        _id = user._id
        firstName = user.firstName
        lastName = user.lastName
        dob = user.dob
        age = user.age
        address = user.address
        mobile = user.mobile
    }
}