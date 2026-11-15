package ru.fefu.dvfu.hero.data

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

const val SERVER_ERROR_INDEX = "server-error"

val slowNetwork = Interceptor { chain ->
    repeat(20) {
        if (chain.call().isCanceled()) throw IOException("Canceled")
        Thread.sleep(100)
    }
    chain.proceed(chain.request())
}

val fakeServerError = Interceptor { chain ->
    val request = chain.request()
    if (request.url.pathSegments.last() == SERVER_ERROR_INDEX) {
        Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(503)
            .message("Service Unavailable")
            .body("".toResponseBody("text/plain".toMediaType()))
            .build()
    } else {
        chain.proceed(request)
    }
}
