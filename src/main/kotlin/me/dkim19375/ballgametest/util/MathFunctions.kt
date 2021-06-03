package me.dkim19375.ballgametest.util

fun Float.toRadians(): Float = this * Math.PI.toFloat() / 180.0F

fun Float.toDegrees(): Float = this * 180.0F / Math.PI.toFloat()

fun Double.toRadians(): Double = toFloat().toRadians().toDouble()

fun Double.toDegrees(): Double = toFloat().toDegrees().toDouble()