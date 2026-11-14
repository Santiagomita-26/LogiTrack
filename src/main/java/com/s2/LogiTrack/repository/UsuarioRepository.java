package com.s2.LogiTrack.repository;

import com.s2.LogiTrack.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    List<Usuario> findByEstado(Usuario.Estado estado);
    List<Usuario> findByRol(Usuario.Rol rol);

}