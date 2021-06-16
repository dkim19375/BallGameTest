package me.dkim19375.tag.util

import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.shape.Circle
import me.dkim19375.tag.main
import me.dkim19375.tag.util.KeyType.DOWN
import me.dkim19375.tag.util.KeyType.LEFT
import me.dkim19375.tag.util.KeyType.RIGHT
import me.dkim19375.tag.util.KeyType.UP
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

val centerX: Double
    get() = windowX / 2.0

val centerY: Double
    get() = windowY / 2.0

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

fun Int.bound(min: Int, max: Int) = toDouble().bound(min.toDouble(), max.toDouble())

fun Double.bound(min: Double, max: Double) = if (min > max) {
    throw IllegalArgumentException("The minimum value - $min cannot be greater than the maximum value - $max!")
} else {
    max(min, min(this, max))
}

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

fun Node.getLocation(): Point2D = Point2D(layoutX + layoutBounds.minX, layoutY + layoutBounds.minY)

fun Circle.isTouching(other: Circle): Boolean {
    val firstLoc = getLocation()
    val otherLoc = other.getLocation()
    val distance = firstLoc.distance(otherLoc)
    return distance < radius + other.radius
}

fun Point2D.getAngle(other: Point2D): Double = atan2(other.y - y, other.x - x).toDegrees()

fun Point2D.getDirectionPoint(distance: Double, angle: Double): Point2D =
    getDirectionPoint(distance.toFloat(), angle.toFloat())

fun Point2D.getDirectionPoint(distance: Float, angle: Float): Point2D {
    val newX = x + distance * cos(angle.toRadians())
    val newY = y + distance * sin(angle.toRadians())
    return Point2D(newX, newY)
}

fun Node.getPoint(direction: KeyType, amount: Double): Point2D = getPoint(listOf(direction), amount)

fun Node.getPoint(directions: Collection<KeyType>, amount: Double): Point2D {
    val loc = getLocation()
    val angle = directions.getDirection() ?: return loc
    return loc.getDirectionPoint(amount, angle)
}

fun Node.teleport(loc: Point2D) = teleport(loc.x, loc.y)

fun Node.teleport(x: Int, y: Int) = teleport(x.toDouble(), y.toDouble())

@Synchronized
fun Node.teleport(x: Double, y: Double) = relocate(x, y)

fun Collection<KeyType>.getDirection(): Double? {
    val list = toSet().toList()
    if (isEmpty()) {
        return null
    }
    val first = list[0]
    if (size == 1) {
        return first.angle
    }
    val second = list[1]
    if (size >= 4) {
        return null
    }
    if (size == 2) {
        when (first) {
            UP -> {
                return when (second) {
                    UP -> UP.angle
                    LEFT -> UP.angle - 45.0
                    DOWN -> null
                    RIGHT -> UP.angle + 45.0
                }
            }
            LEFT -> {
                return when (second) {
                    UP -> LEFT.angle + 45.0
                    LEFT -> LEFT.angle
                    DOWN -> LEFT.angle - 45.0
                    RIGHT -> null
                }
            }
            DOWN -> {
                return when (second) {
                    UP -> null
                    LEFT -> DOWN.angle + 45.0
                    DOWN -> DOWN.angle
                    RIGHT -> DOWN.angle - 45.0
                }
            }
            RIGHT -> {
                return when (second) {
                    UP -> RIGHT.angle - 45.0
                    LEFT -> null
                    DOWN -> 45.0
                    RIGHT -> RIGHT.angle
                }
            }
        }
    }
    val third = list[2]
    if (size != 3) {
        return null
    }
    return when {
        listOf(first, second).getDirection() == null -> third.angle
        listOf(first, third).getDirection() == null -> second.angle
        listOf(second, third).getDirection() == null -> first.angle
        else -> null
    }
}