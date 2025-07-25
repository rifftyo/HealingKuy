package com.rifftyo.healingkuy.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns

object ImageUtils {

    fun getFileNameFromUri(contentResolver: ContentResolver, uri: Uri): String {
        var name = "profile.jpg"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst()) {
                name = it.getString(nameIndex)
            }
        }
        return name
    }

    fun getBytesFromUri(contentResolver: ContentResolver, uri: Uri): ByteArray? {
        return contentResolver.openInputStream(uri)?.readBytes()
    }
}