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

import com.nisum.shoppingcart.beans.Product;
import com.nisum.shoppingcart.dao.ProductDao;
import com.nisum.shoppingcart.rest.ProductRestService;

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
public class ProductRestServiceTest {
 
    private MockMvc mockMvc;
 
    @Autowired
    private ProductDao productDao;
 
    @Autowired
    private WebApplicationContext applicationContext;

    @Before
    public void setUp() {
    // We have to reset our mock between tests because the mock objects
    // are managed by the Spring container. If we would not reset them,
    // stubbing and verified behavior would "leak" from one test to another.
    Mockito.reset(productDao);

    mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }
    
    @Configuration
    @EnableWebMvc
    public static class TestConfiguration {
 
        @Bean
        public ProductRestService productRestService() {
            return new ProductRestService();
        }
        
        @Bean
        public ProductDao productDao() {
          return Mockito.mock(ProductDao.class);
        }
 
    }
    
    @Test
    public void testGetProductByID() throws Exception {
        Product product = new Product("1","Test1", 100.0);
        when(productDao.getProductByID("1")).thenReturn((product));
 
        mockMvc.perform(get("/product/1")).andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is("1")))
        .andExpect(jsonPath("$.name", is("Test1")))
        .andExpect(jsonPath("$.price", is(100.0)));
        
        verify(productDao, times(1)).getProductByID("1");
        verifyNoMoreInteractions(productDao);
    }
    
    @Test
    public void testGetProductByIDNotFound() throws Exception {
    
        when(productDao.getProductByID("2")).thenReturn((null));
 
        mockMvc.perform(get("/product/2")).andDo(print())
        .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
        verify(productDao, times(1)).getProductByID("2");
        verifyNoMoreInteractions(productDao);
    }
    
    
    @Test
    public void testGetAllProduct() throws Exception {
    	Product product = new Product("1", "Test1", 100);
    	
    	Product product2 = new Product("2", "Test2", 200);
    	
        when(productDao.getProductList()).thenReturn(Arrays.asList(product, product2));
 
        mockMvc.perform(get("/product/")).andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is("1")))
        .andExpect(jsonPath("$[0].name", is("Test1")))
        .andExpect(jsonPath("$[0].price", is(100.0)))
        .andExpect(jsonPath("$[1].id", is("2")))
        .andExpect(jsonPath("$[1].name", is("Test2")))
        .andExpect(jsonPath("$[1].price", is(200.0)));
        
        verify(productDao, times(1)).getProductList();
        verifyNoMoreInteractions(productDao);
    }
    
    
    @Test
    public void testDeleteProductSuccess() throws Exception {
        Product product = new Product("id1","Product1", 100.0);

        when(productDao.getProductByID(product.getId())).thenReturn(product);
        doNothing().when(productDao).deleteProduct(product.getId());

        mockMvc.perform(
                delete("/product/{id}", product.getId()))
                .andExpect(status().is(204));

        verify(productDao, times(1)).getProductByID(product.getId());
        verify(productDao, times(1)).deleteProduct(product.getId());
        verifyNoMoreInteractions(productDao);
    }

    
    
    @Test
    public void testDeleteProductNotFound() throws Exception {
        Product product = new Product("id1","Product1", 100.0);

        when(productDao.getProductByID(product.getId())).thenReturn(null);
        doNothing().when(productDao).deleteProduct(product.getId());

        mockMvc.perform(
                delete("/product/{id}", product.getId()))
                .andExpect(status().is(404));

        verify(productDao, times(1)).getProductByID(product.getId());
        verifyNoMoreInteractions(productDao);
    }
    
    @Test
    public void testCreateProductFailure() throws Exception {
        Product product = new Product("id1","Product1", 100.0);

        when(productDao.getProductByID(product.getId())).thenReturn(product);
        doNothing().when(productDao).createProduct(product);

        mockMvc.perform(
                post("/product/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(product)))
                .andExpect(status().is(409));

        verify(productDao, times(1)).getProductByID(product.getId());
        verifyNoMoreInteractions(productDao);
    }

    
    @Test
    public void testCreateProductSuccess() throws Exception {
        Product product = new Product("id2","Product2", 100.0);

        when(productDao.getProductByID(product.getId())).thenReturn(null);
        doNothing().when(productDao).createProduct(product);

        mockMvc.perform(
                post("/product/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(product)))
                .andExpect(status().is(201));

        verify(productDao, times(1)).getProductByID(product.getId());
        verify(productDao, times(1)).createProduct(any());
        verifyNoMoreInteractions(productDao);
    }

    @Test
    public void testUpdateProductFailure() throws Exception {
        Product product = new Product("id1","Product1", 100.0);

        when(productDao.getProductByID(product.getId())).thenReturn(null);
        doNothing().when(productDao).updateProduct(product);

        mockMvc.perform(
                put("/product/id1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(product)))
                .andExpect(status().is(404));

        verify(productDao, times(1)).getProductByID(product.getId());
        verifyNoMoreInteractions(productDao);
    }

    
    @Test
    public void testUpdateProductSuccess() throws Exception {
        Product product = new Product("id2","Product2", 100.0);
        Product newProduct = new Product("id2","ProductTest", 200.0);

        when(productDao.getProductByID(product.getId())).thenReturn(product);
        doNothing().when(productDao).updateProduct(newProduct);

        mockMvc.perform(
                put("/product/id2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(product)))
                .andExpect(status().is(204));

        verify(productDao, times(1)).getProductByID(product.getId());
        verify(productDao, times(1)).updateProduct(any());
        verifyNoMoreInteractions(productDao);
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

