package kotlinx.serialization.meta

import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.serializer

sealed interface Meta {
    fun <T> to(deserializationStrategy: DeserializationStrategy<T>): T {
        val decoder = MetaDecoder(meta = this)
        return deserializationStrategy.deserialize(decoder)
    }

    sealed interface Primitive : Meta

    @JvmInline
    value class Boolean(val value: kotlin.Boolean) : Primitive

    @JvmInline
    value class Byte(val value: kotlin.Byte) : Primitive

    @JvmInline
    value class Short(val value: kotlin.Short) : Primitive

    @JvmInline
    value class Int(val value: kotlin.Int) : Primitive

    @JvmInline
    value class Long(val value: kotlin.Long) : Primitive

    @JvmInline
    value class Float(val value: kotlin.Float) : Primitive

    @JvmInline
    value class Double(val value: kotlin.Double) : Primitive

    @JvmInline
    value class Char(val value: kotlin.Char) : Primitive

    @JvmInline
    value class String(val value: kotlin.String) : Primitive

    @JvmInline
    value class List(val value: kotlin.collections.List<Meta>) : Meta

    @JvmInline
    value class Map(val value: kotlin.collections.Map<Meta, Meta>) : Meta

    data object Null : Meta

    companion object {
        fun <T> from(serializer: SerializationStrategy<T>, value: T): Meta {
            lateinit var result: Meta
            val encoder = MetaEncoder { meta ->
                result = meta
            }
            serializer.serialize(encoder, value)
            return result
        }

        inline fun <reified T> from(value: T): Meta {
            val serializer = serializer<T>()
            return from(serializer, value)
        }
    }
}

inline fun <reified T> Meta.to(): T {
    val serializer = serializer<T>()
    return to(serializer)
}
