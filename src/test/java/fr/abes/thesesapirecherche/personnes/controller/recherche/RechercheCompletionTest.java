package fr.abes.thesesapirecherche.personnes.controller.recherche;

import fr.abes.thesesapirecherche.EnableOnIntegrationTest;
import fr.abes.thesesapirecherche.personnes.controller.PersonneControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RechercheCompletionTest extends PersonneControllerTest {

    @Test
    @DisplayName("Suggestion de personnes avec un mot - Mauvaise méthode")
    public void completionMauvaiseMethode() throws Exception {
        mockMvc.perform(post("/api/v1/tests/personnes/completion"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Suggestion de personnes avec un mot - sans argument")
    public void completionSansArguments() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/completion"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Suggestion de personnes avec le mot D")
    @EnableOnIntegrationTest
    public void completion() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/completion?q=d&index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personnes", hasSize(10)))
                .andExpect(jsonPath("$.personnes[?(@.id)]").exists())
                .andExpect(jsonPath("$.personnes[?(@.suggestion)]").exists());
    }

    /* ------------------------------- */
    /*  Boost Idref, Roles : Directeur de thèse
        et Rapporteurs  */
    /* ------------------------------ */

    @Test
    @DisplayName("Autocomplétion des personnes au nom de Rousseau avec la pondération sur l'identifiant IdRef et le role")
    @EnableOnIntegrationTest
    public void completionRousseauBoostIdrefRole() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/completion/?q=rousseau&index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personnes", hasSize(4)))
                .andExpect(jsonPath("$.personnes[0].id").value("098248782"))
                .andExpect(jsonPath("$.personnes[1].id").value("127566635"))
                .andExpect(jsonPath("$.personnes[2].id").value("081954522"))
                .andExpect(jsonPath("$.personnes[3].id").value("081810652"));
    }

    @Test
    @DisplayName("Autocomplétion des personnes au prénom de Erwan avec la pondération sur l'identifiant IdRef et le role")
    @EnableOnIntegrationTest
    public void completionErwanBoostIdrefRole() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/completion/?q=erwan&index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personnes", hasSize(4)))
                .andExpect(jsonPath("$.personnes[0].id").value("182866122"))
                .andExpect(jsonPath("$.personnes[1].id").value("178429708"))
                .andExpect(jsonPath("$.personnes[2].id").value("098248782"))
                .andExpect(jsonPath("$.personnes[3].id").value("127566635"));
    }

    @Test
    @DisplayName("Autocomplétion des thématiques 'techn'")
    @EnableOnIntegrationTest
    public void completionThematiqueTechn() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/completion/?q=techn&index=per_recherche_simple_autocompl_thematique"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.thematiques", hasSize(3)))
                .andExpect(jsonPath("$.thematiques[0].suggestion").value("Technics evolution"))
                .andExpect(jsonPath("$.thematiques[1].suggestion").value("Techno-Cultural perspective"))
                .andExpect(jsonPath("$.thematiques[2].suggestion").value("Technologie lithique"));
    }

    @Test
    @DisplayName("Autocomplétion des thématiques 'hach'")
    @EnableOnIntegrationTest
    public void completionThematiqueHach() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/completion/?q=hach&index=per_recherche_simple_autocompl_thematique"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.thematiques", hasSize(1)))
                .andExpect(jsonPath("$.thematiques[0].suggestion").value("Hachereaux"));
    }
}
