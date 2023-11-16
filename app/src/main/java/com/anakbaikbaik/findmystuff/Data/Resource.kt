package com.anakbaikbaik.findmystuff.Data

import com.google.android.gms.tasks.Task
import java.lang.Exception

sealed class Resource<out R> {
    data class Success<out R>(val result: R? = null) : Resource<R>()
    data class Failure(val exception: Exception) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}