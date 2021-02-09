sealed class BuildVariant(
    val endpoint: String
) {

    object Release: BuildVariant("https://gorest.co.in/public-api/")
    object Debug: BuildVariant("https://gorest.co.in/public-api/")
}


