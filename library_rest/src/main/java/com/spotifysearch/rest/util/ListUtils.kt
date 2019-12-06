package com.spotifysearch.rest.util

import com.spotifysearch.rest.client.RequestParam

object ListUtils {

    fun buildParametersArray(parametersList: ArrayList<RequestParam>): Array<RequestParam> {
        val fillArray = arrayOfNulls<RequestParam>(parametersList.size)
        return parametersList.toArray(fillArray)
    }
}