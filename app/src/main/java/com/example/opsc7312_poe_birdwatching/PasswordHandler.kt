package com.example.opsc7312_poe_birdwatching

import java.security.MessageDigest

class PasswordHandler {

    companion object {


        //============================================================================
        // Function tio hash user password
        fun hashPassword(password: String): String {
            val digest = MessageDigest.getInstance("SHA-256")
            val encodedHash = digest.digest(password.toByteArray())
            return bytesToHex(encodedHash)
        }

        //============================================================================
        // Function to convert bytes to hexadecimal
        private fun bytesToHex(bytes: ByteArray): String {
            val hexChars = "0123456789ABCDEF"
            val hexString = StringBuilder(bytes.size * 2)
            for (byte in bytes) {
                val value = byte.toInt() and 0xFF
                hexString.append(hexChars[value shr 4])
                hexString.append(hexChars[value and 0x0F])
            }
            return hexString.toString()
        }
    }

}