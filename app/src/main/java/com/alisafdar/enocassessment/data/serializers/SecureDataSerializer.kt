package com.alisafdar.enocassessment.data.serializers

import androidx.datastore.CorruptionException
import androidx.datastore.Serializer
import com.alisafdar.enocassessment.data.security.Crypto
import com.alisafdar.enocassessment.datastore.data.PreferenceData
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class SecureDataSerializer(private val crypto: Crypto) : Serializer<PreferenceData> {

    override fun readFrom(input: InputStream): PreferenceData {
        return if (input.available() != 0) {
            try {

                PreferenceData.ADAPTER.decode(crypto.decrypt(input))
            } catch (exception: IOException) {
                throw CorruptionException("Cannot read proto", exception)
            }
        } else {
            PreferenceData("", "")
        }
    }

    override fun writeTo(t: PreferenceData, output: OutputStream) {
        crypto.encrypt(PreferenceData.ADAPTER.encode(t), output)
    }
}
