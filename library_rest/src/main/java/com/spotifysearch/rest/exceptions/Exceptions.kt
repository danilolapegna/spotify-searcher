package com.spotifysearch.rest.exceptions

class RequestFormatError(message: String) : Exception(message)

class HttpException(val code: Int, message: String, val payload: Any?) : Exception(message)
