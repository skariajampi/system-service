package com.geodesic.adaas.system.cucumber.steps;

import com.geodesic.adaas.system.cucumber.data.RequestData;
import com.geodesic.adaas.system.cucumber.data.RequestDelegate;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;

public class CreateSteps {

    private final RequestData requestData;
    private final RequestDelegate requestDelegate;

    public CreateSteps(RequestData requestData, RequestDelegate requestDelegate) {
        this.requestData = requestData;
        this.requestDelegate = requestDelegate;
    }

    @And("A create system request is created with")
    public void a_create_system_request_is_created_with(DataTable data) {

        Map<String, String> createValues = data.asMaps().get(0);

        String code = createValues.get("code");
        String name = createValues.get("name");
        String description = createValues.get("description");

        requestData.createUpdateRequest(code, name, description);
    }

    @And("The create system request is sent")
    public void the_create_system_request_is_sent() throws Exception {

        MockHttpServletResponse response = requestDelegate.
                postSystem(requestData.getCreateUpdateSystemRequest(),
                        requestData.createJwt());
        requestData.setResponse(response);
    }

    @And("The create system request is sent with no authorisation header")
    public void the_create_system_request_is_sent_with_no_authorisation_header() throws Exception {

        MockHttpServletResponse response = requestDelegate.
                postSystemNoAuth(requestData.getCreateUpdateSystemRequest());
        requestData.setResponse(response);
    }

}
