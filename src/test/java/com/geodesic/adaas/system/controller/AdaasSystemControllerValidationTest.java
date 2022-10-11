package com.geodesic.adaas.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geodesic.adaas.system.dto.CreateUpdateSystemRequest;
import com.geodesic.adaas.system.service.AdaasSystemService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AdaasSystemController.class, GlobalExceptionHandler.class})
class AdaasSystemControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdaasSystemService adaasSystemService;

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", " \t\n"})
    @WithAwsCognitoUser(authority = "MyGreeterLambda")
    void saveAdaasSystem_givenCodeIsBlank_thenBadRequest(String code) throws Exception {

        // Given
        CreateUpdateSystemRequest request = CreateUpdateSystemRequest
                .builder()
                .code(code)
                .name("name")
                .description("description").build();

        // When
        mockMvc.perform(
                post("/systems")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // Then
        verifyNoInteractions(adaasSystemService);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", " \t\n"})
    @WithAwsCognitoUser(authority = "MyGreeterLambda")
    void saveAdaasSystem_givenNameIsBlank_thenBadRequest(String name) throws Exception {

        // Given
        CreateUpdateSystemRequest request = CreateUpdateSystemRequest
                .builder()
                .code("code")
                .name(name)
                .description("description").build();

        // When
        mockMvc.perform(
                post("/systems")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // Then
        verifyNoInteractions(adaasSystemService);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", " \t\n"})
    @WithAwsCognitoUser(authority = "MyGreeterLambda")
    void saveAdaasSystem_givenDescriptionIsBlank_thenBadRequest(String description) throws Exception {

        // Given
        CreateUpdateSystemRequest request = CreateUpdateSystemRequest
                .builder()
                .code("code")
                .name("name")
                .description(description).build();

        // When
        mockMvc.perform(
                post("/systems")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // Then
        verifyNoInteractions(adaasSystemService);
    }

}