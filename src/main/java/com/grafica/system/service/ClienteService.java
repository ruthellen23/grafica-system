package com.grafica.system.service;

import com.grafica.system.dto.ClienteRequest;
import com.grafica.system.entity.Cliente;
import com.grafica.system.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }
    
    public Cliente criar(ClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
        cliente.setTelefone(request.getTelefone());
        cliente.setCpfCnpj(request.getCpfCnpj());
        cliente.setEndereco(request.getEndereco());
        cliente.setCidade(request.getCidade());
        cliente.setEstado(request.getEstado());
        cliente.setCep(request.getCep());
        
        return clienteRepository.save(cliente);
    }
    
    public List<Cliente> listarClientesMaisCompras() {
        return clienteRepository.findTopByTotalCompras();
    }
}
