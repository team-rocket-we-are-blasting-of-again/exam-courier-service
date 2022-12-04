package com.teamrocket.acceptance.config;

import com.teamrocket.repository.CourierRepository;
import com.teamrocket.service.AuthClient;
import io.cucumber.java.AfterAll;
import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.reset;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty")
@ConfigurationParameter(
        key = Constants.GLUE_PROPERTY_NAME,
        value = "com.teamrocket.acceptance," +
                "com.teamrocket.acceptance.config"
)
@ConfigurationParameter(key = Constants.PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")

public final class CucumberAcceptanceTest {
    @MockBean
    private AuthClient authClient;

    @Autowired
    CourierRepository courierRepository;
    @AfterAll
    void cleanUp() {
        reset(authClient);
        courierRepository.deleteAll();
    }
}
