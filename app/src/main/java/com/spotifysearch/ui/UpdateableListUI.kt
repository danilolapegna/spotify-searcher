package com.spotifysearch.ui

interface UpdateableListUI<T> {

    fun updateElements(elements: List<T>)

    fun clearItems()

    fun onRequestStart()

    fun onRequestStop()
}
