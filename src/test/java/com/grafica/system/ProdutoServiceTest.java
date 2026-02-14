package com.grafica.system;

import com.grafica.system.dto.ProdutoRequest;
import com.grafica.system.entity.Produto;
import com.grafica.system.repository.CategoriaRepository;
import com.grafica.system.repository.ProdutoRepository;
import com.grafica.system.service.ProdutoService;
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
class ProdutoServiceTest {
    
    @Mock
    private ProdutoRepository produtoRepository;
    
    @Mock
    private CategoriaRepository categoriaRepository;
    
    @InjectMocks
    private ProdutoService produtoService;
    
    private Produto produto;
    private ProdutoRequest produtoRequest;
    
    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Cartão de Visita");
        produto.setDescricao("Cartão de visita 300g");
        produto.setPreco(50.0);
        produto.setQuantidadeEstoque(100);
        
        produtoRequest = new ProdutoRequest();
        produtoRequest.setNome("Cartão de Visita");
        produtoRequest.setDescricao("Cartão de visita 300g");
        produtoRequest.setPreco(50.0);
        produtoRequest.setQuantidadeEstoque(100);
    }
    
    @Test
    void deveCriarProdutoComSucesso() {
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
        
        Produto resultado = produtoService.criar(produtoRequest);
        
        assertNotNull(resultado);
        assertEquals("Cartão de Visita", resultado.getNome());
        assertEquals(50.0, resultado.getPreco());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }
    
    @Test
    void deveListarTodosProdutos() {
        List<Produto> produtos = Arrays.asList(produto);
        when(produtoRepository.findAll()).thenReturn(produtos);
        
        List<Produto> resultado = produtoService.listarTodos();
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(produtoRepository, times(1)).findAll();
    }
    
    @Test
    void deveBuscarProdutoPorId() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        
        Produto resultado = produtoService.buscarPorId(1L);
        
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Cartão de Visita", resultado.getNome());
    }
    
    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> produtoService.buscarPorId(1L));
    }
}
