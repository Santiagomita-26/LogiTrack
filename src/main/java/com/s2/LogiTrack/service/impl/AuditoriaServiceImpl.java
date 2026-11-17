package com.s2.LogiTrack.service.impl;

import com.s2.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.s2.LogiTrack.mapper.AuditoriaMapper;
import com.s2.LogiTrack.model.Auditoria;
import com.s2.LogiTrack.model.Usuario;
import com.s2.LogiTrack.repository.AuditoriaRepository;
import com.s2.LogiTrack.repository.UsuarioRepository;
import com.s2.LogiTrack.service.interfaces.AuditoriaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;


@Service
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaMapper auditoriaMapper;
    private final AuditoriaRepository auditoriaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<AuditoriaDtoResponse> listar() {
        return auditoriaRepository.findAll().stream().map(auditoriaMapper::entityToDto).toList();
    }

    @Override
    public AuditoriaDtoResponse buscarId(Long id) {
        Auditoria auditoria = auditoriaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, auditoría no encontrada"));
        return auditoriaMapper.entityToDto(auditoria);
    }

    @Override
    public List<AuditoriaDtoResponse> buscarUsuarioId(Long id) {return auditoriaRepository.findByUsuarioId(id).stream().map(auditoriaMapper::entityToDto).toList();
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
    public List<AuditoriaDtoResponse> buscarFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin
    ) { return auditoriaRepository.findByFechaBetween(fechaInicio, fechaFin).stream().map(auditoriaMapper::entityToDto).toList();
    }

    @Override
    public List<AuditoriaDtoResponse> buscarUsuarioOperacion(Long usuarioId, Auditoria.TipoOperacion operacion
    ) {
        return auditoriaRepository.findByUsuarioIdAndOperacion(usuarioId, operacion).stream().map(auditoriaMapper::entityToDto).toList();
    }

    @Override
    public List<AuditoriaDtoResponse> buscarUsuarioFecha(Long usuarioId, LocalDateTime fechaInicio, LocalDateTime fechaFin
    ) {
        return auditoriaRepository.findByUsuarioIdAndFechaBetween(usuarioId, fechaInicio, fechaFin).stream().map(auditoriaMapper::entityToDto).toList();
    }

    @Override
    @Transactional
    public void registrar(Auditoria.TipoOperacion operacion, String entidad, JsonNode valoresAnteriores, JsonNode valoresNuevos
    ) {

        // Obtiene la autenticación actual.
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // Comprueba que exista un usuario autenticado.
        if (authentication == null || !authentication.isAuthenticated()) {throw new AuthenticationCredentialsNotFoundException("No existe un usuario autenticado"
            );
        }

        // Obtiene el ID del usuario desde los detalles de autenticación.
        Object usuarioDetalle = authentication.getDetails();

        if (!(usuarioDetalle instanceof Long usuarioId)) {throw new AuthenticationCredentialsNotFoundException("No se pudo identificar al usuario");
        }

        // Busca al usuario en la base de datos.
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new EntityNotFoundException("Error, usuario no encontrado"));

        // Construye el registro de auditoría.
        Auditoria auditoria = new Auditoria();

        auditoria.setOperacion(operacion);
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setUsuario(usuario);
        auditoria.setEntidad(entidad);
        auditoria.setValoresAnteriores(valoresAnteriores);
        auditoria.setValoresNuevos(valoresNuevos);

        // Guarda el registro.
        auditoriaRepository.save(auditoria);
    }
}

