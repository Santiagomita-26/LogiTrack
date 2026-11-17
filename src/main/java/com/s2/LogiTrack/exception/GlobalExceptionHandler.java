package com.s2.LogiTrack.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import com.s2.LogiTrack.exception.DataBaseRuleException;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestControllerAdvice
@Tag(
        name = "Manejo global de errores",
        description = "Documentación de las respuestas de error generadas por el backend"
)
public class GlobalExceptionHandler {

    // Recurso no encontrado: 404
    @ExceptionHandler(EntityNotFoundException.class)
    @Operation(summary = "Recurso no encontrado", description = "Se genera cuando se intenta consultar o utilizar un recurso que no existe en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado. Código: RESOURCE_NOT_FOUND")
    })
    public ResponseEntity<ErrorResponse> handlerNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage(),
                        "RESOURCE_NOT_FOUND"
                ));
    }

    // Errores de validación de los DTO: 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Operation(
            summary = "Error de validación de datos",
            description = "Se genera cuando los datos enviados en un DTO no cumplen las restricciones de validación. Retorna los campos que contienen errores."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Validación fallida. Código: VALIDATION_FAILED")
    })
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, Object> response = new HashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errorCode", "VALIDATION_FAILED");

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Errores internos del servidor: 500
    @ExceptionHandler(Exception.class)
    @Operation(
            summary = "Error interno del servidor",
            description = "Se genera cuando ocurre una excepción no contemplada por los manejadores específicos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Error interno del servidor. Código: INTERNAL_SERVER_ERROR")
    })
    public ResponseEntity<ErrorResponse> handleGenericErrors(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Error interno del servidor",
                        "INTERNAL_SERVER_ERROR"
                ));
    }

    // Errores de base de datos controlados: 400
    @ExceptionHandler(DataBaseRuleException.class)
    @Operation(
            summary = "Error de regla de base de datos",
            description = "Se genera cuando una operación incumple una regla controlada de la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Regla de base de datos incumplida. Código: DATABASE_RULE_VIOLATION")
    })
    public ResponseEntity<ErrorResponse> handleDatabaseRuleException(DataBaseRuleException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        "DATABASE_RULE_VIOLATION"
                ));
    }

    // Errores de reglas de negocio: 400
    @ExceptionHandler(BusinessRuleException.class)
    @Operation(
            summary = "Error de regla de negocio",
            description = "Se genera cuando una operación incumple una regla de negocio definida en el sistema, como una cantidad inválida o stock insuficiente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Regla de negocio incumplida. Código: BUSINESS_RULE_VIOLATION")
    })
    public ResponseEntity<ErrorResponse> handleBusinessRuleException(BusinessRuleException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        "BUSINESS_RULE_VIOLATION"
                ));
    }

    // Rutas inexistentes: 404
    @ExceptionHandler(NoHandlerFoundException.class)
    @Operation(
            summary = "Ruta no encontrada",
            description = "Se genera cuando se solicita una ruta que no está definida en la aplicación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Ruta no encontrada. Código: ERR_NO_HANDLER_FOUND")
    })
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        "Ruta no encontrada o incompleta",
                        "ERR_NO_HANDLER_FOUND"
                ));
    }

    // JSON incorrecto o incompleto: 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @Operation( summary = "Error al interpretar la solicitud",
            description = "Se genera cuando el cuerpo JSON está mal formado, incompleto o contiene valores que no pueden convertirse al tipo esperado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Cuerpo de solicitud inválido. Código: BAD_REQUEST")
    })
    public ResponseEntity<ErrorResponse> handleParsingErrors(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "El cuerpo de la solicitud no es válido",
                        "BAD_REQUEST"
                ));
    }
}
