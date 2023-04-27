package com.stevdza.san.api

import com.stevdza.san.MongoDB
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Api(routeOverride = "readcomponents")
suspend fun readComponents(ctx: ApiContext) {
    try {
        println(ctx.logger.info("HELLO: ${System.getenv("MONGODB_URI")}"))
        val result = ctx.data.getValue<MongoDB>().read()
        ctx.res.setBodyText(Json.encodeToString(result))
    } catch (e: Exception) {
        ctx.res.setBodyText(e.toString())
    }
}