package com.geodesic.adaas.system.cucumber.steps;

import com.geodesic.adaas.system.cucumber.data.RequestData;
import com.geodesic.adaas.system.cucumber.data.RequestDelegate;
import io.cucumber.java.en.And;
import org.springframework.mock.web.MockHttpServletResponse;

public class ManagementSteps {

  private final RequestData requestData;
  private final RequestDelegate requestDelegate;

  public ManagementSteps(RequestData requestData, RequestDelegate requestDelegate) {
    this.requestData = requestData;
    this.requestDelegate = requestDelegate;
  }

  @And("A request is sent for endpoint {string}")
  public void a_request_is_sent_for_the_endpoint(String endpoint) throws Exception {

    MockHttpServletResponse response = requestDelegate.getManagement(endpoint);
    requestData.setResponse(response);
  }
}
