
package com.s2.LogiTrack.service.impl;

import com.s2.LogiTrack.dto.request.UsuarioActualizarDtoRequest;
import com.s2.LogiTrack.dto.request.UsuarioRegistroDtoRequest;
import com.s2.LogiTrack.dto.response.UsuarioDtoResponse;
import com.s2.LogiTrack.exception.BusinessRuleException;
import com.s2.LogiTrack.mapper.UsuarioMapper;
import com.s2.LogiTrack.model.Usuario;
import com.s2.LogiTrack.repository.UsuarioRepository;
import com.s2.LogiTrack.service.interfaces.UsuarioService;

import jakarta.persistence.EntityNotFoundException;
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

    // Registra un usuario nuevo.
    @Override
    public UsuarioDtoResponse guardar(UsuarioRegistroDtoRequest dto) {
        if (usuarioRepository.existsByCorreo(dto.correo())) {
            throw new BusinessRuleException("El correo ya se encuentra registrado");
        }
        Usuario usuario = usuarioMapper.dtoToEntity(dto);
        // Cifra la contraseña antes de guardarla.
        usuario.setPassword(passwordEncoder.encode(dto.password()));
        usuarioRepository.save(usuario);
        return usuarioMapper.entityToDto(usuario);
    }
    // Actualiza los datos básicos de un usuario.
    @Override
    public UsuarioDtoResponse actualizar(UsuarioActualizarDtoRequest dto, Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        // Comprueba que el correo no pertenezca a otro usuario.
        Usuario usuarioCorreo = usuarioRepository.findByCorreo(dto.correo());
        if (usuarioCorreo != null && !usuarioCorreo.getId().equals(id)) {throw new BusinessRuleException("El correo ya está registrado por otro usuario");}
        usuarioMapper.updateEntityToDto(usuario, dto);
        usuarioRepository.save(usuario);
        return usuarioMapper.entityToDto(usuario);
    }

    // Elimina un usuario por su ID.
    @Override
    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));usuarioRepository.delete(usuario);
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
    public UsuarioDtoResponse aprobar(Long id) {

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        if (usuario.getEstado() != Usuario.Estado.PENDIENTE) {
            throw new BusinessRuleException("El usuario no tiene una solicitud pendiente");
        }
        usuario.setEstado(Usuario.Estado.APROBADO);
        usuarioRepository.save(usuario);
        return usuarioMapper.entityToDto(usuario);
    }

    // Rechaza una solicitud de registro.
    @Override
    public UsuarioDtoResponse rechazar(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        if (usuario.getEstado() != Usuario.Estado.PENDIENTE) {
            throw new BusinessRuleException("El usuario no tiene una solicitud pendiente");
        }
        usuario.setEstado(Usuario.Estado.RECHAZADO);
        usuarioRepository.save(usuario);
        return usuarioMapper.entityToDto(usuario);
    }
}
