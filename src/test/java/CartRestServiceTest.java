package test.java;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.nisum.shoppingcart.beans.CartItem;
import com.nisum.shoppingcart.beans.Product;
import com.nisum.shoppingcart.dao.CartItemDao;
import com.nisum.shoppingcart.dao.ProductDao;
import com.nisum.shoppingcart.rest.CartItemRestService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.core.Is.is;
import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class CartRestServiceTest {

	private MockMvc mockMvc;

	@Autowired
	private CartItemDao cartItemDao;

	@Autowired
	private WebApplicationContext applicationContext;

	@Before
	public void setUp() {
		// We have to reset our mock between tests because the mock objects
		// are managed by the Spring container. If we would not reset them,
		// stubbing and verified behavior would "leak" from one test to another.
		Mockito.reset(cartItemDao);

		mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
	}

	@Configuration
	@EnableWebMvc
	public static class TestConfiguration {

		@Bean
		public CartItemRestService cartItemRestService() {
			return new CartItemRestService();
		}

		@Bean
		public CartItemDao cartItemDao() {
			return Mockito.mock(CartItemDao.class);
		}

		@Bean
		public ProductDao productDao() {
			return Mockito.mock(ProductDao.class);
		}

	}

	@Test
	public void testGetCartItemByID() throws Exception {
		Product product = new Product("1", "Test1", 100.0);
		CartItem cartItem = new CartItem(product, 2, 200);
		
		when(cartItemDao.getCartItemByID("1")).thenReturn((cartItem));

		mockMvc.perform(get("/cartItem/1")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.quantity", is(2.0))).andExpect(jsonPath("$.totalPrice", is(200.0)))
				.andExpect(jsonPath("$.product.id", is("1"))).andExpect(jsonPath("$.product.name", is("Test1")));

		verify(cartItemDao, times(1)).getCartItemByID("1");
		verifyNoMoreInteractions(cartItemDao);
	}

	@Test
	public void testGetCartItemByIDNotFound() throws Exception {

		when(cartItemDao.getCartItemByID("2")).thenReturn((null));

		mockMvc.perform(get("/cartItem/2")).andDo(print()).andExpect(status().is(HttpStatus.NOT_FOUND.value()));
		verify(cartItemDao, times(1)).getCartItemByID("2");
		verifyNoMoreInteractions(cartItemDao);
	}

	@Test
	public void testGetAllCartItem() throws Exception {
		Product product = new Product("1", "Test1", 100.0);
		CartItem cartItem = new CartItem(product, 2, 200);

		Product product2 = new Product("2", "Test2", 200);
		CartItem cartItem2 = new CartItem(product2, 3, 600);

		when(cartItemDao.getCartItems()).thenReturn(Arrays.asList(cartItem, cartItem2));

		mockMvc.perform(get("/cartItem/")).andDo(print()).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].product.id", is("1"))).andExpect(jsonPath("$[0].product.name", is("Test1")))
				.andExpect(jsonPath("$[0].quantity", is(2.0))).andExpect(jsonPath("$[1].quantity", is(3.0)))
				.andExpect(jsonPath("$[0].product.price", is(100.0))).andExpect(jsonPath("$[1].product.id", is("2")))
				.andExpect(jsonPath("$[1].product.name", is("Test2"))).andExpect(jsonPath("$[1].product.price", is(200.0)));

		verify(cartItemDao, times(1)).getCartItems();
		verifyNoMoreInteractions(cartItemDao);
	}

	@Test
	public void testDeleteCartItemSuccess() throws Exception {
		Product product = new Product("id1", "CartItem1", 100.0);
		CartItem cartItem = new CartItem(product, 2, 200);

		when(cartItemDao.getCartItemByID(product.getId())).thenReturn(cartItem);
		when(cartItemDao.removeFromCart(product.getId())).thenReturn(1);

		mockMvc.perform(delete("/cartItem/{id}", product.getId())).andExpect(status().is(204));

		verify(cartItemDao, times(1)).removeFromCart(product.getId());
		verify(cartItemDao, times(1)).getCartItemByID(product.getId());

		verifyNoMoreInteractions(cartItemDao);
	}

	@Test
	public void testDeleteCartItemNotFound() throws Exception {
		Product product = new Product("id1", "CartItem1", 100.0);
		when(cartItemDao.getCartItemByID(product.getId())).thenReturn(null);
		when(cartItemDao.removeFromCart(product.getId())).thenReturn(0);

		mockMvc.perform(delete("/cartItem/{id}", product.getId())).andExpect(status().is(404));

		verify(cartItemDao, times(1)).getCartItemByID(product.getId());
		verifyNoMoreInteractions(cartItemDao);
	}


	@Test
	public void testUpdateCartItemFailure() throws Exception {
		Product product = new Product("id1", "CartItem1", 100.0);

		when(cartItemDao.addToCart(product.getId())).thenReturn(0);

		mockMvc.perform(put("/cartItem/id1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(product)))
				.andExpect(status().is(404));

		verify(cartItemDao, times(1)).addToCart(product.getId());
		verifyNoMoreInteractions(cartItemDao);
	}

	@Test
	public void testUpdateCartItemSuccess() throws Exception {
		Product product = new Product("id1", "CartItem1", 100.0);

		when(cartItemDao.addToCart(product.getId())).thenReturn(1);
		mockMvc.perform(put("/cartItem/id1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(product)))
				.andExpect(status().is(204));

		verify(cartItemDao, times(1)).addToCart(product.getId());
		verifyNoMoreInteractions(cartItemDao);
	}

	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
