package vip.cdms.inspire

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform