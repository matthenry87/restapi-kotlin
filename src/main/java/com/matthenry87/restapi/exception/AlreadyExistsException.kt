package com.matthenry87.restapi.exception

class AlreadyExistsException : RuntimeException {

    constructor() {}
    constructor(message: String?) : super(message) {}

}