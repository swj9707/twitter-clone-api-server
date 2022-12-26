package com.swj9707.twittercloneapiserver.utils

import org.springframework.stereotype.Service

@Service
class StringUtils {
    companion object Utils {
        fun extractFilenameFromPath (path : String) : String {
            return path.substring(path.lastIndexOf("/") + 1)
        }
    }
}