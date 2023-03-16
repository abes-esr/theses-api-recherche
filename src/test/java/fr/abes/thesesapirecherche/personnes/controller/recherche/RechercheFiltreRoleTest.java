package fr.abes.thesesapirecherche.personnes.controller.recherche;

import fr.abes.thesesapirecherche.EnableOnIntegrationTest;
import fr.abes.thesesapirecherche.personnes.controller.PersonneControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RechercheFiltreRoleTest extends PersonneControllerTest {

    /* ---- */
    /*  Ou  */
    /* ---- */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Auteur")
    @EnableOnIntegrationTest
    public void rechercherRoleAuteur() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_recherche_simple_rousseau&filtres=[role=\"auteur\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[?(@.theses[?(@.nnt=='2004BRES2040')])]").exists())
                .andExpect(jsonPath("$[?(@.theses[?(@.nnt=='2007PA066375')])]").exists())
                .andExpect(jsonPath("$[?(@.theses[?(@.nnt=='2011ANGE0040')])]").exists())
                .andExpect(jsonPath("$[?(@.theses[?(@.nnt=='2003PA066582')])]").exists())
                .andExpect(jsonPath("$[?(@.theses[?(@.nnt=='1997PA040286')])]").exists());

    }

}
