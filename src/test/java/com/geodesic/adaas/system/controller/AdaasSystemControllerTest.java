package com.geodesic.adaas.system.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geodesic.adaas.system.config.SecurityConfiguration;
import com.geodesic.adaas.system.dto.CreateUpdateSystemRequest;
import com.geodesic.adaas.system.dto.SystemResponse;
import com.geodesic.adaas.system.exception.RecordNotFoundException;
import com.geodesic.adaas.system.service.AdaasSystemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AdaasSystemController.class, GlobalExceptionHandler.class})
@Import(SecurityConfiguration.class)
class AdaasSystemControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private AdaasSystemService adaasSystemService;

  @Test
  @WithAwsCognitoUser(authority = "VISITOR")
  void getAdaasSystem_givenRecordExists_thenReturnData() throws Exception {

    // Given
    SystemResponse response =
        SystemResponse.builder().code("code").description("description").name("name").build();

    given(adaasSystemService.getAdaasSystem(any())).willReturn(response);

    // When
    mockMvc
        .perform(get("/systems/{code}", "AFN"))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // Then
    verify(adaasSystemService).getAdaasSystem("AFN");
  }

  @Test
  @WithAwsCognitoUser(authority = "VISITOR")
  void getAdaasSystem_givenRecordDoesNotExist_thenReturnRecordNotFound() throws Exception {

    // Given
    given(adaasSystemService.getAdaasSystem(any())).willThrow(new RecordNotFoundException(""));

    // When
    mockMvc.perform(get("/systems/{code}", "123")).andExpect(status().isNotFound());

    // Then
    verify(adaasSystemService).getAdaasSystem("123");
  }

  @Test
  @WithAwsCognitoUser(authority = "VISITOR")
  void getAdaasSystems_givenRecordsExist_thenReturnList() throws Exception {

    // Given
    List<SystemResponse> adaasSystemList = new ArrayList<>();
    adaasSystemList.add(SystemResponse.builder().code("code1").build());
    adaasSystemList.add(SystemResponse.builder().code("code2").build());

    given(adaasSystemService.getAdaasSystems()).willReturn(adaasSystemList);

    // When
    MvcResult result = mockMvc.perform(get("/systems")).andExpect(status().isOk()).andReturn();

    // Then
    String response = result.getResponse().getContentAsString();
    List<SystemResponse> systems = objectMapper.readValue(response, new TypeReference<>() {});
    assertThat(systems)
        .hasSameSizeAs(adaasSystemList)
        .extracting("code")
        .contains("code1", "code2");
    verify(adaasSystemService).getAdaasSystems();
  }

  @Test
  @WithAwsCognitoUser(authority = "VISITOR")
  void saveAdaasSystem_givenSaveSuccessful_thenCreatedRecordReturned() throws Exception {

    // Given
    String code = "AFN";
    CreateUpdateSystemRequest createUpdateSystemRequest =
        CreateUpdateSystemRequest.builder().code(code).description("desc").name("name").build();
    SystemResponse response = SystemResponse.builder().code(code).build();
    given(adaasSystemService.save(any())).willReturn(response);

    // When
    mockMvc
        .perform(
            post("/systems")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUpdateSystemRequest)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // Then
    verify(adaasSystemService).save(any(CreateUpdateSystemRequest.class));
  }

  @Test
  @WithAwsCognitoUser(authority = "VISITOR")
  void updateAdaasSystem_givenUpdateSuccessful_thenUpdatedRecordReturned() throws Exception {

    // Given
    String code = "AFN";
    CreateUpdateSystemRequest createUpdateSystemRequest =
        CreateUpdateSystemRequest.builder().code(code).description("desc").name("name").build();
    SystemResponse response = SystemResponse.builder().code(code).build();
    given(adaasSystemService.update(any(), any())).willReturn(response);

    // When
    mockMvc
        .perform(
            put("/systems/{code}", "AFN")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUpdateSystemRequest)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // Then
    verify(adaasSystemService).update(eq("AFN"), any(CreateUpdateSystemRequest.class));
  }

  @Test
  @WithAwsCognitoUser(authority = "VISITOR")
  void updateAdaasSystem_givenRecordDoesNotExist_thenUpdateFailsAndReturnsNotFoundException()
      throws Exception {

    // Given
    String code = "AFN";
    CreateUpdateSystemRequest createUpdateSystemRequest =
        CreateUpdateSystemRequest.builder().code(code).description("desc").name("name").build();
    given(adaasSystemService.update(any(), any())).willThrow(RecordNotFoundException.class);

    // When
    mockMvc
        .perform(
            put("/systems/{code}", "AFN")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUpdateSystemRequest)))
        .andDo(print())
        .andExpect(status().isNotFound());

    // Then
    verify(adaasSystemService).update(eq("AFN"), any(CreateUpdateSystemRequest.class));
  }

  @Test
  @WithAwsCognitoUser(authority = "MyGreeterLambda123")
  void getAdaasSystem_unauthenticatedUser() throws Exception {

    // Given
    SystemResponse response =
        SystemResponse.builder().code("code").description("description").name("name").build();

    given(adaasSystemService.getAdaasSystem(any())).willReturn(response);

    // When
    mockMvc.perform(get("/systems/{code}", "AFN")).andExpect(status().isForbidden());

    // Then
    verifyNoInteractions(adaasSystemService);
  }

  @Test
  @WithAwsCognitoUser(authority = "VISITOR")
  void deleteAdaasSystem_givenDeeleteSuccessful_thenNoExceptionReturned() throws Exception {

    // Given
    String code = "AFN";

    willDoNothing().given(adaasSystemService).deleteByCode(code);

    // When
    mockMvc
        .perform(delete("/systems/{code}", "AFN").contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNoContent());

    // Then
    verify(adaasSystemService).deleteByCode(eq("AFN"));
  }

  @Test
  @WithAwsCognitoUser(authority = "VISITOR")
  void deleteAdaasSystem_givenRecordDoesNotExist_thenDeleteFailsAndReturnsNotFoundException()
      throws Exception {

    // Given
    String code = "AFN";

    willThrow(RecordNotFoundException.class).given(adaasSystemService).deleteByCode(code);

    // When
    mockMvc
        .perform(delete("/systems/{code}", "AFN").contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());

    // Then
    verify(adaasSystemService).deleteByCode(eq("AFN"));
  }

  @Test
  @WithAwsCognitoUser(authority = "MyGreeterLambda123")
  void deleteAdaasSystem_unauthenticatedUser() throws Exception {

    // Given
    String code = "AFN";

    willDoNothing().given(adaasSystemService).deleteByCode(code);

    // When
    mockMvc
        .perform(delete("/systems/{code}", "AFN").contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    // Then
    verifyNoInteractions(adaasSystemService);
  }
}
