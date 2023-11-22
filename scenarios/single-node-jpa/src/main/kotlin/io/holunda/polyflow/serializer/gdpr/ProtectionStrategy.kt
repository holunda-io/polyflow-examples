package io.holunda.polyflow.serializer.gdpr

/**
 * Strategy to detect the protection.
 */
sealed interface ProtectionStrategy {
  fun shouldEncrypt(any: Any?): Boolean
  fun <T> encrypt(value: T): T
  fun <T> decrypt(value: T): T


  class NoneProtectionStrategy: ProtectionStrategy {
    override fun shouldEncrypt(any: Any?): Boolean = false
    override fun <T> encrypt(value: T): T  = value
    override fun <T> decrypt(value: T): T = value
  }
}
