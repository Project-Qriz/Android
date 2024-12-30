package com.qriz.app.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

internal object CryptographyHelper {
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val ALIAS = "token"
    private const val ENGINE = KeyProperties.KEY_ALGORITHM_AES
    private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
    private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
    private const val CIPHER_TRANSFORMATION = "$ENGINE/$BLOCK_MODE/$PADDING"
    private const val DELIMITER = "|"
    private val keystore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

    private val key: SecretKey = keystore.getKey(ALIAS, null) as? SecretKey ?: createKey()

    private fun createKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE,
        )

        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build()
        )

        return keyGenerator.generateKey()
    }

    fun encrypt(value: String): String {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encrypted = cipher.doFinal(value.encodeToByteArray())
        return "${cipher.iv.encode()}$DELIMITER${encrypted.encode()}"
    }

    fun decrypt(value: String): String {
        val (iv, token) = value.split(DELIMITER)
        val decodedValue = token.decode()
        val decodedIv = iv.decode()

        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(decodedIv))
        val decoded = cipher.doFinal(decodedValue)
        return decoded.decodeToString()
    }

    private fun ByteArray.encode(): String =
        Base64.encodeToString(this, Base64.NO_WRAP)

    private fun String.decode(): ByteArray =
        Base64.decode(this, Base64.NO_WRAP)
}
