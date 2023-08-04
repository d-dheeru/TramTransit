package com.gotranspo.tramtransit.utils

import java.util.UUID

object CommonUtils {

    fun createNewGuid(): String {
        return UUID.randomUUID().toString()
    }
}