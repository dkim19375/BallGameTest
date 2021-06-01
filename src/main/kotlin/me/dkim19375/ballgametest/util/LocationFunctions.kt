package me.dkim19375.ballgametest.util

import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.shape.Circle
import me.dkim19375.ballgametest.main
import kotlin.math.max
import kotlin.math.min

val windowX: Double
    get() = main.stage.width

val windowY: Double
    get() = main.stage.height

fun Int.getX(full: Int = windowX.toInt()): Int = toDouble().getX(full.toDouble()).toInt()

fun Int.getY(full: Int = windowY.toInt()): Int = toDouble().getY(full.toDouble()).toInt()

fun Double.getX(full: Double = windowX): Double = (full / 2) + this

fun Double.getY(full: Double = windowY): Double = (full / 2) + this

fun Pair<Int, Int>.getCoords(x: Int = windowX.toInt(), y: Int = windowY.toInt()): Point2D =
    Point2D(first.getX(x).toDouble(), second.getY(y).toDouble())

fun Pair<Double, Double>.getCoords(x: Double = windowX, y: Double = windowY): Point2D =
    Point2D(first.getX(x), second.getY(y))

fun Int.bound(min: Int, max: Int) = max(min, min(this, max))

fun Double.bound(min: Double, max: Double) = max(min, min(this, max))

fun Point2D.setBounds(
    maxX: Double = windowX,
    maxY: Double = windowY,
    minX: Double = 0.0,
    minY: Double = 0.0
): Point2D {
    return Point2D(x.bound(minX, maxX), y.bound(minY, maxY))
}

fun Point2D.copy(
    x: Double = this.x,
    y: Double = this.y
): Point2D = Point2D(x, y)

fun Node.getLocation(): Point2D {
    val bounds = localToScene(boundsInLocal)
    val point = Point2D(bounds.centerX, bounds.centerY)
    if (this !is Circle) {
        return point
    }
    return point.subtract(radius, radius)
}

fun Point2D.format(): String = "$x, $y"

fun Node.getPoint(direction: KeyType, amount: Double): Point2D {
    val loc = getLocation()
    println("before: $loc, after: ${
        when (direction) {
            KeyType.W -> loc.add(0.0, -amount)
            KeyType.A -> loc.add(-amount, 0.0)
            KeyType.S -> loc.add(0.0, amount)
            KeyType.D -> loc.add(amount, 0.0)
        }
    }")
    return when (direction) {
        KeyType.W -> loc.add(0.0, -amount)
        KeyType.A -> loc.add(-amount, 0.0)
        KeyType.S -> loc.add(0.0, amount)
        KeyType.D -> loc.add(amount, 0.0)
    }
}

fun Node.teleport(loc: Point2D) {
    relocate(loc.x, loc.y)
}