package me.dkim19375.tag.util

import kotlin.reflect.KProperty0

fun KProperty0<*>.isInit(): Boolean = try {
    get()
    true
} catch (_: UninitializedPropertyAccessException) {
    false
}