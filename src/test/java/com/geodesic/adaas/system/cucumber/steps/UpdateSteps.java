package com.geodesic.adaas.system.cucumber.steps;

import com.geodesic.adaas.system.cucumber.data.RequestData;
import com.geodesic.adaas.system.cucumber.data.RequestDelegate;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;

public class UpdateSteps {

  private final RequestData requestData;
  private final RequestDelegate requestDelegate;

  public UpdateSteps(RequestData requestData, RequestDelegate requestDelegate) {
    this.requestData = requestData;
    this.requestDelegate = requestDelegate;
  }

  @And("An update system request is created with")
  public void an_update_system_request_is_created_with(DataTable data) {

    Map<String, String> createValues = data.asMaps().get(0);

    String code = createValues.get("code");
    String name = createValues.get("name");
    String description = createValues.get("description");

    requestData.createUpdateRequest(code, name, description);
  }

  @And("The update system request is sent for code {string}")
  public void the_update_system_request_is_sent_for(String code) throws Exception {

    MockHttpServletResponse response =
        requestDelegate.putSystem(
            code, requestData.getCreateUpdateSystemRequest(), requestData.createJwt());
    requestData.setResponse(response);
  }

  @And("The update system request is sent for code {string} with no authorisation header")
  public void the_update_system_request_is_sent_for_code_with_no_authorisation_header(String code)
      throws Exception {

    MockHttpServletResponse response =
        requestDelegate.putSystemNoAuth(code, requestData.getCreateUpdateSystemRequest());
    requestData.setResponse(response);
  }
}
