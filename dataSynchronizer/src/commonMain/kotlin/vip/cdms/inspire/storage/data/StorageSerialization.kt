package vip.cdms.inspire.storage.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(
    InternalSerializationApi::class,
    ExperimentalUnsignedTypes::class,
    ExperimentalSerializationApi::class,
    ExperimentalUuidApi::class
)
object StorageSerialization {

    private val format = Json {
        explicitNulls = false
        ignoreUnknownKeys = true
        isLenient = true
        allowTrailingComma = true
        allowComments = true
        allowSpecialFloatingPointValues = true
    }

    private fun <T : Any> of(clazz: KClass<T>) = when (clazz) {
        Char::class -> Char.serializer()
        CharArray::class -> CharArraySerializer()

        Byte::class -> Byte.serializer()
        UByte::class -> UByte.serializer()
        ByteArray::class -> ByteArraySerializer()
        UByteArray::class -> UByteArraySerializer()

        Short::class -> Short.serializer()
        UShort::class -> UShort.serializer()
        Short::class -> ShortArraySerializer()
        UShort::class -> UShortArraySerializer()

        Int::class -> Int.serializer()
        UInt::class -> UInt.serializer()
        IntArray::class -> IntArraySerializer()
        UIntArray::class -> UIntArraySerializer()

        Long::class -> Long.serializer()
        ULong::class -> ULong.serializer()
        LongArray::class -> LongArraySerializer()
        ULongArray::class -> ULongArraySerializer()

        Float::class -> Float.serializer()
        FloatArray::class -> FloatArraySerializer()

        Double::class -> Double.serializer()
        DoubleArray::class -> DoubleArraySerializer()

        Boolean::class -> Boolean.serializer()
        BooleanArray::class -> BooleanArraySerializer()

        Unit::class -> Unit.serializer()
        String::class -> String.serializer()

        Duration::class -> Duration.serializer()
        Uuid::class -> Uuid.serializer()
        Nothing::class -> NothingSerializer()

        List::class -> throw IllegalArgumentException("Cannot determine List element type at runtime.")
        Map::class -> throw IllegalArgumentException("Cannot determine Map key/value types at runtime.")
        else -> clazz.serializer()
    }

    fun <T : Any> encode(clazz: KClass<T>, data: T): String {
        @Suppress("UNCHECKED_CAST")
        return format.encodeToString(of(clazz) as KSerializer<T>, data)
    }

    fun <T : Any> decode(clazz: KClass<T>, data: String): T {
        @Suppress("UNCHECKED_CAST")
        return format.decodeFromString(of(clazz) as KSerializer<T>, data)
    }

}
