package kotlinx.serialization.meta

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind

internal class MetaDecoder(
    private val meta: Meta,
) : Decoder {
    override val serializersModule = EmptySerializersModule()

    @ExperimentalSerializationApi
    override fun decodeNotNullMark(): Boolean {
        return meta !is Meta.Null
    }

    @ExperimentalSerializationApi
    override fun decodeNull(): Nothing? {
        meta as Meta.Null
        return null
    }

    override fun decodeBoolean(): Boolean {
        meta as Meta.Boolean
        return meta.value
    }

    override fun decodeByte(): Byte {
        meta as Meta.Byte
        return meta.value
    }

    override fun decodeShort(): Short {
        meta as Meta.Short
        return meta.value
    }

    override fun decodeChar(): Char {
        meta as Meta.Char
        return meta.value
    }

    override fun decodeInt(): Int {
        meta as Meta.Int
        return meta.value
    }

    override fun decodeLong(): Long {
        meta as Meta.Long
        return meta.value
    }

    override fun decodeFloat(): Float {
        meta as Meta.Float
        return meta.value
    }

    override fun decodeDouble(): Double {
        meta as Meta.Double
        return meta.value
    }

    override fun decodeString(): String {
        meta as Meta.String
        return meta.value
    }

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        meta as Meta.Int
        return meta.value
    }

    override fun decodeInline(descriptor: SerialDescriptor): Decoder {
        return this
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        return when (descriptor.kind as StructureKind) {
            is StructureKind.LIST -> ListDecoder(meta as Meta.List)
            is StructureKind.MAP -> MapDecoder(meta as Meta.Map)
            is StructureKind.OBJECT,
            is StructureKind.CLASS -> ClassDecoder(meta as Meta.Map)
        }
    }

    class ListDecoder(val meta: Meta.List) : CompositeDecoder {
        private var index = 0

        override val serializersModule = EmptySerializersModule()

        override fun endStructure(descriptor: SerialDescriptor) {}

        override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
            if (index == meta.value.size) return CompositeDecoder.DECODE_DONE
            return index++
        }

        override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = meta.value.size

        override fun decodeBooleanElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Boolean {
            val element = meta.value[index] as Meta.Boolean
            return element.value
        }

        override fun decodeByteElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Byte {
            val element = meta.value[index] as Meta.Byte
            return element.value
        }

        override fun decodeCharElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Char {
            val element = meta.value[index] as Meta.Char
            return element.value
        }

        override fun decodeShortElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Short {
            val element = meta.value[index] as Meta.Short
            return element.value
        }

        override fun decodeIntElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Int {
            val element = meta.value[index] as Meta.Int
            return element.value
        }

        override fun decodeLongElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Long {
            val element = meta.value[index] as Meta.Long
            return element.value
        }

        override fun decodeFloatElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Float {
            val element = meta.value[index] as Meta.Float
            return element.value
        }

        override fun decodeDoubleElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Double {
            val element = meta.value[index] as Meta.Double
            return element.value
        }

        override fun decodeStringElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): String {
            val element = meta.value[index] as Meta.String
            return element.value
        }

        override fun decodeInlineElement(
            descriptor: SerialDescriptor,
            index: Int
        ): Decoder {
            val decoder = MetaDecoder(meta.value[index])
            return decoder
        }

        override fun <T : Any?> decodeSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            deserializer: DeserializationStrategy<T>,
            previousValue: T?,
        ): T {
            val decoder = MetaDecoder(meta.value[index])
            return deserializer.deserialize(decoder)
        }

        @ExperimentalSerializationApi
        override fun <T : Any> decodeNullableSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            deserializer: DeserializationStrategy<T?>,
            previousValue: T?,
        ): T? {
            val decoder = MetaDecoder(meta.value[index])
            return if (decoder.decodeNotNullMark()) {
                deserializer.deserialize(decoder)
            } else {
                decoder.decodeNull()
            }
        }
    }

    class MapDecoder(val meta: Meta.Map) : CompositeDecoder {
        private var pairs = meta.value.toList()
        private var index = 0

        override val serializersModule = EmptySerializersModule()

        override fun endStructure(descriptor: SerialDescriptor) {}

        override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
            if (index == meta.value.size * 2) return CompositeDecoder.DECODE_DONE
            return index++
        }

        override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = meta.value.size * 2

        override fun decodeBooleanElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Boolean {
            val pair = pairs[index / 2]
            val element = if (index % 2 == 0) pair.first else pair.second
            element as Meta.Boolean
            return element.value
        }

        override fun decodeByteElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Byte {
            val pair = pairs[index / 2]
            val element = if (index % 2 == 0) pair.first else pair.second
            element as Meta.Byte
            return element.value
        }

        override fun decodeCharElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Char {
            val pair = pairs[index / 2]
            val element = if (index % 2 == 0) pair.first else pair.second
            element as Meta.Char
            return element.value
        }

        override fun decodeShortElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Short {
            val pair = pairs[index / 2]
            val element = if (index % 2 == 0) pair.first else pair.second
            element as Meta.Short
            return element.value
        }

        override fun decodeIntElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Int {
            val pair = pairs[index / 2]
            val element = if (index % 2 == 0) pair.first else pair.second
            element as Meta.Int
            return element.value
        }

        override fun decodeLongElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Long {
            val pair = pairs[index / 2]
            val element = if (index % 2 == 0) pair.first else pair.second
            element as Meta.Long
            return element.value
        }

        override fun decodeFloatElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Float {
            val pair = pairs[index / 2]
            val element = if (index % 2 == 0) pair.first else pair.second
            element as Meta.Float
            return element.value
        }

        override fun decodeDoubleElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Double {
            val pair = pairs[index / 2]
            val element = if (index % 2 == 0) pair.first else pair.second
            element as Meta.Double
            return element.value
        }

        override fun decodeStringElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): String {
            val pair = pairs[index / 2]
            val element = if (index % 2 == 0) pair.first else pair.second
            element as Meta.String
            return element.value
        }

        override fun decodeInlineElement(
            descriptor: SerialDescriptor,
            index: Int
        ): Decoder {
            val pair = pairs[index / 2]
            val element = if (index % 2 == 0) pair.first else pair.second
            val decoder = MetaDecoder(element)
            return decoder
        }

        override fun <T : Any?> decodeSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            deserializer: DeserializationStrategy<T>,
            previousValue: T?,
        ): T {
            val pair = pairs[index / 2]
            val element = if (index % 2 == 0) pair.first else pair.second
            val decoder = MetaDecoder(element)
            return deserializer.deserialize(decoder)
        }

        @ExperimentalSerializationApi
        override fun <T : Any> decodeNullableSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            deserializer: DeserializationStrategy<T?>,
            previousValue: T?,
        ): T? {
            val pair = pairs[index / 2]
            val element = if (index % 2 == 0) pair.first else pair.second
            val decoder = MetaDecoder(element)
            return if (decoder.decodeNotNullMark()) {
                deserializer.deserialize(decoder)
            } else {
                decoder.decodeNull()
            }
        }
    }

    class ClassDecoder(val meta: Meta.Map) : CompositeDecoder {
        private var pairs = meta.value.toList()
        private var index = 0

        override val serializersModule = EmptySerializersModule()

        override fun endStructure(descriptor: SerialDescriptor) {}

        override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
            while (true) {
                if (index == pairs.size) return CompositeDecoder.DECODE_DONE
                val pair = pairs[index]
                val key = pair.first as Meta.String
                index++
                repeat(descriptor.elementsCount) { i ->
                    if (descriptor.getElementName(i) == key.value) {
                        return i
                    }
                }
            }
        }

        override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = meta.value.size

        override fun decodeBooleanElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Boolean {
            val element = pairs[this.index - 1].second
            element as Meta.Boolean
            return element.value
        }

        override fun decodeByteElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Byte {
            val element = pairs[this.index - 1].second
            element as Meta.Byte
            return element.value
        }

        override fun decodeCharElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Char {
            val element = pairs[this.index - 1].second
            element as Meta.Char
            return element.value
        }

        override fun decodeShortElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Short {
            val element = pairs[this.index - 1].second
            element as Meta.Short
            return element.value
        }

        override fun decodeIntElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Int {
            val element = pairs[this.index - 1].second
            element as Meta.Int
            return element.value
        }

        override fun decodeLongElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Long {
            val element = pairs[this.index - 1].second
            element as Meta.Long
            return element.value
        }

        override fun decodeFloatElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Float {
            val element = pairs[this.index - 1].second
            element as Meta.Float
            return element.value
        }

        override fun decodeDoubleElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): Double {
            val element = pairs[this.index - 1].second
            element as Meta.Double
            return element.value
        }

        override fun decodeStringElement(
            descriptor: SerialDescriptor,
            index: Int,
        ): String {
            val element = pairs[this.index - 1].second
            element as Meta.String
            return element.value
        }

        override fun decodeInlineElement(
            descriptor: SerialDescriptor,
            index: Int
        ): Decoder {
            val element = pairs[this.index - 1].second
            val decoder = MetaDecoder(element)
            return decoder
        }

        override fun <T : Any?> decodeSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            deserializer: DeserializationStrategy<T>,
            previousValue: T?,
        ): T {
            val element = pairs[this.index - 1].second
            val decoder = MetaDecoder(element)
            return deserializer.deserialize(decoder)
        }

        @ExperimentalSerializationApi
        override fun <T : Any> decodeNullableSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            deserializer: DeserializationStrategy<T?>,
            previousValue: T?,
        ): T? {
            val element = pairs[this.index - 1].second
            val decoder = MetaDecoder(element)
            return if (decoder.decodeNotNullMark()) {
                deserializer.deserialize(decoder)
            } else {
                decoder.decodeNull()
            }
        }
    }
}
