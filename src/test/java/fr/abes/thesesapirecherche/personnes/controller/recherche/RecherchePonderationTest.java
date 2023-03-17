package fr.abes.thesesapirecherche.personnes.controller.recherche;

import fr.abes.thesesapirecherche.EnableOnIntegrationTest;
import fr.abes.thesesapirecherche.personnes.controller.PersonneControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecherchePonderationTest extends PersonneControllerTest {

    /* ------------------------------- */
    /*  Boost Idref, Roles : Directeur de thèse
        et Rapporteurs  */
    /* ------------------------------ */

    @Test
    @DisplayName("Rechercher des personnes au nom de Rousseau avec la pondération sur l'identifiant IdRef et le role")
    @EnableOnIntegrationTest
    public void rechercherRousseauBoostIdrefRole() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8)))
                .andExpect(jsonPath("$[0].id").value("098248782"))
                .andExpect(jsonPath("$[1].id").value("050646176"))
                .andExpect(jsonPath("$[2].id").value("081954522"))
                .andExpect(jsonPath("$[3].id").value("127566635"))
                .andExpect(jsonPath("$[4].id").value("081810652"))
                .andExpect(jsonPath("$[5].id").value("128704802"))
                .andExpect(jsonPath("$[6].has_idref").value(false))
                .andExpect(jsonPath("$[6].nom").value("Rousseau"))
                .andExpect(jsonPath("$[6].prenom").value("Erwan"))
                .andExpect(jsonPath("$[7].has_idref").value(false))
                .andExpect(jsonPath("$[7].nom").value("Rousseau"))
                .andExpect(jsonPath("$[7].prenom").value("Pascal"));
    }

    @Test
    @DisplayName("Rechercher des personnes au prénom de Erwan avec la pondération sur l'identifiant IdRef et le role")
    @EnableOnIntegrationTest
    public void rechercherErwanBoostIdrefRole() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=erwan&index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id").value("098248782"))
                .andExpect(jsonPath("$[1].id").value("182866122"))
                .andExpect(jsonPath("$[2].id").value("178429708"))
                .andExpect(jsonPath("$[3].id").value("127566635"))
                .andExpect(jsonPath("$[4].has_idref").value(false))
                .andExpect(jsonPath("$[4].nom").value("Rousseau"))
                .andExpect(jsonPath("$[4].prenom").value("Erwan"));
    }
}
