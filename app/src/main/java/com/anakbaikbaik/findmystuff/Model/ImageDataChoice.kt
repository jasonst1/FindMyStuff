package com.anakbaikbaik.findmystuff.Model

import android.net.Uri

sealed class ImageData {
    data class UriData(val uri: Uri) : ImageData()
    data class StringData(val string: String) : ImageData()
}