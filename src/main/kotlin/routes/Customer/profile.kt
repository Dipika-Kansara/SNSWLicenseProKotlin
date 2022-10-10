package routes.Customer




import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.User


import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

fun Route.customerProfile (db: MongoDatabase){

    val usersCollection = db.getCollection<User>("users")

    route("/api/customer"){
        get("/me"){
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("id").toString().replace("\"","")
            val customer = usersCollection.findOne("{_id:ObjectId('$userId')}")
            if(customer != null){
                return@get call.respond(customer.dto())
            }
            call.respond(HttpStatusCode.NotFound);
        }
    }
}