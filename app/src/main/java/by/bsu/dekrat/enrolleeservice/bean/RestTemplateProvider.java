package by.bsu.dekrat.enrolleeservice.bean;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Created by USER on 01.12.2016.
 */

public class RestTemplateProvider {

    private static final RestTemplateProvider INSTANCE = new RestTemplateProvider();

    private RestTemplate commonTemplate;
    private RestTemplate hateoasTemplate;

    public static RestTemplateProvider getInstance() {
        return INSTANCE;
    }

    public RestTemplate getCommonTemplate() {
        return commonTemplate;
    }

    public RestTemplate getHateoasTemplate() {
        return hateoasTemplate;
    }

    private RestTemplateProvider() {
        commonTemplate = new RestTemplate();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
        converter.setObjectMapper(mapper);

        hateoasTemplate =
                new RestTemplate(Collections.<HttpMessageConverter<?>>singletonList(converter));
    }
}
