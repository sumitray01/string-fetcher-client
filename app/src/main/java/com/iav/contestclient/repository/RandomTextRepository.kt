package com.iav.contestclient.repository

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import com.iav.contestclient.data.model.RandomStringEntity
import com.iav.contestclient.data.RandomStringDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

class RandomTextRepository @Inject constructor(
    private val dao: RandomStringDao,
    @ApplicationContext private val context: Context
) {

    val allRandomStrings = dao.getAllRandomStrings()

    suspend fun fetchAndStoreRandomString(desiredLength: Int) {
        withContext(Dispatchers.IO) {
            val uri = Uri.parse("content://com.iav.contestdataprovider/text")
            val queryArgs = Bundle().apply {
                putInt(ContentResolver.QUERY_ARG_LIMIT, desiredLength)
            }
            // Query the content provider
            val cursor = context.contentResolver.query(uri, null, queryArgs, null)
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        val dataColumnIndex = cursor.getColumnIndex("data")
                        if (dataColumnIndex != -1) {
                            val dataJson = cursor.getString(dataColumnIndex)
                            // Parse the JSON returned by the content provider
                            val jsonObject = JSONObject(dataJson)
                            val randomTextObject = jsonObject.getJSONObject("randomText")
                            val value = randomTextObject.getString("value")
                            val length = randomTextObject.getInt("length")
                            val created = randomTextObject.getString("created")
                            val entity = RandomStringEntity(
                                value = value,
                                length = length,
                                created = created
                            )
                            dao.insertRandomString(entity)
                        } else {
                            throw Exception("Data column not found in content provider response.")
                        }
                    } else {
                        throw Exception("No data returned from content provider.")
                    }
                } finally {
                    cursor.close()
                }
            } else {
                throw Exception("Failed to query the content provider.")
            }
        }
    }

    suspend fun deleteRandomString(entity: RandomStringEntity) {
        withContext(Dispatchers.IO) {
            dao.deleteRandomString(entity)
        }
    }

    suspend fun deleteAllRandomStrings() {
        withContext(Dispatchers.IO) {
            dao.deleteAllRandomStrings()
        }
    }
}
