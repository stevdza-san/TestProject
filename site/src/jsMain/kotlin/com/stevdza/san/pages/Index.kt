package com.stevdza.san.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.stevdza.san.UiComponent
import com.varabyte.kobweb.browser.api
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.forms.Button
import kotlinx.browser.window
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.dom.Text

@Page
@Composable
fun HomePage() {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            scope.launch {
                println(window.api.tryGet("addcomponent?name=Stefan")?.decodeToString())
            }
        }) {
            Text("Add")
        }
        Button(onClick = {
            scope.launch {
                val result = window.api.tryGet("readcomponents")?.decodeToString()
                val parsed = Json.decodeFromString<List<UiComponent>>(result.toString())
                println(parsed)
            }
        }) {
            Text("Read")
        }
    }
}