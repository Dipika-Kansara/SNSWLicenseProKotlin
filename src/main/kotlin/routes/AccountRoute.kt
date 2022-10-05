package routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.User
import org.litote.kmongo.*
import org.mindrot.jbcrypt.*
import java.util.*

fun getJWTToken(user:User): String {

    val expiry = Date(System.currentTimeMillis() + 86400000)

    return JWT.create()
        .withAudience("http://localhost:8080")
        .withIssuer("http://localhost:8080")
        .withClaim("email",user?.email)
        .withClaim("id",user._id.toString())
        .withClaim("roles",user?.roles)
        .withClaim("dob",user?.dob)
        .withExpiresAt(expiry)
        .sign(Algorithm.HMAC256("secret"))
}

fun Route.accountRoute (db:MongoDatabase){

    val usersCollection = db.getCollection<User>("users")

    route("/api/account"){

        post("/register"){
            val data = call.receive<User>()
            val hashed = BCrypt.hashpw(data.password,BCrypt.gensalt())
            val user = User(email = data.email, password = hashed,roles = listOf("customer"),dob = data.dob, address = data.address)
            usersCollection.insertOne(user)
            val token = getJWTToken(user)
            call.respond(HttpStatusCode.Created,token)
        }

        post("/login"){
            val data = call.receive<User>()
            val filter = "{email:/^${data.email}$/i}"
            val user = usersCollection.findOne(filter) ?: return@post call.respond(HttpStatusCode.BadRequest)
            val valid = BCrypt.checkpw(data.password,user.password)
            if(!valid){
                return@post call.respond(HttpStatusCode.BadRequest)
            }
            val token = getJWTToken(user)
            return@post call.respond(token)
        }
    }
}