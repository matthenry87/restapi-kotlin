package com.matthenry87.restapi.exception

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.*
import java.util.stream.Collectors

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<List<Error>> {

        val fieldErrors = e.bindingResult.fieldErrors

        val errors = fieldErrors.stream()
                .map { x: FieldError -> Error(x.field, x.defaultMessage) }
                .collect(Collectors.toList())

        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun alreadyExistsException(e: AlreadyExistsException): ResponseEntity<Error> {

        val exceptionMessage = e.message

        val message = exceptionMessage ?: "already exists"

        val error = Error(null, message)

        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun notFoundException(e: NotFoundException?): ResponseEntity<Error> {

        val error = Error(null, "not found")

        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun httpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<Error> {

        val cause = e.cause

        if (cause is InvalidFormatException) {

            val invalidFormatException = cause

            val targetType = invalidFormatException.targetType

            if (Enum::class.java.isAssignableFrom(targetType)) {

                val enumConstants = targetType.enumConstants as Array<Enum<*>>

                val values = Arrays.stream(enumConstants)
                        .map { obj: Enum<*> -> obj.name }
                        .collect(Collectors.joining(", "))

                val message = "Invalid value. Valid values: $values"

                val field = invalidFormatException.path[0].fieldName

                val error = Error(field, message)

                return ResponseEntity(error, HttpStatus.BAD_REQUEST)
            }
        }

        val error = Error(null, e.message!!)

        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun httpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ResponseEntity<Error> {

        val error = Error(null, e.message!!)

        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun exception(e: Exception?): ResponseEntity<Error> {

        return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    data class Error(@field:JsonInclude(JsonInclude.Include.NON_NULL) val field: String?, val message: String)
}