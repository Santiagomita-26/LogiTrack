package com.s2.LogiTrack.service.impl;

import com.s2.LogiTrack.dto.request.CiudadDtoRequest;
import com.s2.LogiTrack.dto.response.CiudadDtoResponse;
import com.s2.LogiTrack.mapper.CiudadMapper;
import com.s2.LogiTrack.model.Ciudad;
import com.s2.LogiTrack.repository.CiudadRepository;
import com.s2.LogiTrack.service.interfaces.CiudadService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor

public class CiudadServiceImpl implements CiudadService {

    private final CiudadMapper ciudadMapper;
    private final CiudadRepository ciudadRepository;

    @Override
    public CiudadDtoResponse guardar(CiudadDtoRequest dto) {

        Ciudad ciudad = ciudadMapper.dtoToEntity(dto);
        Ciudad ciudadRespuesta = ciudadRepository.save(ciudad);
        return ciudadMapper.entityToDto(ciudadRespuesta);
    }

    @Override
    public CiudadDtoResponse actualizar(CiudadDtoRequest dto, Long id) {

        Ciudad ciudad = ciudadRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, ciudad a actualizar no encontrada"));
        ciudadMapper.updateEntityToDto(ciudad, dto);
        Ciudad ciudadActualizada = ciudadRepository.save(ciudad);
        return ciudadMapper.entityToDto(ciudadActualizada);
    }

    @Override
    public void eliminar(Long id) {
        Ciudad ciudad = ciudadRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, ciudad a eliminar no existe"));
        ciudadRepository.delete(ciudad);
    }

    @Override
    public List<CiudadDtoResponse> listar() {
        return ciudadRepository.findAll().stream().map(ciudadMapper::entityToDto).toList();
    }

    @Override
    public CiudadDtoResponse buscarId(Long id) {
        Ciudad ciudad = ciudadRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, no existe una ciudad con ese ID"));
        return ciudadMapper.entityToDto(ciudad);
    }

}