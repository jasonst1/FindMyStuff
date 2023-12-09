package com.anakbaikbaik.findmystuff.Util

import com.anakbaikbaik.findmystuff.Model.ItemMessage

fun searchItems(messages: List<ItemMessage>, query: String): List<ItemMessage> {
    val sortedMessages = messages.sortedByDescending { it.tanggal }

    return if (query.isNotBlank()) {
        sortedMessages.filter {
            it.nama.contains(query, ignoreCase = true) ||
                    it.lokasi.contains(query, ignoreCase = true) ||
                    it.deskripsi.contains(query, ignoreCase = true)
        }
    } else {
        messages
    }
}