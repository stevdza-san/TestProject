package com.stevdza.san.api

import com.stevdza.san.MongoDB
import com.stevdza.san.UiComponent
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText

@Api(routeOverride = "addcomponent")
suspend fun addComponent(ctx: ApiContext) {
    val name = ctx.req.params["name"] ?: "unknown"

//    try {
        ctx.res.setBodyText(ctx.data.getValue<MongoDB>().add(UiComponent(name = name)).toString())
//    } catch (e: Exception) {
//        ctx.res.setBodyText(e.toString())
//    }
}