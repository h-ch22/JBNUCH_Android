package kr.ac.jbnu.ch.frameworks.helper

import android.util.Base64
import java.security.KeyStore
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AES256Util {
    companion object{
        private const val SECRET_KEY = "JBNU_CH_54_SUN_23858291929394283"
        private val IV = "JBNUCH_PR_SUN_54".toByteArray()

        fun encrypt(string: String?): String {
            val keySpec = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(IV))

            val encrypted = cipher.doFinal(string?.toByteArray())

            return Base64.encodeToString(encrypted, 0)
        }

        fun decrypt(encoded : String?) : String{
            try {
                val textBytes = Base64.decode(encoded, Base64.DEFAULT)
                val ivSpec: AlgorithmParameterSpec = IvParameterSpec(IV)
                val key = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)

                return String(cipher.doFinal(textBytes))
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return encoded ?: ""
        }
    }
}