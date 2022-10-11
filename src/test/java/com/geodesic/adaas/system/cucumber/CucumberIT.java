package com.geodesic.adaas.system.cucumber;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Cucumber integration tests. This is simply the entry point for all the Cucumber tests and should
 * remain empty. The Cucumber glue classes should be automatically detected.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("cucumber")
public class CucumberIT {}
