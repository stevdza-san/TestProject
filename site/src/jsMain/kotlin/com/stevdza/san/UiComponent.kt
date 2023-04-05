package com.stevdza.san

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
actual data class UiComponent(
    @SerialName(value = "_id")
    actual val id: Long,
    actual val name: String
)