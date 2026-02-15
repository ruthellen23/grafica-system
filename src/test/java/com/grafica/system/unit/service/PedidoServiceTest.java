package com.grafica.system.unit.service;

import com.grafica.system.entity.Cliente;
import com.grafica.system.entity.Pedido;
import com.grafica.system.entity.Usuario;
import com.grafica.system.enums.TipoUsuario;
import com.grafica.system.repository.ClienteRepository;
import com.grafica.system.repository.PedidoRepository;
import com.grafica.system.repository.UsuarioRepository;
import com.grafica.system.service.PedidoService;
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
        String emailTeste = "maria@email.com";

        Usuario maria = new Usuario();
        maria.setKeycloakId("uuid-keycloak-maria");
        maria.setUsername("maria");
        maria.setEmail(emailTeste);
        maria.setTipo(TipoUsuario.CLIENTE);

        Cliente clienteMaria = new Cliente();
        clienteMaria.setId(1L);
        clienteMaria.setEmail(emailTeste);

        Cliente clienteJoao = new Cliente();
        clienteJoao.setId(2L);

        Pedido pedidoJoao = new Pedido();
        pedidoJoao.setId(10L);
        pedidoJoao.setCliente(clienteJoao);

        when(usuarioRepository.findByEmail(emailTeste)).thenReturn(Optional.of(maria));
        when(pedidoRepository.findById(10L)).thenReturn(Optional.of(pedidoJoao));
        when(clienteRepository.findByEmail(emailTeste)).thenReturn(Optional.of(clienteMaria));

        assertThrows(RuntimeException.class, () -> {
            pedidoService.buscarPorId(10L, emailTeste);
        });
    }
}