package com.teamrocket.acceptance.config;

import com.teamrocket.repository.CourierRepository;
import com.teamrocket.repository.DeliveryRepository;
import io.cucumber.junit.platform.engine.Constants;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

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
@DirtiesContext()
public final class CucumberAcceptanceTest {
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private CourierRepository courierRepository;

    @AfterEach
    void cleanUp() {
        System.out.println("MAGDAAALENA");
        courierRepository.deleteAll();
        deliveryRepository.deleteAll();
    }
}
