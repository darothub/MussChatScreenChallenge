package com.darothub.musschatscreen

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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import com.darothub.musschatscreen.model.Sender
import com.darothub.musschatscreen.presentation.ui.screens.Number
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
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
inline fun Sender.says(block: (Sender) -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block(this)
}

fun Modifier.flip(isFlipped: Boolean): Modifier = composed {
    if (isFlipped) {
        scale(-1f, 1f)
    } else {
        this
    }
}


fun isTimeDifferenceGreaterThanOneHour(instant1: Instant, instant2: Instant): Boolean {
    val duration = Duration.between(instant1, instant2).abs()
    return duration.toMinutes() > Number.SIXTY_MIN
}

fun formatInstantToDayAndTime(instant: Instant): String {
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
    val formatter = DateTimeFormatter.ofPattern("EEEE HH:mm")
    return localDateTime.format(formatter)
}