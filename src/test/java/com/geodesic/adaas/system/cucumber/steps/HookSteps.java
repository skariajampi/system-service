package com.geodesic.adaas.system.cucumber.steps;

import com.geodesic.adaas.system.cucumber.data.DatabaseDelegate;
import com.geodesic.adaas.system.cucumber.data.RequestData;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.springframework.security.core.context.SecurityContextHolder;

public class HookSteps {

  private final RequestData requestData;
  private final DatabaseDelegate databaseDelegate;

  public HookSteps(RequestData requestData, DatabaseDelegate databaseDelegate) {
    this.requestData = requestData;
    this.databaseDelegate = databaseDelegate;
  }

  @Before
  public void before() {
    requestData.clear();
    databaseDelegate.clear();
  }

  @After
  public void after() {
    SecurityContextHolder.clearContext();
  }
}
