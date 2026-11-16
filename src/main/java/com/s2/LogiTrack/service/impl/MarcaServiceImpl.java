package com.s2.LogiTrack.service.impl;

import com.s2.LogiTrack.dto.request.MarcaDtoRequest;
import com.s2.LogiTrack.dto.response.MarcaDtoResponse;
import com.s2.LogiTrack.exception.BusinessRuleException;
import com.s2.LogiTrack.mapper.MarcaMapper;
import com.s2.LogiTrack.model.Marca;
import com.s2.LogiTrack.repository.MarcaRepository;
import com.s2.LogiTrack.service.interfaces.MarcaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarcaServiceImpl implements MarcaService {

    private final MarcaMapper marcaMapper;
    private final MarcaRepository marcaRepository;

    @Override
    public MarcaDtoResponse guardar(MarcaDtoRequest dto) {

        if (marcaRepository.existsByNombre(dto.nombre())) { throw new BusinessRuleException("Error, la marca ya se encuentra registrada");
        }
        Marca marca = marcaMapper.dtoToEntity(dto);
        Marca marcaRespuesta = marcaRepository.save(marca);
        return marcaMapper.entityToDto(marcaRespuesta);
    }

    @Override
    public MarcaDtoResponse actualizar(MarcaDtoRequest dto, Long id) {

        Marca marca = marcaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, marca a actualizar no encontrada"));
        Marca marcaExistente = marcaRepository.findByNombre(dto.nombre());
        if (marcaExistente != null && !marcaExistente.getId().equals(id)) {throw new BusinessRuleException("Error, ya existe otra marca con ese nombre");}
        marcaMapper.updateEntityToDto(marca, dto);
        Marca marcaActualizada = marcaRepository.save(marca);
        return marcaMapper.entityToDto(marcaActualizada);
    }

    @Override
    public void eliminar(Long id) {
        Marca marca = marcaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, marca a eliminar no existe"));
        marcaRepository.delete(marca);
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