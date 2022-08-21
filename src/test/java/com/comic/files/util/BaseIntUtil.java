package com.comic.files.util;

import com.comic.files.ComicFilesApplication;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {ComicFilesApplication.class})
@WebAppConfiguration
@TestConfiguration
public class BaseIntUtil {

    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private EntityManager entityManager;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Atribuir o valor do <code>pWebApplicationContext</code> no atribuito <code>webApplicationContext</code> da Classe.
     *
     * @param pWebApplicationContext Parâmetro de Entrada
     */
    @Autowired
    public void setWebApplicationContext(WebApplicationContext pWebApplicationContext) {
        webApplicationContext = pWebApplicationContext;
    }

    /**
     * Obter o valor do atributo <code>entityManager</code> da classe.
     *
     * @return O atributo <code>entityManager</code>.
     */
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Atribuir o valor do <code>pEntityManager</code> no atribuito <code>entityManager</code> da Classe.
     *
     * @param pEntityManager Parâmetro de Entrada
     */
    @Autowired
    private void setEntityManager(EntityManager pEntityManager) {
        entityManager = pEntityManager;
    }

    /**
     * Retorna o valor do atributo <code>mockMvc</code> do Objeto.
     *
     * @return O atributo <code>mockMvc</code>
     */
    protected MockMvc getMockMvc() {
        return mockMvc;
    }


    protected byte[] toJson(Object object) throws IOException {
        return TestUtil.convertObjectToJsonBytes(object);
    }

    public Object makeRequest(String url, HttpMethod method, HttpStatus status, Class objectClass, MappingJackson2HttpMessageConverter jacksonMessageConverter, Object value){
        try{


            RequestBuilder requestBuilder = getRequestMethod(method,url,value);
            ResultMatcher resultMatcher = getResultMatcher(status);

            return jacksonMessageConverter.getObjectMapper()
                                          .readValue(getMockMvc()
                                          .perform(requestBuilder)
                                          .andExpect(resultMatcher).andReturn().getResponse().getContentAsString(), objectClass);

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public RequestBuilder getRequestMethod(HttpMethod method, String url, Object value) throws IOException {
        switch(method) {
            case GET:
                return get(url);
            case DELETE:
                return delete(url);
            case POST:
                return post(url).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(value));
            case PUT:
                return put(url).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(value));
            default:
                return null;
        }
    }
    public ResultMatcher getResultMatcher(HttpStatus status) {
        switch (status) {
            case OK:
                return status().isOk();
            case BAD_REQUEST:
                return status().isBadRequest();
            case NOT_FOUND:
                return status().isNotFound();
        }

        return null;
    }

    protected void emFlushClear() {
        entityManager.flush();
        entityManager.clear();
    }
}