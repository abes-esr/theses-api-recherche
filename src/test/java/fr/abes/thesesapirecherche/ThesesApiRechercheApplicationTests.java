package fr.abes.thesesapirecherche;

import fr.abes.thesesapirecherche.security.SpringSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = {ThesesApiRechercheApplication.class, HandlerMappingIntrospector.class, SpringSecurityConfig.class}
// La suite suivante est à décommenter pour exécuter les tests d'intégrations
//        ,properties = { "spring.profiles.active=test-es" }
)
public class ThesesApiRechercheApplicationTests {

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("Route inexistante")
    public void doRouteInexistante() throws Exception {
        mockMvc.perform(post("/api"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").exists());
    }
}
