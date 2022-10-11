package com.geodesic.adaas.system.cucumber.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geodesic.adaas.system.dto.CreateUpdateSystemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Component
public class RequestDelegate {

  private static final String AUTHORIZATION = "Authorization";
  private static final String BEARER = "Bearer ";
  private static final String SYSTEMS = "/systems";
  private static final String SYSTEMS_WITH_CODE = "/systems/{code}";

  @Autowired private ObjectMapper objectMapper;

  @Autowired private MockMvc mockMvc;

  /** Request to post data to the service for system. */
  public MockHttpServletResponse postSystem(CreateUpdateSystemRequest request, String jwt)
      throws Exception {

    return mockMvc
        .perform(
            post(SYSTEMS)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header(AUTHORIZATION, BEARER + jwt))
        .andReturn()
        .getResponse();
  }

  public MockHttpServletResponse postSystemNoAuth(CreateUpdateSystemRequest request)
          throws Exception {

    return mockMvc
            .perform(
                    post(SYSTEMS)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andReturn()
            .getResponse();
  }

  public MockHttpServletResponse getSystem(String code, String jwt)
          throws Exception {

    return mockMvc
            .perform(get(SYSTEMS_WITH_CODE, code).header(AUTHORIZATION, BEARER + jwt))
            .andDo(print())
            .andReturn()
            .getResponse();
  }

  public MockHttpServletResponse getSystemNoAuth(String code)
          throws Exception {

    return mockMvc
            .perform(get(SYSTEMS_WITH_CODE, code))
            .andDo(print())
            .andReturn()
            .getResponse();
  }

  public MockHttpServletResponse getSystemNoJwt(String code) throws Exception {

    return mockMvc
        .perform(get(SYSTEMS_WITH_CODE, code))
        .andDo(print())
        .andReturn()
        .getResponse();
  }

  public MockHttpServletResponse deleteSystemNoAuth(String code)
      throws Exception {
    return mockMvc
        .perform(delete(SYSTEMS_WITH_CODE, code))
        .andReturn()
        .getResponse();
  }

  public MockHttpServletResponse deleteSystem(String code, String jwt)
          throws Exception {
    return mockMvc
            .perform(delete(SYSTEMS_WITH_CODE, code).header(AUTHORIZATION, BEARER + jwt))
            .andReturn()
            .getResponse();
  }


  public MockHttpServletResponse putSystem(
      String code, CreateUpdateSystemRequest request, String jwt) throws Exception {

    return mockMvc
        .perform(
            put(SYSTEMS_WITH_CODE, code)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header(AUTHORIZATION, BEARER + jwt))
        .andReturn()
        .getResponse();
  }

  public MockHttpServletResponse putSystemNoAuth(
          String code, CreateUpdateSystemRequest request) throws Exception {

    return mockMvc
            .perform(
                    put(SYSTEMS_WITH_CODE, code)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andReturn()
            .getResponse();
  }

  public MockHttpServletResponse getAllSystems(String jwt) throws Exception {
    return mockMvc
        .perform(get(SYSTEMS).header(AUTHORIZATION, BEARER + jwt))
        .andReturn()
        .getResponse();
  }

  public MockHttpServletResponse getAllSystemsNoAuth() throws Exception {
    return mockMvc.perform(get(SYSTEMS)).andReturn().getResponse();
  }
}
