package com.geodesic.adaas.system.cucumber.data;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geodesic.adaas.system.dto.CreateUpdateSystemRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class RequestData {

  @Autowired private ObjectMapper objectMapper;

  @Autowired private KeyPair jwtKeyPair;

  @Getter @Setter private List<String> roles;

  @Getter private CreateUpdateSystemRequest createUpdateSystemRequest;

  private MockHttpServletResponse mockHttpServletResponse;

  public void clear() {
    mockHttpServletResponse = null;
    roles = null;
    createUpdateSystemRequest = null;
  }

  public void createUpdateRequest(final String code, final String name, final String description) {

    this.createUpdateSystemRequest =
        CreateUpdateSystemRequest.builder().code(code).name(name).description(description).build();
  }

  public String createJwt() {
    return JWT.create()
        .withClaim("realm_access", Map.of("roles", roles))
        .withIssuer("CucumberTests")
        .withIssuedAt(new Date())
        .sign(
            Algorithm.RSA256(
                (RSAPublicKey) jwtKeyPair.getPublic(), (RSAPrivateKey) jwtKeyPair.getPrivate()));
  }

  public void setResponse(MockHttpServletResponse mockHttpServletResponse) {
    this.mockHttpServletResponse = mockHttpServletResponse;
  }

  public <T> T getResponseBody(Class<T> type)
      throws JsonProcessingException, UnsupportedEncodingException {
    return objectMapper.readValue(mockHttpServletResponse.getContentAsString(), type);
  }

  public String getResponseBodyAsString() throws UnsupportedEncodingException {
    return mockHttpServletResponse.getContentAsString();
  }

  public int getResponseStatus() {
    return mockHttpServletResponse.getStatus();
  }

  public <T> T getResponseBody(TypeReference<T> type)
      throws JsonProcessingException, UnsupportedEncodingException {
    return objectMapper.readValue(mockHttpServletResponse.getContentAsString(), type);
  }
}
