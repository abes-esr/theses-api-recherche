package fr.abes.thesesapirecherche.personnes.controller;

import fr.abes.thesesapirecherche.EnableOnIntegrationTest;
import fr.abes.thesesapirecherche.ThesesApiRechercheApplicationTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PersonneControllerTest extends ThesesApiRechercheApplicationTests {

    /* ----------- */
    /*  Rechercher */
    /* ----------- */

    @Test
    @DisplayName("Rechercher personne avec un mot - Mauvaise méthode")
    public void rechercherMauvaiseMethode() throws Exception {
        mockMvc.perform(post("/api/v1/personnes/recherche"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Rechercher personne avec un mot - sans argument")
    public void rechercherSansArguments() throws Exception {
        mockMvc.perform(get("/api/v1/personnes/recherche"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Rechercher personne avec le mot Rousseau")
    @EnableOnIntegrationTest
    public void rechercherRousseau() throws Exception {
        mockMvc.perform(get("/api/v1/personnes/recherche/?q=Rousseau"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8)))
                .andExpect(jsonPath("$[?(@.id)]").exists())
                .andExpect(jsonPath("$[?(@.nom)]").exists())
                .andExpect(jsonPath("$[?(@.prenom)]").exists())
                .andExpect(jsonPath("$[?(@.has_idref)]").exists());
    }

    @Test
    @DisplayName("Rechercher personne avec le booléen SAUF : NOT")
    @EnableOnIntegrationTest
    public void rechercherBooleansSaufNot() throws Exception {
        mockMvc.perform(get("/api/v1/personnes/recherche/?q=Erwan%20NOT%20Rousseaux"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[?(@.id)]").exists())
                .andExpect(jsonPath("$[?(@.nom)]").exists())
                .andExpect(jsonPath("$[?(@.prenom)]").exists())
                .andExpect(jsonPath("$[?(@.has_idref)]").exists());
    }

    @Test
    @DisplayName("Rechercher personne avec le booléen SAUF : -")
    @EnableOnIntegrationTest
    public void rechercherBooleansSaufMinus() throws Exception {
        mockMvc.perform(get("/api/v1/personnes/recherche/?q=Erwan%20-Rousseaux"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[?(@.id)]").exists())
                .andExpect(jsonPath("$[?(@.nom)]").exists())
                .andExpect(jsonPath("$[?(@.prenom)]").exists())
                .andExpect(jsonPath("$[?(@.has_idref)]").exists());
    }

    /* ----------- */
    /*  Completion */
    /* ----------- */

    @Test
    @DisplayName("Suggestion de personnes avec un mot - Mauvaise méthode")
    public void completionMauvaiseMethode() throws Exception {
        mockMvc.perform(post("/api/v1/personnes/completion"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Suggestion de personnes avec un mot - sans argument")
    public void completionSansArguments() throws Exception {
        mockMvc.perform(get("/api/v1/personnes/completion"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Suggestion de personnes avec le mot D")
    @EnableOnIntegrationTest
    public void completion() throws Exception {
        mockMvc.perform(get("/api/v1/personnes/completion?q=d"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[?(@.id)]").exists())
                .andExpect(jsonPath("$[?(@.suggestion)]").exists());
    }

    /* ---------------------- */
    /*  Rechercher avec un ID */
    /* ---------------------- */

    @Test
    @DisplayName("Rechercher personne avec un id - Mauvaise méthode")
    public void rechercherAvecIdMauvaiseMethode() throws Exception {
        mockMvc.perform(post("/api/v1/personnes/personne/098248782"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Rechercher personne avec son identifiant")
    @EnableOnIntegrationTest
    public void rechercherAvecId() throws Exception {
        mockMvc.perform(get("/api/v1/personnes/personne/098248782/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("098248782"))
                .andExpect(jsonPath("$.nom").value("Rousseau"))
                .andExpect(jsonPath("$.prenom").value("Erwan"))
                .andExpect(jsonPath("$.has_idref").value(true));
    }
}
