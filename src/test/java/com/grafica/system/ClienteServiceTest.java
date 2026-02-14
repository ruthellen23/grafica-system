package com.grafica.system;

import com.grafica.system.dto.ClienteRequest;
import com.grafica.system.entity.Cliente;
import com.grafica.system.repository.ClienteRepository;
import com.grafica.system.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {
    
    @Mock
    private ClienteRepository clienteRepository;
    
    @InjectMocks
    private ClienteService clienteService;
    
    private Cliente cliente;
    private ClienteRequest clienteRequest;
    
    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("11999999999");
        
        clienteRequest = new ClienteRequest();
        clienteRequest.setNome("João Silva");
        clienteRequest.setEmail("joao@email.com");
        clienteRequest.setTelefone("11999999999");
    }
    
    @Test
    void deveCriarClienteComSucesso() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        
        Cliente resultado = clienteService.criar(clienteRequest);
        
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }
    
    @Test
    void deveListarTodosClientes() {
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteRepository.findAll()).thenReturn(clientes);
        
        List<Cliente> resultado = clienteService.listarTodos();
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(clienteRepository, times(1)).findAll();
    }
    
    @Test
    void deveBuscarClientePorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        
        Cliente resultado = clienteService.buscarPorId(1L);
        
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(clienteRepository, times(1)).findById(1L);
    }
    
    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> clienteService.buscarPorId(1L));
    }
}
