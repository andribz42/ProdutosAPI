package br.com.apr;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import br.com.apr.application.controller.FornecedorController;
import br.com.apr.application.controller.ProdutoController;
import br.com.apr.application.handlers.BadRequestHandler;
import br.com.apr.application.handlers.InternalServerErrorHandler;
import br.com.apr.application.handlers.NotFoundHandler;
import br.com.apr.application.handlers.UnprocessableEntityHandler;
import br.com.apr.domain.dtos.FornecedorRequest;
import br.com.apr.domain.dtos.FornecedorResponse;
import br.com.apr.domain.dtos.ProdutoRequest;
import br.com.apr.domain.dtos.ProdutoResponse;
import br.com.apr.domain.exceptions.FornecedorAlreadyRegisteredException;
import br.com.apr.domain.exceptions.FornecedorNotFoundException;
import br.com.apr.domain.exceptions.ProdutoNotFoundException;
import br.com.apr.domain.service.FornecedorService;
import br.com.apr.domain.service.ProdutoService;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutosApiApplicationTests {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;
	@MockBean FornecedorService fornecedorService;
	@InjectMocks FornecedorController fornecedorController;
	@MockBean ProdutoService produtoService;
	@InjectMocks ProdutoController produtoController;
	
	private Faker faker;
	
	@SuppressWarnings("deprecation")
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(
				produtoController, fornecedorController)
				.setControllerAdvice(
						new BadRequestHandler(), 
						new InternalServerErrorHandler(),
						new NotFoundHandler(),
						new UnprocessableEntityHandler()).build();
		faker = new Faker(Locale.forLanguageTag("pt-BR"));
	}
	
	@Test
	void shouldCreateFornecedorSuccessfully() throws Exception {
		FornecedorRequest request = new FornecedorRequest();
		request.setNome(faker.name().fullName());
		
		FornecedorResponse response = new FornecedorResponse();
		response.setNome(request.getNome());
		response.setId(UUID.randomUUID());
		
		when(fornecedorService.create(any(FornecedorRequest.class))).thenReturn(response);
		
		mockMvc.perform(post("/api/fornecedores")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.nome").value(response.getNome()))
				.andExpect(jsonPath("$.id").value(response.getId().toString()));
	}
	
	@Test
	void shouldNotCreateAndReturnFornecedorAlreadyRegisteredException() throws Exception {
		FornecedorRequest request = new FornecedorRequest();
		
		when(fornecedorService.create(any(FornecedorRequest.class)))
			.thenThrow(new FornecedorAlreadyRegisteredException());
		
		mockMvc.perform(post("/api/fornecedores")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().string("[\"Fornecedor jÃ¡ cadastrado no sistema.\"]"));
	}
	
	@Test
	void shouldUpdateFornecedorSuccessfully() throws Exception {
		FornecedorRequest request = new FornecedorRequest();
		request.setNome(faker.name().fullName());
		UUID id = UUID.randomUUID();
		
		FornecedorResponse response = new FornecedorResponse();
		response.setNome(request.getNome());
		response.setId(id);
		
		when(fornecedorService.update(eq(id), any(FornecedorRequest.class))).thenReturn(response);
		
		mockMvc.perform(put("/api/fornecedores/" + id.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value(response.getNome()))
				.andExpect(jsonPath("$.id").value(response.getId().toString()));
	}
	
	@Test
	void shouldNotUpdateAndReturnFornecedorNotFoundExceptionWhile() throws Exception {
		FornecedorRequest request = new FornecedorRequest();
		
		when(fornecedorService.update(any(), any(FornecedorRequest.class)))
			.thenThrow(new FornecedorNotFoundException());
		
		mockMvc.perform(put("/api/fornecedores/" + UUID.randomUUID().toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound())
				.andExpect(content().string("[\"Fornecedor informado nÃ£o existe no sistema.\"]"));
	}
	
	@Test
	void shouldDeleteFornecedorSuccessfully() throws Exception {
		UUID id = UUID.randomUUID();
		
		FornecedorResponse response = new FornecedorResponse();
		response.setNome(faker.name().fullName());
		response.setId(id);
		
		when(fornecedorService.delete(eq(id))).thenReturn(response);
		
		mockMvc.perform(delete("/api/fornecedores/" + id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value(response.getNome()))
				.andExpect(jsonPath("$.id").value(response.getId().toString()));
	}
	
	@Test
	void shouldNotDeleteAndReturnFornecedorNotFoundExceptionWhile() throws Exception {
		when(fornecedorService.delete(any()))
			.thenThrow(new FornecedorNotFoundException());
		
		mockMvc.perform(delete("/api/fornecedores/" + UUID.randomUUID().toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().string("[\"Fornecedor informado nÃ£o existe no sistema.\"]"));
	}
	
	@Test
	void shouldListFornecedorSuccessfully() throws Exception {
		UUID id = UUID.randomUUID();
		
		FornecedorResponse response = new FornecedorResponse();
		response.setNome(faker.name().fullName());
		response.setId(id);
		
		when(fornecedorService.getById(eq(id))).thenReturn(response);
		
		mockMvc.perform(get("/api/fornecedores/" + id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value(response.getNome()))
				.andExpect(jsonPath("$.id").value(response.getId().toString()));
	}
	
	@Test
	void shouldNotListAndReturnFornecedorNotFoundException() throws Exception {
		when(fornecedorService.getById(any()))
			.thenThrow(new FornecedorNotFoundException());
		
		mockMvc.perform(get("/api/fornecedores/" + UUID.randomUUID().toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().string("[\"Fornecedor informado nÃ£o existe no sistema.\"]"));
	}
	
	@Test
	void shouldListAllFornecedorSuccessfully() throws Exception {
		UUID id = UUID.randomUUID();
		
		FornecedorResponse response = new FornecedorResponse();
		response.setNome(faker.name().fullName());
		response.setId(id);
		ArrayList<FornecedorResponse> responseList = new ArrayList<FornecedorResponse>();
		responseList.add(response);
		
		when(fornecedorService.getAll(any())).thenReturn(responseList);
		
		mockMvc.perform(get("/api/fornecedores")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].nome").value(response.getNome()))
				.andExpect(jsonPath("$[0].id").value(response.getId().toString()));
	}
	
	@Test
	void shouldCreateProdutoSuccessfully() throws Exception {
		ProdutoRequest request = new ProdutoRequest();
		request.setNome(faker.name().fullName());
		request.setPreco(faker.number().randomDouble(2, 1, 100));
		request.setQuantidade(faker.number().numberBetween(1, 99));
		request.setFornecedorId(UUID.randomUUID());
		
		ProdutoResponse response = new ProdutoResponse();
		response.setId(UUID.randomUUID());
		response.setNome(request.getNome());
		response.setPreco(request.getPreco());
		response.setQuantidade(request.getQuantidade());
		response.setFornecedorId(request.getFornecedorId());
		
		when(produtoService.create(any(ProdutoRequest.class))).thenReturn(response);
		
		mockMvc.perform(post("/api/produtos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(response.getId().toString()))
				.andExpect(jsonPath("$.nome").value(response.getNome()))
				.andExpect(jsonPath("$.preco").value(response.getPreco()))
				.andExpect(jsonPath("$.quantidade").value(response.getQuantidade()))
				.andExpect(jsonPath("$.fornecedorId").value(response.getFornecedorId().toString()));
	}
	
	@Test
	void shouldNotCreateAndReturnFornecedorNotFoundException() throws Exception {
		ProdutoRequest request = new ProdutoRequest();
		
		when(produtoService.create(any(ProdutoRequest.class)))
			.thenThrow(new FornecedorNotFoundException());
		
		mockMvc.perform(post("/api/produtos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound())
				.andExpect(content().string("[\"Fornecedor informado nÃ£o existe no sistema.\"]"));
	}
	
	@Test
	void shouldUpdateProdutoSuccessfully() throws Exception {
		ProdutoRequest request = new ProdutoRequest();
		UUID id = UUID.randomUUID();
		request.setNome(faker.name().fullName());
		request.setPreco(faker.number().randomDouble(2, 1, 100));
		request.setQuantidade(faker.number().numberBetween(1, 99));
		request.setFornecedorId(UUID.randomUUID());
		
		ProdutoResponse response = new ProdutoResponse();
		response.setId(id);
		response.setNome(request.getNome());
		response.setPreco(request.getPreco());
		response.setQuantidade(request.getQuantidade());
		response.setFornecedorId(request.getFornecedorId());
		
		when(produtoService.update(eq(id), any(ProdutoRequest.class))).thenReturn(response);
		
		mockMvc.perform(put("/api/produtos/" + id.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(response.getId().toString()))
				.andExpect(jsonPath("$.nome").value(response.getNome()))
				.andExpect(jsonPath("$.preco").value(response.getPreco()))
				.andExpect(jsonPath("$.quantidade").value(response.getQuantidade()))
				.andExpect(jsonPath("$.fornecedorId").value(response.getFornecedorId().toString()));
	}
	
	@Test
	void shouldNotUpdateAndReturnFornecedorNotFoundException() throws Exception {
		ProdutoRequest request = new ProdutoRequest();
		
		when(produtoService.update(any(), any(ProdutoRequest.class)))
			.thenThrow(new FornecedorNotFoundException());
		
		mockMvc.perform(put("/api/produtos/" + UUID.randomUUID().toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound())
				.andExpect(content().string("[\"Fornecedor informado nÃ£o existe no sistema.\"]"));
	}
	
	@Test
	void shouldNotUpdateAndReturnProdutoNotFoundExceptionWhile() throws Exception {
		ProdutoRequest request = new ProdutoRequest();
		
		when(produtoService.update(any(), any(ProdutoRequest.class)))
			.thenThrow(new ProdutoNotFoundException());
		
		mockMvc.perform(put("/api/produtos/" + UUID.randomUUID().toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound())
				.andExpect(content().string("[\"Produto informado nÃ£o existe no sistema.\"]"));
	}
	
	@Test
	void shouldDeleteProdutoSuccessfully() throws Exception {
		UUID id = UUID.randomUUID();
		
		ProdutoResponse response = new ProdutoResponse();
		response.setId(id);
		response.setNome(faker.name().fullName());
		response.setPreco(faker.number().randomDouble(2, 1, 100));
		response.setQuantidade(faker.number().numberBetween(1, 99));
		response.setFornecedorId(UUID.randomUUID());
		
		when(produtoService.delete(eq(id))).thenReturn(response);
		
		mockMvc.perform(delete("/api/produtos/" + id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(response.getId().toString()))
				.andExpect(jsonPath("$.nome").value(response.getNome()))
				.andExpect(jsonPath("$.preco").value(response.getPreco()))
				.andExpect(jsonPath("$.quantidade").value(response.getQuantidade()))
				.andExpect(jsonPath("$.fornecedorId").value(response.getFornecedorId().toString()));
	}
	
	@Test
	void shouldNotDeleteAndReturnProdutoNotFoundExceptionWhile() throws Exception {
		when(produtoService.delete(any()))
			.thenThrow(new ProdutoNotFoundException());
		
		mockMvc.perform(delete("/api/produtos/" + UUID.randomUUID().toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().string("[\"Produto informado nÃ£o existe no sistema.\"]"));
	}
	
	@Test
	void shouldListProdutoSuccessfully() throws Exception {
		UUID id = UUID.randomUUID();
		
		ProdutoResponse response = new ProdutoResponse();
		response.setId(id);
		response.setNome(faker.name().fullName());
		response.setPreco(faker.number().randomDouble(2, 1, 100));
		response.setQuantidade(faker.number().numberBetween(1, 99));
		response.setFornecedorId(UUID.randomUUID());
		
		when(produtoService.getById(eq(id))).thenReturn(response);
		
		mockMvc.perform(get("/api/produtos/" + id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(response.getId().toString()))
				.andExpect(jsonPath("$.nome").value(response.getNome()))
				.andExpect(jsonPath("$.preco").value(response.getPreco()))
				.andExpect(jsonPath("$.quantidade").value(response.getQuantidade()))
				.andExpect(jsonPath("$.fornecedorId").value(response.getFornecedorId().toString()));
	}
	
	@Test
	void shouldNotListAndReturnProdutoNotFoundExceptionWhile() throws Exception {
		when(produtoService.getById(any()))
			.thenThrow(new ProdutoNotFoundException());
		
		mockMvc.perform(get("/api/produtos/" + UUID.randomUUID().toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().string("[\"Produto informado nÃ£o existe no sistema.\"]"));
	}
	
	@Test
	void shouldListAllProdutoSuccessfully() throws Exception {
		UUID id = UUID.randomUUID();
		
		ProdutoResponse response = new ProdutoResponse();
		response.setId(id);
		response.setNome(faker.name().fullName());
		response.setPreco(faker.number().randomDouble(2, 1, 100));
		response.setQuantidade(faker.number().numberBetween(1, 99));
		response.setFornecedorId(UUID.randomUUID());
		
		ArrayList<ProdutoResponse> responseList = new ArrayList<ProdutoResponse>();
		responseList.add(response);
		
		when(produtoService.getAll(any())).thenReturn(responseList);
		
		mockMvc.perform(get("/api/produtos")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].id").value(response.getId().toString()))
				.andExpect(jsonPath("$[0].nome").value(response.getNome()))
				.andExpect(jsonPath("$[0].preco").value(response.getPreco()))
				.andExpect(jsonPath("$[0].quantidade").value(response.getQuantidade()))
				.andExpect(jsonPath("$[0].fornecedorId").value(response.getFornecedorId().toString()));
	}
}