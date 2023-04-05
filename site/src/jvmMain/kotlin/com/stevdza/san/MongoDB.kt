package com.stevdza.san

import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.coroutine.toList
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.reactivestreams.getCollection

@InitApi
fun initMongoDb(ctx: InitApiContext) {
    System.setProperty(
        "org.litote.mongo.test.mapping.service",
        "org.litote.kmongo.serialization.SerializationClassMappingTypeService"
    )
    ctx.data.add(MongoDB())
}

class MongoDB {
    private val client = KMongo.createClient(System.getenv("MONGODB_URI"))
    private val database = client.getDatabase("compose_snacks")
    private val collection = database.getCollection<UiComponent>()

    suspend fun add(uiComponent: UiComponent) =
        collection.insertOne(uiComponent).awaitFirst().wasAcknowledged()

    suspend fun read() = collection.find(UiComponent::class.java).toList()
}