package routes

import RoleBasedAuthorization
import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.User
import org.litote.kmongo.*


fun Route.customerRoute (db:MongoDatabase){

    val usersCollection = db.getCollection<User>("users")

    route("/api/admin/customers"){

        install(RoleBasedAuthorization) { roles = listOf("admin") }

        get("/{customerId}"){
            val id = call.parameters["customerId"]
            val customer = usersCollection.findOne("{_id:ObjectId('$id')}")
            if(customer != null){
                return@get call.respond(customer)
            }
            call.respond(HttpStatusCode.NotFound);
        }

        get("/search"){

            var filter = "roles:[\"customer\"]"
            val email = call.request.queryParameters["email"]
            if (email != null) {
                filter = "email:/${call.parameters["email"]}/i," + filter
            }
            val id = call.request.queryParameters["id"]
            if (id != null) {
                filter = "_id:ObjectId('$id')," + filter
            }
            filter = "{$filter}"
            val result = usersCollection.find(filter).toList();
            return@get call.respond(result)
        }

    }
}