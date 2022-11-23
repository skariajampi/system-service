package com.geodesic.adaas.system.cucumber.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.geodesic.adaas.system.cucumber.data.DatabaseDelegate;
import com.geodesic.adaas.system.cucumber.data.RequestData;
import com.geodesic.adaas.system.dto.SystemResponse;
import com.geodesic.adaas.system.entity.SystemEntity;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonSteps {

  private final RequestData requestData;
  private final DatabaseDelegate databaseDelegate;

  public CommonSteps(RequestData requestData, DatabaseDelegate databaseDelegate) {
    this.requestData = requestData;
    this.databaseDelegate = databaseDelegate;
  }

  @And("The response has an http status {int}")
  public void the_response_has_an_http_status(int status) {
    assertThat(requestData.getResponseStatus()).isEqualTo(status);
  }

  @And("The client is authenticated with roles {string}")
  public void the_client_is_authenticated_with_roles(String roles) {
    requestData.setRoles(Arrays.asList(roles.split(",")));
  }

  @And("System database records exist")
  public void database_system_records_exist(DataTable data) {
    List<Map<String, String>> systemFields = data.asMaps();

    systemFields.forEach(this::createSystemEntity);
  }

  @And("The response contains system data")
  public void the_response_contains_system_data(DataTable data)
      throws UnsupportedEncodingException, JsonProcessingException {

    List<Map<String, String>> systemFields = data.asMaps();
    assertThat(systemFields).hasSize(1);

    Map<String, String> expectedResponseData = systemFields.get(0);

    SystemResponse response = requestData.getResponseBody(SystemResponse.class);
    assertThat(expectedResponseData)
        .containsEntry("code", response.getCode())
        .containsEntry("name", response.getName())
        .containsEntry("description", response.getDescription());

    if (expectedResponseData.containsKey("id")) {
      assertThat(expectedResponseData).containsEntry("id", response.getId().toString());
    }
  }

  @And("The database only contains records")
  public void the_database_only_contains_records(DataTable dataTable) {
    List<SystemEntity> systemEntities = databaseDelegate.getAllSystems();

    // Loop through all the expected records, and remove these from the list of database records. If
    // any are left, then there were more database records than expected; if we couldn't find an
    // entry, then the expected record did not exist in the DB.
    for (Map<String, String> expectedSystem : dataTable.asMaps()) {
      Optional<SystemEntity> optionalSystemEntity =
          systemEntities.stream().filter(hasSystemFields(expectedSystem)).findFirst();

      assertThat(optionalSystemEntity).isPresent();
      systemEntities.remove(optionalSystemEntity.get());
    }

    assertThat(systemEntities).isEmpty();
  }

  private Predicate<SystemEntity> hasSystemFields(Map<String, String> expectedSystem) {
    return systemEntity ->
        compareIfPresent(expectedSystem.get("id"), systemEntity.getId().toString())
            && compareIfPresent(expectedSystem.get("code"), systemEntity.getCode())
            && compareIfPresent(expectedSystem.get("name"), systemEntity.getName())
            && compareIfPresent(expectedSystem.get("description"), systemEntity.getDescription());
  }

  // If a column in the Cucumber steps is omitted, we don't want to compare that
  private boolean compareIfPresent(String expected, String actual) {
    if (expected == null) {
      return true;
    }

    return expected.equals(actual);
  }

  private SystemEntity createSystemEntity(Map<String, String> systemFields) {
    return databaseDelegate.createSystem(
        systemFields.get("code"), systemFields.get("name"), systemFields.get("description"));
  }
}
