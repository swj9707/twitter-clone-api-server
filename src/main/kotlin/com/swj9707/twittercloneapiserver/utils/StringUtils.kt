package com.swj9707.twittercloneapiserver.utils

import org.springframework.stereotype.Service
import java.util.*

@Service
class StringUtils {
    companion object Utils {
        fun extractFilenameFromPath (path : String) : String {
            return path.substring(path.lastIndexOf("/") + 1)
        }
        fun createImageFileName(fileName: String?): String {
            return UUID.randomUUID().toString() + "_" + fileName
        }
    }
}