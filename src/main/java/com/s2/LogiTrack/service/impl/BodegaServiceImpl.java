package com.s2.LogiTrack.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s2.LogiTrack.dto.request.BodegaDtoRequest;
import com.s2.LogiTrack.dto.response.BodegaDtoResponse;
import com.s2.LogiTrack.exception.DataBaseRuleException;
import com.s2.LogiTrack.mapper.BodegaMapper;
import com.s2.LogiTrack.model.Auditoria;
import com.s2.LogiTrack.model.Bodega;
import com.s2.LogiTrack.model.Ciudad;
import com.s2.LogiTrack.repository.BodegaRepository;
import com.s2.LogiTrack.repository.CiudadRepository;
import com.s2.LogiTrack.service.interfaces.AuditoriaService;
import com.s2.LogiTrack.service.interfaces.BodegaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BodegaServiceImpl implements BodegaService {

    private final BodegaMapper bodegaMapper;
    private final BodegaRepository bodegaRepository;
    private final CiudadRepository ciudadRepository;
    private final AuditoriaService auditoriaService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public BodegaDtoResponse guardar(BodegaDtoRequest dto) {
        Ciudad ciudad = ciudadRepository.findById(dto.ciudadId()).orElseThrow(() -> new EntityNotFoundException("Error, la ciudad no existe"));
        Bodega bodega = bodegaMapper.dtoToEntity(dto, ciudad);
        Bodega bodegaRespuesta = bodegaRepository.save(bodega);
        BodegaDtoResponse respuesta = bodegaMapper.entityToDto(bodegaRespuesta);
        JsonNode valoresNuevos = objectMapper.valueToTree(respuesta);
        auditoriaService.registrar(Auditoria.TipoOperacion.INSERT, "Bodega", null, valoresNuevos);
        return respuesta;
    }

    @Override
    @Transactional
    public BodegaDtoResponse actualizar(BodegaDtoRequest dto, Long id) {
        Bodega bodega = bodegaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, bodega a actualizar no encontrada"));
        JsonNode valoresAnteriores = objectMapper.valueToTree(bodegaMapper.entityToDto(bodega));
        Ciudad ciudad = ciudadRepository.findById(dto.ciudadId()).orElseThrow(() -> new EntityNotFoundException("Error, la ciudad no existe"));
        bodegaMapper.updateEntityToDto(bodega, dto, ciudad);
        Bodega bodegaActualizada = bodegaRepository.save(bodega);
        BodegaDtoResponse respuesta = bodegaMapper.entityToDto(bodegaActualizada);
        JsonNode valoresNuevos = objectMapper.valueToTree(respuesta);
        auditoriaService.registrar(Auditoria.TipoOperacion.UPDATE, "Bodega", valoresAnteriores, valoresNuevos);
        return respuesta;
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Bodega bodega = bodegaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, bodega a eliminar no existe"));
        JsonNode valoresAnteriores = objectMapper.valueToTree(bodegaMapper.entityToDto(bodega));
        try {bodegaRepository.delete(bodega);bodegaRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new DataBaseRuleException("No se puede eliminar la bodega porque tiene registros relacionados en otras tablas.");
        }
        auditoriaService.registrar(Auditoria.TipoOperacion.DELETE, "Bodega", valoresAnteriores, null);
    }

    @Override
    public List<BodegaDtoResponse> listar() {
        return bodegaRepository.findAll().stream().map(bodegaMapper::entityToDto).toList();
    }

    @Override
    public BodegaDtoResponse buscarId(Long id) {
        Bodega bodega = bodegaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, no existe una bodega con ese ID"));
        return bodegaMapper.entityToDto(bodega);
    }

    @Override
    public List<BodegaDtoResponse> buscarCiudadId(Long id) {

        if (!ciudadRepository.existsById(id)) {
            throw new EntityNotFoundException("Error, no existe una ciudad con ese ID");
        }
        return bodegaRepository.findByCiudadId(id).stream().map(bodegaMapper::entityToDto).toList();
    }
}