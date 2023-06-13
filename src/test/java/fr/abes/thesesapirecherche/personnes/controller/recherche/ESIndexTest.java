package fr.abes.thesesapirecherche.personnes.controller.recherche;

import fr.abes.thesesapirecherche.EnableOnIntegrationTest;
import fr.abes.thesesapirecherche.personnes.controller.PersonneControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ESIndexTest extends PersonneControllerTest {

    @Test
    @DisplayName("Jeu de données 'per_recherche_simple_rousseau'")
    @EnableOnIntegrationTest
    public void per_recherche_simple_rousseau() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=*&index=per_recherche_simple_rousseau&debut=0&nombre=100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..theses[?(@.id=='2017PA01H038')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2021AIXM0253')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017ECLI0025')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2021SORUL154')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017GREAM026')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2003PA066582')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='1999ROUES082')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2011ANGE0040')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2018LYSE1193')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020AIXM0184')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2004BRES2040')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2007PA066375')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2019LYSE2095')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='1997PA040286')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020REN1B015')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2019LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020PA01H073')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2016SACLN057')]").exists());
    }

    @Test
    @DisplayName("Jeu de données 'per_affichage_theses_roles_etabs_disc'")
    @EnableOnIntegrationTest
    public void per_affichage_theses_roles_etabs_disc() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=*&index=per_affichage_theses_roles_etabs_disc&debut=0&nombre=1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..theses[?(@.id=='1995ISAL0076')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='1996ISAL0026')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='1997PA040286')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='1999ROUES082')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2000STET4011')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2003ISAL0049')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2003PA066582')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2004BRES2040')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2004INPG0060')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2004STET4010')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2004STET4012')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2007INPG0139')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2007PA066375')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2008ISAL0003')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2008STET4009')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2008STET4015')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2010STET4019')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2011ANGE0040')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2011PA040151')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2011STET4012')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2011STET4017')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2012AIXM3115')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2012GRENY105')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2012PA010644')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2012PA010707')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2012PA040212')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2012STET4009')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2012STET4019')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2013PA010597')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2013PA010652')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2013PA100201')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2013STET4002')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2013STET4011')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2014AIXM4745')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2014PA040127')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2014PA112271')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2014STET4002')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2014STET4006')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2014STET4010')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2014STET4015')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2015CAEN2008')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2015GREAT020')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2015PA010584')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2015STET4004')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2016LYSES015')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2016LYSES039')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2016LYSES056')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2016PA01H032')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2016PA040017')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2016PA040052')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2016PA040191')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2016SACLN057')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2016SACLS213')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017ECLI0025')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017GREAM026')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017LYSES012')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017LYSES022')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017PA01H038')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017SACLS479')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017SACLX027')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2018AIXM0140')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2018AIXM0198')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2018AIXM0224')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2018AIXM0659')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2018LYSE1193')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2018PA01H046')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2018PA01H049')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2019LYSE2095')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2019LYSES010')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2019LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2019PA01H036')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2019PA01H067')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2019PA01H090')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020AIXM0184')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020PA01H040')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020PA01H073')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020REN1B015')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2021AIXM0253')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2021PA080021')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2021SORUL154')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2022PA01H030')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2022PA01H081')]").exists());
    }

    @Test
    @DisplayName("Jeu de données 'per_recherche_simple_rousseau_ponderationv2'")
    @EnableOnIntegrationTest
    public void per_recherche_simple_rousseau_ponderationv2() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=*&index=per_recherche_simple_rousseau_ponderationv2&debut=0&nombre=100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..theses[?(@.id=='2017PA01H038')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017ECLI0025')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2021SORUL154')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017GREAM026')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2003PA066582')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='1999ROUES082')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2011ANGE0040')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2018LYSE1193')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020AIXM0184')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2004BRES2040')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2019LYSE2095')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020REN1B015')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2019LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020PA01H073')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2016SACLN057')]").exists());
    }

    @Test
    @DisplayName("Jeu de données 'per_recherche_simple_nom_prenom'")
    @EnableOnIntegrationTest
    public void per_recherche_simple_nom_prenom() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=*&index=per_recherche_simple_nom_prenom&debut=0&nombre=100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..theses[?(@.id=='2017PA01H038')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2021AIXM0253')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017ECLI0025')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2021SORUL154')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017GREAM026')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2003PA066582')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='1999ROUES082')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2011ANGE0040')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2018LYSE1193')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020AIXM0184')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2004BRES2040')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2007PA066375')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2019LYSE2095')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='1997PA040286')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020REN1B015')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2019LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020PA01H073')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2016SACLN057')]").exists());
    }

    @Test
    @DisplayName("Jeu de données 'per_recherche_sujet'")
    @EnableOnIntegrationTest
    public void per_recherche_sujet() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=*&index=per_recherche_sujet&debut=0&nombre=100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..theses[?(@.id=='2004BRES2040')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2014AIXM4745')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2016SACLS213')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017GREAM026')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2017SACLX027')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2018AIXM0198')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2018AIXM0224')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2018AIXM0659')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020AIXM0184')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2021AIXM0253')]").exists());
    }

    @Test
    @DisplayName("Jeu de données 'per_recherche_simple_autocompl_thematique'")
    @EnableOnIntegrationTest
    public void per_recherche_simple_autocompl_thematique() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=*&index=per_recherche_simple_autocompl_thematique&debut=0&nombre=100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..theses[?(@.id=='2000PA010697')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2001MNHN0022')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2003MON30025')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2003PA100181')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2011AIX10218')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2012PA010501')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2014TOU20035')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2014TOU20047')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2015TOU20116')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020PA100137')]").exists())
                .andExpect(jsonPath("$..theses[?(@.id=='2020TOU20084')]").exists());
    }
}
