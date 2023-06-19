package fr.abes.thesesapirecherche.personnes.controller.recherche;

import fr.abes.thesesapirecherche.EnableOnIntegrationTest;
import fr.abes.thesesapirecherche.personnes.controller.PersonneControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RechercheTroncatureTest extends PersonneControllerTest {

    /* ------------------- */
    /*  Expression exacte  */
    /* ------------------- */

    @Test
    @DisplayName("Rechercher des personnes avec l'expression exacte : nom prénom")
    @EnableOnIntegrationTest
    public void rechercherExpressionExacteNomPrenom() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=%22Erwan%20Rousseau%22&index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(9))
                .andExpect(jsonPath("$..theses[?(@.id=='2004BRES2040')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2021AIXM0253')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017GREAM026')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020AIXM0184')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='s347820')]").exists());
    }

    @Test
    @DisplayName("Rechercher des personnes avec l'expression exacte : prénom nom")
    @EnableOnIntegrationTest
    public void rechercherExpressionExactePrenomNom() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=%22Rousseau%20Erwan%22&index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(2))
                .andExpect(jsonPath("$..theses[?(@.id=='2004BRES2040')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2021AIXM0253')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017GREAM026')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020AIXM0184')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='s347820')]").exists());
    }


    /* ------------ */
    /*  Troncature  */
    /* ------------ */

    @Test
    @DisplayName("Rechercher des personnes avec la troncature arrière *")
    @EnableOnIntegrationTest
    public void rechercherTroncatureArriereWildchar() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=Rouss*&index=per_recherche_simple_rousseau&debut=0&nombre=1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(17))
                .andExpect(jsonPath("$..theses[?(@.id=='2004BRES2040')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2021AIXM0253')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017GREAM026')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020AIXM0184')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2007PA066375')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='128704802')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2011ANGE0040')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2003PA066582')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='1997PA040286')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020PA01H073')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2021SORUL154')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2019LYSE2095')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017PA01H038')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2019LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2018LYSE1193')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017ECLI0025')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2016SACLN057')]").exists());
    }

    @Test
    @DisplayName("Rechercher des personnes avec la troncature avant *")
    @EnableOnIntegrationTest
    public void rechercherTroncatureAvantWildchar() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=*wan&index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(12))
                .andExpect(jsonPath("$..theses[?(@.id=='2004BRES2040')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2021AIXM0253')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017GREAM026')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020AIXM0184')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020REN1B015')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='1999ROUES082')]").exists());

    }
}