package com.stevdza.san

import kotlinx.serialization.Serializable

@Serializable
expect class UiComponent {
    val id: Long
    val name: String
}