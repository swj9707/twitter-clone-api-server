package com.swj9707.twittercloneapiserver.global.utils

import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream

@Service
class FileUtils {
    companion object Utils {
        @Throws(Exception::class)
        fun uploadFile(uploadPath: String, fileName: String, fileData: ByteArray): Boolean {
            val fileUploadFullUrl = "$uploadPath/$fileName"
            val fos = FileOutputStream(fileUploadFullUrl)
            fos.write(fileData)
            fos.close()
            return true
        }

        @Throws(Exception::class)
        fun deleteFile(filePath: String, fileName: String): Boolean {
            val deleteFile = File("$filePath/$fileName")
            return if (deleteFile.exists()) {
                deleteFile.delete()
            } else {
                false
            }
        }
    }

}