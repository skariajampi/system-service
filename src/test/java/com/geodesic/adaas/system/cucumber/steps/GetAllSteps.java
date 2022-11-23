package com.geodesic.adaas.system.cucumber.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.geodesic.adaas.system.cucumber.data.RequestData;
import com.geodesic.adaas.system.cucumber.data.RequestDelegate;
import com.geodesic.adaas.system.dto.SystemResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import org.assertj.core.groups.Tuple;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class GetAllSteps {

  private final RequestData requestData;
  private final RequestDelegate requestDelegate;

  public GetAllSteps(RequestData requestData, RequestDelegate requestDelegate) {
    this.requestData = requestData;
    this.requestDelegate = requestDelegate;
  }

  @And("The getAll systems request is sent")
  public void the_getAll_systems_request_is_sent() throws Exception {

    MockHttpServletResponse response = requestDelegate.getAllSystems(requestData.createJwt());
    requestData.setResponse(response);
  }

  @And("The getAll systems response contains")
  public void the_getAll_systems_response_contains(DataTable data)
      throws UnsupportedEncodingException, JsonProcessingException {

    List<Map<String, String>> systemFields = data.asMaps();
    assertThat(systemFields).hasSize(2);

    TypeReference<List<SystemResponse>> typeRef = new TypeReference<List<SystemResponse>>() {};
    List<SystemResponse> response = requestData.getResponseBody(typeRef);

    assertThat(response).hasSize(2);

    Tuple[] tuplesArr =
        systemFields.stream()
            .map(row -> tuple(row.get("code"), row.get("name")))
            .toArray(Tuple[]::new);

    assertThat(response)
        .hasSize(2)
        .extracting(SystemResponse::getCode, SystemResponse::getName)
        .containsExactlyInAnyOrder(tuplesArr);
  }

  @And("The getAll systems request is sent with no authorisation header")
  public void the_getAll_systems_request_is_sent_with_no_authorisation_header() throws Exception {

    MockHttpServletResponse response = requestDelegate.getAllSystemsNoAuth();
    requestData.setResponse(response);
  }

  @And("The getAll systems request is sent with an empty jwt")
  public void the_getAll_systems_request_is_sent_with_an_empty_jwt() throws Exception {

    MockHttpServletResponse response = requestDelegate.getAllSystems("");
    requestData.setResponse(response);
  }
}
