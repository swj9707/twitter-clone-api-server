package com.swj9707.twittercloneapiserver.utils

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream

@Service
class FileUtils {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Throws(Exception::class)
    fun uploadFile(uploadPath: String, fileName: String, fileData: ByteArray): Boolean {
        val fileUploadFullUrl = "$uploadPath/$fileName"
        val fos = FileOutputStream(fileUploadFullUrl)
        fos.write(fileData)
        fos.close()
        return true
    }

    @Throws(Exception::class)
    fun deleteFile(filePath: String, fileName : String) {
        val deleteFile = File("$filePath/$fileName")
        if (deleteFile.exists()) {
            deleteFile.delete()
            logger.info("파일을 삭제하였습니다.")
        } else {
            logger.error("파일이 존재하지 않습니다.")
        }
    }
}