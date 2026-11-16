package com.s2.LogiTrack.service.impl;

import com.s2.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.s2.LogiTrack.mapper.AuditoriaMapper;
import com.s2.LogiTrack.model.Auditoria;
import com.s2.LogiTrack.repository.AuditoriaRepository;
import com.s2.LogiTrack.service.interfaces.AuditoriaService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaMapper auditoriaMapper;
    private final AuditoriaRepository auditoriaRepository;

    @Override
    public List<AuditoriaDtoResponse> listar() {
        return auditoriaRepository.findAll().stream().map(auditoriaMapper::entityToDto).toList();
    }

    @Override
    public AuditoriaDtoResponse buscarId(Long id) {
        Auditoria auditoria = auditoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, auditoría no encontrada"));
        return auditoriaMapper.entityToDto(auditoria);
    }

    @Override
    public List<AuditoriaDtoResponse> buscarUsuarioId(Long id) {
        return auditoriaRepository.findByUsuarioId(id).stream().map(auditoriaMapper::entityToDto).toList();
    }

    @Override
    public List<AuditoriaDtoResponse> buscarOperacion(Auditoria.TipoOperacion operacion) {
        return auditoriaRepository.findByOperacion(operacion).stream().map(auditoriaMapper::entityToDto).toList();
    }

    @Override
    public List<AuditoriaDtoResponse> buscarEntidad(String entidad) {
        return auditoriaRepository.findByEntidad(entidad).stream().map(auditoriaMapper::entityToDto).toList();
    }

    @Override
    public List<AuditoriaDtoResponse> buscarFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return auditoriaRepository.findByFechaBetween(fechaInicio, fechaFin).stream().map(auditoriaMapper::entityToDto).toList();
    }

    @Override
    public List<AuditoriaDtoResponse> buscarUsuarioOperacion(Long usuarioId, Auditoria.TipoOperacion operacion) {
        return auditoriaRepository.findByUsuarioIdAndOperacion(usuarioId, operacion).stream().map(auditoriaMapper::entityToDto).toList();
    }

    @Override
    public List<AuditoriaDtoResponse> buscarUsuarioFecha(Long usuarioId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return auditoriaRepository.findByUsuarioIdAndFechaBetween(usuarioId, fechaInicio, fechaFin).stream().map(auditoriaMapper::entityToDto).toList();
    }
}