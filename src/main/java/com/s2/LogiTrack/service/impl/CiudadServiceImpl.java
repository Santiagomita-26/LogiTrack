package com.s2.LogiTrack.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s2.LogiTrack.dto.request.CiudadDtoRequest;
import com.s2.LogiTrack.dto.response.CiudadDtoResponse;
import com.s2.LogiTrack.exception.DataBaseRuleException;
import com.s2.LogiTrack.mapper.CiudadMapper;
import com.s2.LogiTrack.model.Auditoria;
import com.s2.LogiTrack.model.Ciudad;
import com.s2.LogiTrack.repository.CiudadRepository;
import com.s2.LogiTrack.service.interfaces.AuditoriaService;
import com.s2.LogiTrack.service.interfaces.CiudadService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CiudadServiceImpl implements CiudadService {

    private final CiudadMapper ciudadMapper;
    private final CiudadRepository ciudadRepository;
    private final AuditoriaService auditoriaService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public CiudadDtoResponse guardar(CiudadDtoRequest dto) {

        Ciudad ciudad = ciudadMapper.dtoToEntity(dto);
        Ciudad ciudadRespuesta = ciudadRepository.save(ciudad);
        CiudadDtoResponse respuesta = ciudadMapper.entityToDto(ciudadRespuesta);
        // Captura los valores nuevos.
        JsonNode valoresNuevos = objectMapper.valueToTree(respuesta);
        // Registra la operación INSERT.
        auditoriaService.registrar(Auditoria.TipoOperacion.INSERT, "Ciudad", null, valoresNuevos);
        return respuesta;
    }

    @Override
    @Transactional
    public CiudadDtoResponse actualizar(CiudadDtoRequest dto, Long id) {

        Ciudad ciudad = ciudadRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, ciudad a actualizar no encontrada"));

        // Captura los valores antes de modificar.
        JsonNode valoresAnteriores = objectMapper.valueToTree(ciudadMapper.entityToDto(ciudad));
        ciudadMapper.updateEntityToDto(ciudad, dto);
        Ciudad ciudadActualizada = ciudadRepository.save(ciudad);
        CiudadDtoResponse respuesta = ciudadMapper.entityToDto(ciudadActualizada);
        // Captura los valores después de modificar.
        JsonNode valoresNuevos = objectMapper.valueToTree(respuesta);
        // RegIstra la operación UPDATE.
        auditoriaService.registrar(Auditoria.TipoOperacion.UPDATE, "Ciudad", valoresAnteriores, valoresNuevos
        );
        return respuesta;
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Ciudad ciudad = ciudadRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, ciudad a eliminar no existe"));
        JsonNode valoresAnteriores = objectMapper.valueToTree(ciudadMapper.entityToDto(ciudad));
        try {ciudadRepository.delete(ciudad);ciudadRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new DataBaseRuleException("No se puede eliminar la ciudad porque tiene registros relacionados en otras tablas.");
        }
        auditoriaService.registrar(Auditoria.TipoOperacion.DELETE, "Ciudad", valoresAnteriores, null);
    }

    @Override
    public List<CiudadDtoResponse> listar() {
        return ciudadRepository.findAll().stream().map(ciudadMapper::entityToDto).toList();
    }

    @Override
    public CiudadDtoResponse buscarId(Long id) {
        Ciudad ciudad = ciudadRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, no existe una ciudad con ese ID"));
        return ciudadMapper.entityToDto(ciudad);
    }

}