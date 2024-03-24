package com.darothub.musschatscreen.util

import android.view.ViewTreeObserver
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import com.darothub.musschatscreen.model.Sender
import kotlinx.coroutines.launch
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@ExperimentalFoundationApi
fun Modifier.bringViewAboveKeyboard(): Modifier = Modifier.composed {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()
    val view = LocalView.current
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            scope.launch { bringIntoViewRequester.bringIntoView() }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose { view.viewTreeObserver.removeOnGlobalLayoutListener(listener) }
    }
    return@composed this.bringIntoViewRequester(bringIntoViewRequester)
}

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
fun Modifier.hideKeyboardOnClick(onClick:() -> Unit): Modifier = Modifier.composed {
    val focusManager = LocalFocusManager.current
    return@composed this.clickable {
        onClick()
        focusManager.clearFocus()
    }
}

@OptIn(ExperimentalContracts::class)
inline fun Sender.say(block: (Sender) -> Boolean): Boolean {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block(this)
}