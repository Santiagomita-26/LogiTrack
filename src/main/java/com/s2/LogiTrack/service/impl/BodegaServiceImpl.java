package com.s2.LogiTrack.service.impl;

import com.s2.LogiTrack.dto.request.BodegaDtoRequest;
import com.s2.LogiTrack.dto.response.BodegaDtoResponse;
import com.s2.LogiTrack.mapper.BodegaMapper;
import com.s2.LogiTrack.model.Bodega;
import com.s2.LogiTrack.model.Ciudad;
import com.s2.LogiTrack.repository.BodegaRepository;
import com.s2.LogiTrack.repository.CiudadRepository;
import com.s2.LogiTrack.service.interfaces.BodegaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BodegaServiceImpl implements BodegaService {

    private final BodegaMapper bodegaMapper;
    private final BodegaRepository bodegaRepository;
    private final CiudadRepository ciudadRepository;

    @Override
    public BodegaDtoResponse guardar(BodegaDtoRequest dto) {
        Ciudad ciudad = ciudadRepository.findById(dto.ciudadId()).orElseThrow(() -> new RuntimeException("Error, la ciudad no existe"));
        Bodega bodega = bodegaMapper.dtoToEntity(dto, ciudad);
        Bodega bodegaRespuesta = bodegaRepository.save(bodega);
        return bodegaMapper.entityToDto(bodegaRespuesta);
    }

    @Override
    public BodegaDtoResponse actualizar(BodegaDtoRequest dto, Long id) {
        Bodega bodega = bodegaRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, bodega a actualizar no encontrada"));
        Ciudad ciudad = ciudadRepository.findById(dto.ciudadId()).orElseThrow(() -> new RuntimeException("Error, la ciudad no existe"));
        bodegaMapper.updateEntityToDto(bodega, dto, ciudad);
        Bodega bodegaActualizada = bodegaRepository.save(bodega);
        return bodegaMapper.entityToDto(bodegaActualizada);
    }

    @Override
    public void eliminar(Long id) {
        Bodega bodega = bodegaRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, bodega a eliminar no existe"));
        bodegaRepository.delete(bodega);
    }

    @Override
    public List<BodegaDtoResponse> listar() {
        return bodegaRepository.findAll().stream().map(bodegaMapper::entityToDto).toList();
    }

    @Override
    public BodegaDtoResponse buscarId(Long id) {
        Bodega bodega = bodegaRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, no existe una bodega con ese ID"));
        return bodegaMapper.entityToDto(bodega);
    }

    @Override
    public List<BodegaDtoResponse> buscarCiudadId(Long id) {

        if (!ciudadRepository.existsById(id)) {
            throw new RuntimeException("Error, no existe una ciudad con ese ID");
        }
        return bodegaRepository.findByCiudadId(id).stream().map(bodegaMapper::entityToDto).toList();
    }
}