package com.grafica.system.controller;

import com.grafica.system.entity.Usuario;
import com.grafica.system.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Usuários", description = "Gerenciamento de usuários")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "keycloak")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna todos os usuários sincronizados no banco local.")
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza dados cadastrais. O Keycloak ID e E-mail são imutáveis.")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioRepository.findById(id)
                .map(usuarioExistente -> {
                    usuarioExistente.setNome(usuario.getNome());
                    usuarioExistente.setTipo(usuario.getTipo());
                    usuarioExistente.setAtivo(usuario.getAtivo());
                    return ResponseEntity.ok(usuarioRepository.save(usuarioExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}