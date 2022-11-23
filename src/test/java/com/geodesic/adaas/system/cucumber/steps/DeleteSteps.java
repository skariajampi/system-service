package com.geodesic.adaas.system.cucumber.steps;

import com.geodesic.adaas.system.cucumber.data.RequestData;
import com.geodesic.adaas.system.cucumber.data.RequestDelegate;
import io.cucumber.java.en.And;
import org.springframework.mock.web.MockHttpServletResponse;

public class DeleteSteps {

  private final RequestData requestData;
  private final RequestDelegate requestDelegate;

  public DeleteSteps(RequestData requestData, RequestDelegate requestDelegate) {
    this.requestData = requestData;
    this.requestDelegate = requestDelegate;
  }

  @And("The delete system request is sent for code {string}")
  public void the_delete_system_request_is_sent_for_code(String code) throws Exception {
    MockHttpServletResponse response = requestDelegate.deleteSystem(code, requestData.createJwt());
    requestData.setResponse(response);
  }

  @And("The delete system request is sent for code {string} with no authorisation header")
  public void the_delete_system_request_is_sent_for_code_with_no_authorisation_header(String code)
      throws Exception {
    MockHttpServletResponse response = requestDelegate.deleteSystemNoAuth(code);
    requestData.setResponse(response);
  }
}
