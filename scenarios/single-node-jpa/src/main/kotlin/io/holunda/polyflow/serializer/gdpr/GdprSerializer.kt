package io.holunda.polyflow.serializer.gdpr

import org.axonframework.serialization.SerializedObject
import org.axonframework.serialization.Serializer


class GdprSerializer(
  private val serializer: Serializer,
  private val protectionDetection: ProtectionStrategy
) : Serializer by serializer {

  override fun <T : Any?> serialize(`object`: Any?, expectedRepresentation: Class<T>): SerializedObject<T> {
    val result = if(protectionDetection.shouldEncrypt(`object`)) {
      protectionDetection.encrypt(`object`)
    } else {
      `object`
    }
    return serializer.serialize(result, expectedRepresentation)
  }

  override fun <S : Any?, T : Any?> deserialize(serializedObject: SerializedObject<S>): T {
    val deserialized = serializer.deserialize<S, T>(serializedObject)
    return protectionDetection.decrypt(deserialized)
  }
}
