package com.geodesic.adaas.system.cucumber.steps;

import io.cucumber.java.DataTableType;

public class DataTypeDefinitions {

  @DataTableType(replaceWithEmptyString = "[blank]")
  public String stringType(String cell) {
    return cell;
  }
}
