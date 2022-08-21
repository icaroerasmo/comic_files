package com.comic.files.resource;


import com.comic.files.builder.UserBuilder;
import com.comic.files.dto.DErrorResponse;
import com.comic.files.dto.DUser;
import com.comic.files.util.BaseIntUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class UserResourceIntTest extends BaseIntUtil{

    private static final String BASE_URL = "/user";

    @Autowired
    private UserBuilder userBuilder;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    public void findById(){

        DUser dUser = userBuilder.newAndSaveEntity();
        DUser result = (DUser) makeRequest(BASE_URL+"/"+dUser.getId(), HttpMethod.GET, HttpStatus.OK, DUser.class, jacksonMessageConverter, null);

        Assert.assertEquals(dUser.getId(),result.getId());
        Assert.assertEquals(dUser.getName(),result.getName());
        Assert.assertEquals(dUser.getEmail(),result.getEmail());
        Assert.assertEquals(dUser.getBirthDate(),result.getBirthDate());
        Assert.assertEquals(dUser.getUsername(),result.getUsername());
        Assert.assertEquals(dUser.getPassword(),result.getPassword());

    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    public void findInvalidById(){

        DErrorResponse result = (DErrorResponse) makeRequest(BASE_URL+"/"+Long.MAX_VALUE, HttpMethod.GET, HttpStatus.NOT_FOUND, DErrorResponse.class, jacksonMessageConverter, null);
        Assert.assertEquals("User with ID: "+Long.MAX_VALUE+" not found",result.getMessage());

    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    public void insert(){
        DUser dUser = userBuilder.newEntity();
        DUser result = (DUser) makeRequest(BASE_URL, HttpMethod.POST, HttpStatus.OK, DUser.class, jacksonMessageConverter, dUser);

        Assert.assertNotNull(result.getId());
        Assert.assertEquals(dUser.getName(),result.getName());
        Assert.assertEquals(dUser.getEmail(),result.getEmail());
        Assert.assertEquals(dUser.getBirthDate(),result.getBirthDate());
        Assert.assertEquals(dUser.getUsername(),result.getUsername());
        Assert.assertEquals(dUser.getPassword(),result.getPassword());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    public void insertInvalid(){
        DUser dUser = userBuilder.newEntity();
        dUser.setId(Long.MAX_VALUE);
        DErrorResponse result = (DErrorResponse) makeRequest(BASE_URL, HttpMethod.POST, HttpStatus.BAD_REQUEST, DErrorResponse.class, jacksonMessageConverter, dUser);
        Assert.assertEquals("Failed to insert object",result.getMessage());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    public void update(){
        DUser dUser = userBuilder.newAndSaveEntity();
        dUser.setName("Jhon Doe 2");
        DUser result = (DUser) makeRequest(BASE_URL, HttpMethod.PUT, HttpStatus.OK, DUser.class, jacksonMessageConverter, dUser);

        Assert.assertEquals(dUser.getId(), result.getId());
        Assert.assertEquals(dUser.getName(),result.getName());
        Assert.assertEquals(dUser.getEmail(),result.getEmail());
        Assert.assertEquals(dUser.getBirthDate(),result.getBirthDate());
        Assert.assertEquals(dUser.getUsername(),result.getUsername());
        Assert.assertEquals(dUser.getPassword(),result.getPassword());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    public void updateInvalid(){
        DUser dUser = userBuilder.newAndSaveEntity();
        dUser.setId(null);
        DErrorResponse result = (DErrorResponse) makeRequest(BASE_URL, HttpMethod.PUT, HttpStatus.BAD_REQUEST, DErrorResponse.class, jacksonMessageConverter, dUser);
        Assert.assertEquals("Failed to update object",result.getMessage());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    public void list() throws Exception {
        DUser dUser = userBuilder.newAndSaveEntity();

        RequestBuilder requestBuilder = getRequestMethod(HttpMethod.GET,BASE_URL,null);
        ResultMatcher resultMatcher = getResultMatcher(HttpStatus.OK);

        List<DUser> result = jacksonMessageConverter.getObjectMapper()
                                                    .readValue(getMockMvc()
                                                    .perform(requestBuilder)
                                                    .andExpect(resultMatcher).andReturn().getResponse().getContentAsString(),  jacksonMessageConverter.getObjectMapper().getTypeFactory().constructCollectionType(List.class, DUser.class));


        Assert.assertEquals(1,result.size());
        Assert.assertEquals(dUser.getId(), result.get(0).getId());
        Assert.assertEquals(dUser.getName(),result.get(0).getName());
        Assert.assertEquals(dUser.getEmail(),result.get(0).getEmail());
        Assert.assertEquals(dUser.getBirthDate(),result.get(0).getBirthDate());
        Assert.assertEquals(dUser.getUsername(),result.get(0).getUsername());
        Assert.assertEquals(dUser.getPassword(),result.get(0).getPassword());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    public void delete() {
        DUser dUser = userBuilder.newAndSaveEntity();
        makeRequest(BASE_URL+"/"+dUser.getId(),HttpMethod.DELETE,HttpStatus.OK, Void.class,jacksonMessageConverter,null);
        DErrorResponse result = (DErrorResponse) makeRequest(BASE_URL+"/"+dUser.getId(), HttpMethod.GET, HttpStatus.NOT_FOUND, DErrorResponse.class, jacksonMessageConverter, null);

        Assert.assertEquals("User with ID: "+dUser.getId()+" not found",result.getMessage());
    }
}
