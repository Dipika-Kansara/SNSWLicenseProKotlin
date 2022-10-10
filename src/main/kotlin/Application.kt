import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.*
import routes.Account.accountRoute
import routes.Admin.customerRoute
import routes.Customer.customerProfile
import routes.licenceRoute

val client = KMongo.createClient()
val db = client.getDatabase("ServiceNSW")

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.init() {

    installPlugins()

    routing {

        get("/") {
            call.respondText("Service NSW")
        }

        accountRoute(db)

        authenticate {

            licenceRoute(db)
            customerRoute(db)
            customerProfile(db)

        }
    }
}

fun Application.installPlugins(){
    install(ContentNegotiation){
        json()
    }

    install(CORS){
        allowHost("*")
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Post)
    }

    install(Authentication){
        jwt{
            realm = "tms.com.au"
            verifier(JWT
                .require(Algorithm.HMAC256("secret"))
                .withAudience("http://localhost:8080")
                .withIssuer("http://localhost:8080")
                .build()
            )
            validate{
                    token -> JWTPrincipal(token.payload)
            }
            challenge{
                    defaultScheme, realm ->  call.respond(HttpStatusCode.Unauthorized,"Invalid Token")
            }
        }
    }
}