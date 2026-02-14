package com.grafica.system.service;

import com.grafica.system.entity.Cliente;
import com.grafica.system.entity.Pedido;
import com.grafica.system.entity.Usuario;
import com.grafica.system.enums.TipoUsuario;
import com.grafica.system.repository.ClienteRepository;
import com.grafica.system.repository.PedidoRepository;
import com.grafica.system.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    @DisplayName("Deve barrar cliente tentando ver pedido de outro cliente")
    void deveBarrarAcessoIndevido() {
        String username = "maria";
        Usuario maria = new Usuario();
        maria.setUsername(username);
        maria.setEmail("maria@email.com");
        maria.setTipo(TipoUsuario.CLIENTE);

        Cliente clienteMaria = new Cliente();
        clienteMaria.setId(1L);

        Cliente clienteJoao = new Cliente();
        clienteJoao.setId(2L);

        Pedido pedidoJoao = new Pedido();
        pedidoJoao.setId(10L);
        pedidoJoao.setCliente(clienteJoao);

        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(maria));
        when(pedidoRepository.findById(10L)).thenReturn(Optional.of(pedidoJoao));
        when(clienteRepository.findByEmail("maria@email.com")).thenReturn(Optional.of(clienteMaria));

        assertThrows(RuntimeException.class, () -> {
            pedidoService.buscarPorId(10L, username);
        });
    }
}