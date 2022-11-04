package fr.abes.thesesapirecherche.controller;

import fr.abes.thesesapirecherche.ThesesApiRechercheApplicationTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PersonneControllerTest extends ThesesApiRechercheApplicationTests {

    @Test
    @DisplayName("Rechercher personne avec un mot - Mauvaise méthode")
    public void personnesMauvaiseMethode() throws Exception {
        mockMvc.perform(post("/api/v1/personne/rechercher"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Rechercher personne avec un mot - sans argument")
    public void personnesSansArguments() throws Exception {
        mockMvc.perform(get("/api/v1/personne/rechercher"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Rechercher personne avec un id - Mauvaise méthode")
    public void personneMauvaiseMethode() throws Exception {
        mockMvc.perform(post("/api/v1/personne/rechercher/098248782"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Rechercher personne avec un mot - sans argument")
    public void personneSansArguments() throws Exception {
        mockMvc.perform(get("/api/v1/personne/rechercher/"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").exists());
    }
}
