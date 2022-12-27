package domain_layer

enum class Platform(val platformString: String) {
    IOS("ios"),
    ANDROID("android")
}

fun String.getPlatform(): Platform {
    return Platform::platformString.find(this)
        ?: throw IllegalArgumentException("Bad platform string: $this")
}