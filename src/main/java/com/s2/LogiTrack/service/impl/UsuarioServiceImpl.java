
package com.s2.LogiTrack.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s2.LogiTrack.dto.request.UsuarioActualizarDtoRequest;
import com.s2.LogiTrack.dto.request.UsuarioRegistroDtoRequest;
import com.s2.LogiTrack.dto.response.UsuarioDtoResponse;
import com.s2.LogiTrack.exception.BusinessRuleException;
import com.s2.LogiTrack.mapper.UsuarioMapper;
import com.s2.LogiTrack.model.Auditoria;
import com.s2.LogiTrack.model.Usuario;
import com.s2.LogiTrack.repository.UsuarioRepository;
import com.s2.LogiTrack.service.interfaces.AuditoriaService;
import com.s2.LogiTrack.service.interfaces.UsuarioService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;
    private final ObjectMapper objectMapper;

    // Registra un usuario nuevo.
    @Override
    @Transactional
    public UsuarioDtoResponse guardar(UsuarioRegistroDtoRequest dto) {
        if (usuarioRepository.existsByCorreo(dto.correo())) {
            throw new BusinessRuleException("El correo ya se encuentra registrado");
        }
        Usuario usuario = usuarioMapper.dtoToEntity(dto);
        usuario.setPassword(passwordEncoder.encode(dto.password()));
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        UsuarioDtoResponse respuesta = usuarioMapper.entityToDto(usuarioGuardado);
        // El registro es público: todavía NO existe un usuario autenticado.
        // AuditoriaService.registrar requiere el ID de un usuario autenticado.
        // Intentar auditar esta operación hacía fallar y revertir toda la transacción.
        // La posterior aprobación o rechazo sí queda auditada por un ADMIN.
        return respuesta;
    }


    @Override
    @Transactional
    public UsuarioDtoResponse actualizar(UsuarioActualizarDtoRequest dto, Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Usuario usuarioCorreo = usuarioRepository.findByCorreo(dto.correo());
        if (usuarioCorreo != null && !usuarioCorreo.getId().equals(id)) {throw new BusinessRuleException("El correo ya está registrado por otro usuario");}
        JsonNode valoresAnteriores = objectMapper.valueToTree(usuarioMapper.entityToDto(usuario));
        usuarioMapper.updateEntityToDto(usuario, dto);
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        UsuarioDtoResponse respuesta = usuarioMapper.entityToDto(usuarioActualizado);
        JsonNode valoresNuevos = objectMapper.valueToTree(respuesta);
        auditoriaService.registrar(Auditoria.TipoOperacion.UPDATE, "Usuario", valoresAnteriores, valoresNuevos);
        return respuesta;
    }

    // Elimina un usuario por su ID.
    @Override
    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        JsonNode valoresAnteriores = objectMapper.valueToTree(usuarioMapper.entityToDto(usuario));
        usuarioRepository.delete(usuario);
        auditoriaService.registrar(Auditoria.TipoOperacion.DELETE, "Usuario", valoresAnteriores, null);
    }

    // Lista todos los usuarios.
    @Override
    public List<UsuarioDtoResponse> listar() {
        return usuarioRepository.findAll().stream().map(usuarioMapper::entityToDto).toList();
    }

    // Busca un usuario por su ID.
    @Override
    public UsuarioDtoResponse buscarId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        return usuarioMapper.entityToDto(usuario);
    }

    // Busca un usuario por su correo.
    @Override
    public UsuarioDtoResponse buscarCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario == null) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }
        return usuarioMapper.entityToDto(usuario);
    }
    // Consulta los usuarios que esperan aprobación.
    @Override
    public List<UsuarioDtoResponse> listarPendientes() {return usuarioRepository.findByEstado(Usuario.Estado.PENDIENTE).stream().map(usuarioMapper::entityToDto).toList();
    }

    // Aprueba una solicitud de registro.
    @Override
    @Transactional
    public UsuarioDtoResponse aprobar(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        if (usuario.getEstado() != Usuario.Estado.PENDIENTE) {throw new BusinessRuleException("El usuario no tiene una solicitud pendiente");}
        JsonNode valoresAnteriores = objectMapper.valueToTree(usuarioMapper.entityToDto(usuario));
        usuario.setEstado(Usuario.Estado.APROBADO);
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        UsuarioDtoResponse respuesta = usuarioMapper.entityToDto(usuarioActualizado);
        JsonNode valoresNuevos = objectMapper.valueToTree(respuesta);
        auditoriaService.registrar(Auditoria.TipoOperacion.UPDATE, "Usuario", valoresAnteriores, valoresNuevos);
        return respuesta;
    }

    // Rechaza una solicitud de registro.
    @Override
    @Transactional
    public UsuarioDtoResponse rechazar(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        if (usuario.getEstado() != Usuario.Estado.PENDIENTE) {throw new BusinessRuleException("El usuario no tiene una solicitud pendiente");}
        JsonNode valoresAnteriores = objectMapper.valueToTree(usuarioMapper.entityToDto(usuario));
        usuario.setEstado(Usuario.Estado.RECHAZADO);
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        UsuarioDtoResponse respuesta = usuarioMapper.entityToDto(usuarioActualizado);
        JsonNode valoresNuevos = objectMapper.valueToTree(respuesta);
        auditoriaService.registrar(Auditoria.TipoOperacion.UPDATE, "Usuario", valoresAnteriores, valoresNuevos);
        return respuesta;
    }
}
