package br.dev.charles.challenge.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.dev.charles.challenge.controller.resources.LoginResource;
import br.dev.charles.challenge.controller.resources.UserCreateResource;
import br.dev.charles.challenge.controller.resources.UserPhoneResource;
import br.dev.charles.challenge.controller.resources.UserProfileResource;
import br.dev.charles.challenge.utils.StringUtils;

@AutoConfigureMockMvc
@SpringBootTest
public class ControllerTest {
	
	private static ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void userCreateOk() throws Exception {
		MvcResult result = mockMvc.perform(post("/user")
				.contentType("application/json; charset=UTF-8")
				.content(mapper.writeValueAsString(
						new UserCreateResource()
						.setName("José da Silva")
						.setEmail("jose@silva.org")
						.setPassword("hunter2")
						.addUserPhoneResource(new UserPhoneResource("21", "987654321"))

						))
				).andExpect(status().isOk())
				.andReturn();
		
		UserProfileResource profile = mapper.readValue(result.getResponse().getContentAsString(Charset.forName("UTF-8")), UserProfileResource.class);
		assertNotNull(profile.getCreated());
		assertNotNull(profile.getToken());
		assertNotNull(profile.getLast_login());
		assertNotNull(profile.getModified());
		assertEquals("José da Silva", profile.getName());
		assertEquals("jose@silva.org", profile.getEmail());
		assertEquals(StringUtils.sha256("hunter2"), profile.getPassword());
		assertEquals(profile.getPhones().get(0).getDdd(), "21");
		assertEquals(profile.getPhones().get(0).getNumber(), "987654321");
	}
	
	@Test
	void userCreateEmailInvalid() throws Exception {
		MvcResult result = mockMvc.perform(post("/user")
				.contentType("application/json; charset=UTF-8")
				.content(mapper.writeValueAsString(
						new UserCreateResource()
						.setName("Jose da Silva")
						.setEmail("jose@org")
						.setPassword("hunter2")
						.addUserPhoneResource(new UserPhoneResource("21", "987654321"))

						))
				).andExpect(status().isBadRequest())
				.andReturn();
		
		Map<String, String> payload = convertJsonToMap(result);
		assertEquals("Campo email inválido", payload.get("mensagem"));
	}
	
	@Test
	void userCreateBlankPassword() throws Exception {
		MvcResult result = mockMvc.perform(post("/user")
				.contentType("application/json; charset=UTF-8")
				.content(mapper.writeValueAsString(
						new UserCreateResource()
						.setName("Jose da Silva")
						.setEmail("jose@silva.org")
						.addUserPhoneResource(new UserPhoneResource("21", "987654321"))

						))
				).andExpect(status().isBadRequest())
				.andReturn();
		
		Map<String, String> payload = convertJsonToMap(result);
		assertEquals("Campo password inválido", payload.get("mensagem"));
	}
	
	@Test
	void userCreateBlankPasswordName() throws Exception {
		MvcResult result = mockMvc.perform(post("/user")
				.contentType("application/json; charset=utf-8")
				.content(mapper.writeValueAsString(
						new UserCreateResource()
						.setEmail("jose@silva.org")
						.addUserPhoneResource(new UserPhoneResource("21", "987654321"))
						))
				).andExpect(status().isBadRequest())
				.andReturn();
		
		Map<String, String> payload = convertJsonToMap(result);
		assertEquals("Campos name, password inválidos", payload.get("mensagem"));
	}
	
	@Test
	void userCreateWithoutPhone() throws Exception {
		MvcResult result = mockMvc.perform(post("/user")
				.contentType("application/json; charset=UTF-8")
				.content(mapper.writeValueAsString(
						new UserCreateResource()
						.setName("José da Silva")
						.setEmail("jose2@silva.org")
						.setPassword("hunter2")
						))
				).andExpect(status().isOk())
				.andReturn();
		
		UserProfileResource profile = mapper.readValue(result.getResponse().getContentAsString(Charset.forName("UTF-8")), UserProfileResource.class);
		assertNotNull(profile.getCreated());
		assertNotNull(profile.getToken());
		assertNotNull(profile.getLast_login());
		assertNotNull(profile.getModified());
		assertEquals("José da Silva", profile.getName());
		assertEquals("jose2@silva.org", profile.getEmail());
		assertEquals(StringUtils.sha256("hunter2"), profile.getPassword());
		assertNull(profile.getPhones());
	}
	
	@Test
	void loginSucessfull() throws Exception {
		MvcResult result = mockMvc.perform(post("/user")
				.contentType("application/json; charset=UTF-8")
				.content(mapper.writeValueAsString(
						new UserCreateResource()
						.setName("Charles Silva")
						.setEmail("charles@charles.dev.br")
						.setPassword("password312")
						))
				).andExpect(status().isOk())
				.andReturn();
		
		UserProfileResource profile = mapper.readValue(result.getResponse().getContentAsString(Charset.forName("UTF-8")), UserProfileResource.class);
		assertNotNull(profile.getLast_login());
		assertNotNull(profile.getModified());
		assertNotNull(profile.getCreated());
		assertNotNull(profile.getToken());
		
		result = mockMvc.perform(post("/login")
				.contentType("application/json; charset=UTF-8")
				.content(mapper.writeValueAsString(
						new LoginResource()
						.setEmail("charles@charles.dev.br")
						.setPassword("password312")
						))
				).andExpect(status().isOk())
				.andReturn();
		
		UserProfileResource loginResult = mapper.readValue(result.getResponse().getContentAsString(Charset.forName("UTF-8")), UserProfileResource.class);
		assertNotNull(loginResult.getCreated());
		assertNotNull(loginResult.getToken());
		assertNotNull(loginResult.getLast_login());
		assertNotNull(loginResult.getModified());
		assertEquals("Charles Silva", loginResult.getName());
		assertEquals("charles@charles.dev.br", loginResult.getEmail());

		assertNotEquals(profile.getToken(), loginResult.getToken());
		assertNotEquals(profile.getModified(), loginResult.getModified());
		assertNotEquals(profile.getLast_login(), loginResult.getLast_login());
	}
	
	@Test
	void loginEmailInvalid() throws Exception {
		MvcResult result = mockMvc.perform(post("/login")
				.contentType("application/json; charset=UTF-8")
				.content(mapper.writeValueAsString(
						new LoginResource()
						.setEmail("invalido@email.com")
						.setPassword("password31X")
						))
				).andExpect(status().isForbidden())
				.andReturn();
		
		
		Map<String, String> payload = convertJsonToMap(result);
		assertEquals("Usuário/Senha inválido", payload.get("mensagem"));
	}
	
	@Test
	void loginWithoutPassword() throws Exception {
		MvcResult result = mockMvc.perform(post("/login")
				.contentType("application/json; charset=UTF-8")
				.content(mapper.writeValueAsString(
						new LoginResource()
						.setEmail("oi@email.com")
						))
				).andExpect(status().isBadRequest())
				.andReturn();
		
		
		Map<String, String> payload = convertJsonToMap(result);
		assertEquals("Campo password inválido", payload.get("mensagem"));
	}
	
	@Test
	void getProfileWithoutToken() throws Exception {
		MvcResult result = mockMvc.perform(get("/user/profile/{id}/", UUID.randomUUID().toString())
				.contentType("application/json; charset=UTF-8")
				).andExpect(status().isUnauthorized())
				.andReturn();
		
		
		Map<String, String> payload = convertJsonToMap(result);
		assertEquals("Não autorizado", payload.get("mensagem"));
	}
	
	@Test
	void getProfileExpiredToken() throws Exception {
		MvcResult result = mockMvc.perform(post("/user")
				.contentType("application/json; charset=UTF-8")
				.content(mapper.writeValueAsString(
						new UserCreateResource()
						.setName("Charles Silva")
						.setEmail("oi@charles.dev.br")
						.setPassword("password312")
						))
				).andExpect(status().isOk())
				.andReturn();
		
		UserProfileResource created = mapper.readValue(result.getResponse().getContentAsString(Charset.forName("UTF-8")), UserProfileResource.class);
		
		result = mockMvc.perform(post("/login")
				.contentType("application/json; charset=UTF-8")
				.content(mapper.writeValueAsString(
						new LoginResource()
						.setEmail("oi@charles.dev.br")
						.setPassword("password312")
						))
				).andExpect(status().isOk())
				.andReturn();
		
		UserProfileResource logged = mapper.readValue(result.getResponse().getContentAsString(Charset.forName("UTF-8")), UserProfileResource.class);
		
		assertNotEquals(created.getToken(), logged.getToken());
		
		result = mockMvc.perform(get("/user/profile/{id}/", UUID.randomUUID().toString())
				.contentType("application/json; charset=UTF-8")
				.header("token", created.getToken())
				).andExpect(status().isUnauthorized())
				.andReturn();
		
		Map<String, String> payload = convertJsonToMap(result);
		assertEquals("Não autorizado", payload.get("mensagem"));
	}
	
	@Test
	void getProfileUserInvalid() throws Exception {
		MvcResult result = mockMvc.perform(post("/user")
				.contentType("application/json; charset=UTF-8")
				.content(mapper.writeValueAsString(
						new UserCreateResource()
						.setName("Charles Silva")
						.setEmail("email@charles.dev.br")
						.setPassword("password312")
						))
				).andExpect(status().isOk())
				.andReturn();
		
		UserProfileResource created = mapper.readValue(result.getResponse().getContentAsString(Charset.forName("UTF-8")), UserProfileResource.class);
		
		result = mockMvc.perform(get("/user/profile/{id}/", UUID.randomUUID().toString())
				.contentType("application/json; charset=UTF-8")
				.header("token", created.getToken())
				).andExpect(status().isNotFound())
				.andReturn();
		
		Map<String, String> payload = convertJsonToMap(result);
		assertEquals("Usuário não encontrado", payload.get("mensagem"));
	}
	
	@Test
	void getProfileValid() throws Exception {
		MvcResult result = mockMvc.perform(post("/user")
				.contentType("application/json; charset=UTF-8")
				.content(mapper.writeValueAsString(
						new UserCreateResource()
						.setName("Charles Silva")
						.setEmail("email2@charles.dev.br")
						.setPassword("password312")
						))
				).andExpect(status().isOk())
				.andReturn();
		
		UserProfileResource created = mapper.readValue(result.getResponse().getContentAsString(Charset.forName("UTF-8")), UserProfileResource.class);
		
		result = mockMvc.perform(get("/user/profile/{id}/", created.getId())
				.contentType("application/json; charset=UTF-8")
				.header("token", created.getToken())
				).andExpect(status().isOk())
				.andReturn();
		
		UserProfileResource profile = mapper.readValue(result.getResponse().getContentAsString(Charset.forName("UTF-8")), UserProfileResource.class);
		assertEquals(created.getId(), profile.getId());
		assertEquals(created.getEmail(), profile.getEmail());
		assertEquals(created.getToken(), profile.getToken());
	}
	
	
	private Map<String, String> convertJsonToMap(MvcResult result) throws Exception {
		return mapper.readValue(result.getResponse().getContentAsString(Charset.forName("UTF-8")), Map.class);
	}
	
}
