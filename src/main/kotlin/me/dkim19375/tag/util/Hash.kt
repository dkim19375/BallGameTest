package me.dkim19375.tag.util

import com.toxicbakery.bcrypt.Bcrypt

data class Hash(val array: ByteArray) {
    fun hashEquals(other: String, ignoreIfEmptyHash: Boolean = true): Boolean {
        if (ignoreIfEmptyHash && array.isEmpty()) {
            return true
        }
        return Bcrypt.verify(other, array)
    }

    fun serialize(): String = array.joinToString("-")

    companion object {
        fun deserialize(string: String): Hash = Hash(
            try {
                string.split('-').map(String::toByte).toByteArray()
            } catch (_: NumberFormatException) {
                byteArrayOf()
            }
        )

        fun emptyHash(): Hash = deserialize("")
    }

    override operator fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Hash

        if (!array.contentEquals(other.array)) return false

        return true
    }

    override fun hashCode(): Int = array.contentHashCode()
}

fun String.toHash(): Hash = Hash(if (isEmpty()) byteArrayOf() else Bcrypt.hash(this, 6))