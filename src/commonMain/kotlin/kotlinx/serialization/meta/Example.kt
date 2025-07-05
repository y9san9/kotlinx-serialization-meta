package kotlinx.serialization.meta

import kotlinx.serialization.Serializable

@Serializable
class DataUser(
    val id: Int,
    val username: String,
    val test: Map<String, Test>,
)

@Serializable
data class DomainUser(
    val id: Int,
    val username: String,
)

enum class Test {
    A, B
}

inline fun <reified T, reified R> T.auto(): R {
    return Meta.from(this).to()
}

fun main() {
    val dataUser = DataUser(0, "user", mapOf("to" to Test.A, "to" to Test.B))
    val domainUser: DomainUser = dataUser.auto()
    println(domainUser)
}
