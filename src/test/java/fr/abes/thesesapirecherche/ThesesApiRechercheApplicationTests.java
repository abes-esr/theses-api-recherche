package fr.abes.thesesapirecherche;

import fr.abes.thesesapirecherche.security.SpringSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@AutoConfigureMockMvc
@SpringBootTest(classes = {ThesesApiRechercheApplication.class, HandlerMappingIntrospector.class, SpringSecurityConfig.class})
public class ThesesApiRechercheApplicationTests {

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

}
