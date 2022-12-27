package network_layer.models.nodes

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable(with = LineHeightUnitSerializer::class)
enum class LineHeightUnit(val key: String) {
    PIXELS("PIXELS"),
    FONT_SIZE("FONT_SIZE_%"),
    INTRINSIC("INTRINSIC_%"),
    NONE("");
    companion object {
        fun findByKey(key: String, default: LineHeightUnit = LineHeightUnit.NONE): LineHeightUnit {
            return LineHeightUnit.values().find { it.key == key } ?: default
        }
    }
}



@Serializer(forClass = LineHeightUnit::class)
object LineHeightUnitSerializer : KSerializer<LineHeightUnit> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LineHeightUnit", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LineHeightUnit) {
        encoder.encodeString(value.key)
    }

    override fun deserialize(decoder: Decoder): LineHeightUnit {
        return try {
            val key = decoder.decodeString()
            LineHeightUnit.findByKey(key)
        } catch (e: IllegalArgumentException) {
            LineHeightUnit.NONE
        }
    }
}
