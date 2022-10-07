import io.ktor.server.auth.jwt.*

fun JWTPrincipal.getEmail() : String{

    return payload?.getClaim("email").toString().replace("\"","")
}

fun JWTPrincipal.getId() : String{
    return   payload?.getClaim("id").toString().replace("\"","")
}