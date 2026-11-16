package com.s2.LogiTrack.controller;

import com.s2.LogiTrack.dto.request.CiudadDtoRequest;
import com.s2.LogiTrack.dto.response.CiudadDtoResponse;
import com.s2.LogiTrack.service.interfaces.CiudadService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ciudades")
public class CiudadController {

    private final CiudadService ciudadService;

    // http://localhost:8080/api/ciudades
    @PostMapping
    public ResponseEntity<CiudadDtoResponse> guardar(@RequestBody CiudadDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ciudadService.guardar(dto));
    }

    @GetMapping
    public ResponseEntity<List<CiudadDtoResponse>> listar() {
        return ResponseEntity.ok(ciudadService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CiudadDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(ciudadService.buscarId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CiudadDtoResponse> actualizar(
            @RequestBody CiudadDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(ciudadService.actualizar(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ciudadService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}