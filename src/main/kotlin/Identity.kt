import io.ktor.server.auth.jwt.*

fun JWTPrincipal.getEmail() : String{

    return payload?.getClaim("email").toString().replace("\"","")
}