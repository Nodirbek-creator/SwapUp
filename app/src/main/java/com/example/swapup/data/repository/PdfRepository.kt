package com.example.swapup.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class PdfRepository {

    suspend fun downloadPdfFile(context: Context, urlStr: String): File {
        val url = URL(urlStr)
        val connection = withContext(Dispatchers.IO) {
            url.openConnection()
        } as HttpURLConnection
        connection.connectTimeout = 15000
        connection.readTimeout = 15000
        withContext(Dispatchers.IO) {
            connection.connect()
        }

        if (connection.responseCode != HttpURLConnection.HTTP_OK) {
            throw Exception("HTTP error code: ${connection.responseCode}")
        }

        val file = withContext(Dispatchers.IO) {
            File.createTempFile("temp_pdf", ".pdf", context.cacheDir)
        }
        file.outputStream().use { output ->
            connection.inputStream.use { input ->
                input.copyTo(output)
            }
        }
        return file
    }
}