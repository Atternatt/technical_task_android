sealed class BuildVariant(
    val endpoint: String,
    val port: Int
) {

    object Release: BuildVariant("https://gorest.co.in/public-api/", 37964)
    object Debug: BuildVariant("https://gorest.co.in/public-api/", 37964)
    object Integration: BuildVariant("http://localhost", 37964)
}


