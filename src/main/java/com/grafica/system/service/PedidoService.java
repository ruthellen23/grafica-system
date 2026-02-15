package com.grafica.system.service;

import com.grafica.system.dto.ItemPedidoRequest;
import com.grafica.system.dto.PedidoRequest;
import com.grafica.system.entity.*;
import com.grafica.system.enums.StatusPedido;
import com.grafica.system.enums.TipoUsuario;
import com.grafica.system.repository.ClienteRepository;
import com.grafica.system.repository.PedidoRepository;
import com.grafica.system.repository.ProdutoRepository;
import com.grafica.system.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    private Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado no banco: " + email));
    }

    @Transactional
    public Pedido criarPedido(PedidoRequest request, String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (usuario.getTipo() == TipoUsuario.CLIENTE) {
            Cliente clienteUsuario = clienteRepository.findByEmail(usuario.getEmail())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado para este usuário"));

            if (!cliente.getId().equals(clienteUsuario.getId())) {
                throw new RuntimeException("Você só pode criar pedidos para você mesmo");
            }
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setObservacoes(request.getObservacoes());
        pedido.setStatus(StatusPedido.PENDENTE);

        for (ItemPedidoRequest itemRequest : request.getItens()) {
            Produto produto = produtoRepository.findById(itemRequest.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            if (produto.getQuantidadeEstoque() < itemRequest.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para: " + produto.getNome());
            }

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemRequest.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());

            pedido.getItens().add(item);

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - itemRequest.getQuantidade());
            produto.setTotalVendas(produto.getTotalVendas() + itemRequest.getQuantidade());
            produtoRepository.save(produto);
        }

        pedido.calcularValorTotal();
        pedido = pedidoRepository.save(pedido);

        cliente.setTotalCompras(cliente.getTotalCompras() + 1);
        cliente.setValorTotalGasto(cliente.getValorTotalGasto() + pedido.getValorTotal());
        clienteRepository.save(cliente);

        return pedido;
    }

    public List<Pedido> listarPedidos(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);

        if (usuario.getTipo() == TipoUsuario.ADMIN) {
            return pedidoRepository.findAll();
        } else {
            Cliente cliente = clienteRepository.findByEmail(usuario.getEmail())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            return pedidoRepository.findByClienteId(cliente.getId());
        }
    }

    public Pedido buscarPorId(Long id, String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (usuario.getTipo() == TipoUsuario.CLIENTE) {
            Cliente cliente = clienteRepository.findByEmail(usuario.getEmail())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            if (!pedido.getCliente().getId().equals(cliente.getId())) {
                throw new RuntimeException("Você não tem permissão para ver este pedido");
            }
        }

        return pedido;
    }

    @Transactional
    public Pedido atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPorStatus(StatusPedido status) {
        return pedidoRepository.findByStatus(status);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }
}