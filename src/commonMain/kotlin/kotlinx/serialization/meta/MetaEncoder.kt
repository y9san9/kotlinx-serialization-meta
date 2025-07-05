package kotlinx.serialization.meta

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind

internal class MetaEncoder(
    private val consumer: (Meta) -> Unit,
) : Encoder {
    override val serializersModule = EmptySerializersModule()

    @ExperimentalSerializationApi
    override fun encodeNull() {
        consumer(Meta.Null)
    }

    override fun encodeBoolean(value: Boolean) {
        consumer(Meta.Boolean(value))
    }

    override fun encodeByte(value: Byte) {
        consumer(Meta.Byte(value))
    }

    override fun encodeShort(value: Short) {
        consumer(Meta.Short(value))
    }

    override fun encodeChar(value: Char) {
        consumer(Meta.Char(value))
    }

    override fun encodeInt(value: Int) {
        consumer(Meta.Int(value))
    }

    override fun encodeLong(value: Long) {
        consumer(Meta.Long(value))
    }

    override fun encodeFloat(value: Float) {
        consumer(Meta.Float(value))
    }

    override fun encodeDouble(value: Double) {
        consumer(Meta.Double(value))
    }

    override fun encodeString(value: String) {
        consumer(Meta.String(value))
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        consumer(Meta.Int(index))
    }

    override fun encodeInline(descriptor: SerialDescriptor): Encoder {
        return this
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        return when (descriptor.kind as StructureKind) {
            StructureKind.LIST -> ListEncoder(consumer)
            StructureKind.OBJECT,
            StructureKind.CLASS -> ClassEncoder(consumer)
            StructureKind.MAP -> MapEncoder(consumer)
        }
    }

    class ListEncoder(private val consumer: (Meta) -> Unit) : CompositeEncoder {
        private val list = mutableListOf<Meta>()

        override val serializersModule = EmptySerializersModule()

        override fun endStructure(descriptor: SerialDescriptor) {
            consumer(Meta.List(list.toList()))
        }

        override fun encodeBooleanElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Boolean,
        ) {
            list += Meta.Boolean(value)
        }

        override fun encodeByteElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Byte,
        ) {
            list += Meta.Byte(value)
        }

        override fun encodeShortElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Short,
        ) {
            list += Meta.Short(value)
        }

        override fun encodeCharElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Char,
        ) {
            list += Meta.Char(value)
        }

        override fun encodeIntElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Int,
        ) {
            list += Meta.Int(value)
        }

        override fun encodeLongElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Long,
        ) {
            list += Meta.Long(value)
        }

        override fun encodeFloatElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Float,
        ) {
            list += Meta.Float(value)
        }

        override fun encodeDoubleElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Double,
        ) {
            list += Meta.Double(value)
        }

        override fun encodeStringElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: String,
        ) {
            list += Meta.String(value)
        }


        override fun encodeInlineElement(
            descriptor: SerialDescriptor,
            index: Int
        ): Encoder {
            val encoder = MetaEncoder { meta ->
                list += meta
            }
            return encoder
        }

        override fun <T : Any?> encodeSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            serializer: SerializationStrategy<T>,
            value: T
        ) {
            val encoder = MetaEncoder { meta ->
                list += meta
            }
            serializer.serialize(encoder, value)
        }

        @ExperimentalSerializationApi
        override fun <T : Any> encodeNullableSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            serializer: SerializationStrategy<T>,
            value: T?
        ) {
            val encoder = MetaEncoder { meta ->
                list += meta
            }
            if (value == null) {
                encoder.encodeNull()
            } else {
                serializer.serialize(encoder, value)
            }
        }
    }

    class ClassEncoder(private val consumer: (Meta) -> Unit) : CompositeEncoder {
        private val map = mutableMapOf<Meta.String, Meta>()

        override val serializersModule = EmptySerializersModule()

        override fun endStructure(descriptor: SerialDescriptor) {
            consumer(Meta.Map(map.toMap()))
        }

        override fun encodeBooleanElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Boolean,
        ) {
            val elementName = descriptor.getElementName(index)
            val key = Meta.String(elementName)
            map[key] = Meta.Boolean(value)
        }

        override fun encodeByteElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Byte,
        ) {
            val elementName = descriptor.getElementName(index)
            val key = Meta.String(elementName)
            map[key] = Meta.Byte(value)
        }

        override fun encodeShortElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Short,
        ) {
            val elementName = descriptor.getElementName(index)
            val key = Meta.String(elementName)
            map[key] = Meta.Short(value)
        }

        override fun encodeCharElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Char,
        ) {
            val elementName = descriptor.getElementName(index)
            val key = Meta.String(elementName)
            map[key] = Meta.Char(value)
        }

        override fun encodeIntElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Int,
        ) {
            val elementName = descriptor.getElementName(index)
            val key = Meta.String(elementName)
            map[key] = Meta.Int(value)
        }

        override fun encodeLongElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Long,
        ) {
            val elementName = descriptor.getElementName(index)
            val key = Meta.String(elementName)
            map[key] = Meta.Long(value)
        }

        override fun encodeFloatElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Float,
        ) {
            val elementName = descriptor.getElementName(index)
            val key = Meta.String(elementName)
            map[key] = Meta.Float(value)
        }

        override fun encodeDoubleElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Double,
        ) {
            val elementName = descriptor.getElementName(index)
            val key = Meta.String(elementName)
            map[key] = Meta.Double(value)
        }

        override fun encodeStringElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: String,
        ) {
            val elementName = descriptor.getElementName(index)
            val key = Meta.String(elementName)
            map[key] = Meta.String(value)
        }

        override fun encodeInlineElement(
            descriptor: SerialDescriptor,
            index: Int
        ): Encoder {
            val elementName = descriptor.getElementName(index)
            val key = Meta.String(elementName)
            val encoder = MetaEncoder { meta ->
                map[key] = meta
            }
            return encoder
        }

        override fun <T : Any?> encodeSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            serializer: SerializationStrategy<T>,
            value: T
        ) {
            val elementName = descriptor.getElementName(index)
            val key = Meta.String(elementName)
            val encoder = MetaEncoder { meta ->
                map[key] = meta
            }
            serializer.serialize(encoder, value)
        }

        @ExperimentalSerializationApi
        override fun <T : Any> encodeNullableSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            serializer: SerializationStrategy<T>,
            value: T?
        ) {
            val elementName = descriptor.getElementName(index)
            val key = Meta.String(elementName)
            val encoder = MetaEncoder { meta ->
                map[key] = meta
            }
            if (value == null) {
                encoder.encodeNull()
            } else {
                serializer.serialize(encoder, value)
            }
        }
    }

    class MapEncoder(private val consumer: (Meta) -> Unit) : CompositeEncoder {
        private val map = mutableMapOf<Meta, Meta>()
        private var key: Meta? = null

        override val serializersModule = EmptySerializersModule()

        override fun endStructure(descriptor: SerialDescriptor) {
            consumer(Meta.Map(map.toMap()))
        }

        override fun encodeBooleanElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Boolean,
        ) {
            val element = Meta.Boolean(value)
            val key = this.key
            if (key == null) {
                this.key = element
            } else {
                map[key] = element
                this.key = null
            }
        }

        override fun encodeByteElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Byte,
        ) {
            val element = Meta.Byte(value)
            val key = this.key
            if (key == null) {
                this.key = element
            } else {
                map[key] = element
                this.key = null
            }
        }

        override fun encodeShortElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Short,
        ) {
            val element = Meta.Short(value)
            val key = this.key
            if (key == null) {
                this.key = element
            } else {
                map[key] = element
                this.key = null
            }
        }

        override fun encodeCharElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Char,
        ) {
            val element = Meta.Char(value)
            val key = this.key
            if (key == null) {
                this.key = element
            } else {
                map[key] = element
                this.key = null
            }
        }

        override fun encodeIntElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Int,
        ) {
            val element = Meta.Int(value)
            val key = this.key
            if (key == null) {
                this.key = element
            } else {
                map[key] = element
                this.key = null
            }
        }

        override fun encodeLongElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Long,
        ) {
            val element = Meta.Long(value)
            val key = this.key
            if (key == null) {
                this.key = element
            } else {
                map[key] = element
                this.key = null
            }
        }

        override fun encodeFloatElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Float,
        ) {
            val element = Meta.Float(value)
            val key = this.key
            if (key == null) {
                this.key = element
            } else {
                map[key] = element
                this.key = null
            }
        }

        override fun encodeDoubleElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Double,
        ) {
            val element = Meta.Double(value)
            val key = this.key
            if (key == null) {
                this.key = element
            } else {
                map[key] = element
                this.key = null
            }
        }

        override fun encodeStringElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: String,
        ) {
            val element = Meta.String(value)
            val key = this.key
            if (key == null) {
                this.key = element
            } else {
                map[key] = element
                this.key = null
            }
        }

        override fun encodeInlineElement(
            descriptor: SerialDescriptor,
            index: Int
        ): Encoder {
            val encoder = MetaEncoder { meta ->
                val key = this.key
                if (key == null) {
                    this.key = meta
                } else {
                    map[key] = meta
                    this.key = null
                }
            }
            return encoder
        }

        override fun <T : Any?> encodeSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            serializer: SerializationStrategy<T>,
            value: T
        ) {
            val encoder = MetaEncoder { meta ->
                val key = this.key
                if (key == null) {
                    this.key = meta
                } else {
                    map[key] = meta
                    this.key = null
                }
            }
            serializer.serialize(encoder, value)
        }

        @ExperimentalSerializationApi
        override fun <T : Any> encodeNullableSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            serializer: SerializationStrategy<T>,
            value: T?
        ) {
            val encoder = MetaEncoder { meta ->
                val key = this.key
                if (key == null) {
                    this.key = meta
                } else {
                    map[key] = meta
                    this.key = null
                }
            }
            if (value == null) {
                encoder.encodeNull()
            } else {
                serializer.serialize(encoder, value)
            }
        }
    }
}
