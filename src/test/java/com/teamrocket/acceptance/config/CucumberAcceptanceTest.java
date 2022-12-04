//package com.teamrocket.acceptance.config;
//
//import com.teamrocket.service.AuthClient;
//import com.teamrocket.service.CourierService;
//import io.cucumber.junit.platform.engine.Constants;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.platform.suite.api.ConfigurationParameter;
//import org.junit.platform.suite.api.IncludeEngines;
//import org.junit.platform.suite.api.SelectClasspathResource;
//import org.junit.platform.suite.api.Suite;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentMatchers;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.test.context.TestPropertySource;
//
//import static org.mockito.Mockito.when;
//
//@Suite
//@IncludeEngines("cucumber")
//@SelectClasspathResource("features")
//@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty")
//@ConfigurationParameter(
//        key = Constants.GLUE_PROPERTY_NAME,
//        value = "com.teamrocket.acceptance," +
//                "com.teamrocket.acceptance.config"
//)
//@ConfigurationParameter(key = Constants.PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
//
//public final class CucumberAcceptanceTest {
//
//}
