package com.s2.LogiTrack.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s2.LogiTrack.dto.request.MarcaDtoRequest;
import com.s2.LogiTrack.dto.response.MarcaDtoResponse;
import com.s2.LogiTrack.exception.BusinessRuleException;
import com.s2.LogiTrack.mapper.MarcaMapper;
import com.s2.LogiTrack.model.Auditoria;
import com.s2.LogiTrack.model.Marca;
import com.s2.LogiTrack.repository.MarcaRepository;
import com.s2.LogiTrack.service.interfaces.AuditoriaService;
import com.s2.LogiTrack.service.interfaces.MarcaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarcaServiceImpl implements MarcaService {

    private final MarcaMapper marcaMapper;
    private final MarcaRepository marcaRepository;
    private final AuditoriaService auditoriaService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public MarcaDtoResponse guardar(MarcaDtoRequest dto) {

        if (marcaRepository.existsByNombre(dto.nombre())) {throw new BusinessRuleException("Error, la marca ya se encuentra registrada");}

        Marca marca = marcaMapper.dtoToEntity(dto);
        Marca marcaRespuesta = marcaRepository.save(marca);
        MarcaDtoResponse respuesta = marcaMapper.entityToDto(marcaRespuesta);

        // Captura los valores nuevos.
        JsonNode valoresNuevos = objectMapper.valueToTree(respuesta);

        // Registra la operación INSERT.
        auditoriaService.registrar(Auditoria.TipoOperacion.INSERT, "Marca", null, valoresNuevos);
        return respuesta;
    }

    @Override
    @Transactional
    public MarcaDtoResponse actualizar(MarcaDtoRequest dto, Long id) {
        Marca marca = marcaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, marca a actualizar no encontrada"));
        Marca marcaExistente = marcaRepository.findByNombre(dto.nombre());

        if (marcaExistente != null && !marcaExistente.getId().equals(id)) {
            throw new BusinessRuleException("Error, ya existe otra marca con ese nombre");
        }
        // Captura los valores anteriores.
        JsonNode valoresAnteriores = objectMapper.valueToTree(marcaMapper.entityToDto(marca));
        marcaMapper.updateEntityToDto(marca, dto);
        Marca marcaActualizada = marcaRepository.save(marca);
        MarcaDtoResponse respuesta = marcaMapper.entityToDto(marcaActualizada);
        // Captura los valores nuevos.
        JsonNode valoresNuevos = objectMapper.valueToTree(respuesta);

        // Registra la operación UPDATE.
        auditoriaService.registrar(Auditoria.TipoOperacion.UPDATE, "Marca", valoresAnteriores, valoresNuevos);
        return respuesta;
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Marca marca = marcaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, marca a eliminar no existe"));
        // Captura los valores anteriores.
        JsonNode valoresAnteriores = objectMapper.valueToTree(marcaMapper.entityToDto(marca));
        marcaRepository.delete(marca);
        // Registra la operación DELETE.
        auditoriaService.registrar(Auditoria.TipoOperacion.DELETE, "Marca", valoresAnteriores, null
        );
    }

    @Override
    public List<MarcaDtoResponse> listar() {
        return marcaRepository.findAll().stream().map(marcaMapper::entityToDto).toList();
    }

    @Override
    public MarcaDtoResponse buscarId(Long id) {
        Marca marca = marcaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, no existe una marca con ese ID"));
        return marcaMapper.entityToDto(marca);
    }

}