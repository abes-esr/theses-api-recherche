package fr.abes.thesesapirecherche.personnes.controller;

import fr.abes.thesesapirecherche.EnableOnIntegrationTest;
import fr.abes.thesesapirecherche.ThesesApiRechercheApplicationTests;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PersonneControllerTest extends ThesesApiRechercheApplicationTests {

    /* --------------------- */
    /*  Rechercher test API */
    /* -------------------- */

    @Test
    @DisplayName("Rechercher personne avec un mot - Mauvaise méthode")
    public void rechercherMauvaiseMethode() throws Exception {
        mockMvc.perform(post("/api/v1/tests/personnes/recherche"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Rechercher personne avec un mot - sans argument")
    public void rechercherSansArguments() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").exists());
    }

    /* ---------------------------- */
    /*  Rechercher avec nom, prénom */
    /* ---------------------------- */

    @Test
    @DisplayName("Rechercher personne avec le mot Rousseau")
    @EnableOnIntegrationTest
    public void rechercherRousseau() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=Rousseau&index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(17))
                .andExpect(jsonPath("$.personnes[?(@.id)]").exists())
                .andExpect(jsonPath("$.personnes[?(@.nom)]").exists())
                .andExpect(jsonPath("$.personnes[?(@.prenom)]").exists())
                .andExpect(jsonPath("$.personnes[?(@.has_idref)]").exists());
    }

    /* ------------------------------- */
    /*  Rechercher avec une thématique */
    /* ------------------------------- */

    @Test
    @DisplayName("Rechercher personne avec la thématique 'hyperbolicité'")
    @EnableOnIntegrationTest
    public void rechercherThematiqueHyperbolicité() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=hyperbolicité&index=per_recherche_sujet&debut=0&nombre=100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personnes[?(@.id=='221478388')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='07069348X')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='156182831')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='082989117')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='234956372')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='031753973')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='120567822')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='184714346')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='028750691')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='223395471')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='098307991')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='133999793')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='262100606')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='096319097')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='112080553')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='098313940')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='07099563X')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='152938095')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='118017853')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='204758394')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='137728395')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='098248782')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='031832113')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='050264753')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLX027')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLX027')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004BRES2040')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLX027')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLX027')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLX027')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLX027')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004BRES2040')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLX027')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists());
    }

    /* ---------------------- */
    /*  Rechercher avec un ID */
    /* ---------------------- */

    @Test
    @DisplayName("Rechercher personne avec un id - Mauvaise méthode")
    public void rechercherAvecIdMauvaiseMethode() throws Exception {
        mockMvc.perform(post("/api/v1/tests/personnes/personne/098248782?index=per_recherche_simple_rousseau"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Rechercher personne avec son identifiant")
    @EnableOnIntegrationTest
    public void rechercherAvecId() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/personne/050646176/?index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("050646176"))
                .andExpect(jsonPath("$.nom").value("Rousseau"))
                .andExpect(jsonPath("$.prenom").value("Pascal"))
                .andExpect(jsonPath("$.has_idref").value(true))
                /***** Thèse n°1997PA040286 *****/
                .andExpect(jsonPath("$.theses['auteur'][?(@.nnt=='1997PA040286')]").exists())
                .andExpect(jsonPath("$.theses['auteur'][?(@.nnt=='1997PA040286')].role").value("auteur"))
                .andExpect(jsonPath("$.theses['auteur'][?(@.nnt=='1997PA040286')].status").value("soutenue"))
                .andExpect(jsonPath("$.theses['auteur'][?(@.nnt=='1997PA040286')].titre").value("\"Réalité, peinture pure\" : l'oeuvre de Robert Delaunay jusqu'en 1914"))
                .andExpect(jsonPath("$.theses['auteur'][?(@.nnt=='1997PA040286')].etablissement_soutenance.nom").value("Paris 4"))
                .andExpect(jsonPath("$.theses['auteur'][?(@.nnt=='1997PA040286')].discipline").value("Art et archéologie"))
                .andExpect(jsonPath("$.theses['auteur'][?(@.nnt=='1997PA040286')].date_soutenance").value("1998-01-01"))
                .andExpect(jsonPath("$.theses['auteur'][?(@.nnt=='1997PA040286')].auteurs[?(@.id=='050646176')]").exists())
                .andExpect(jsonPath("$.theses['auteur'][?(@.nnt=='1997PA040286')].auteurs[?(@.id=='050646176')].nom").value("Rousseau"))
                .andExpect(jsonPath("$.theses['auteur'][?(@.nnt=='1997PA040286')].auteurs[?(@.id=='050646176')].prenom").value("Pascal"))
                .andExpect(jsonPath("$.theses['auteur'][?(@.nnt=='1997PA040286')].auteurs[?(@.id=='050646176')].has_idref").value(true))
                .andExpect(jsonPath("$.theses['auteur'][?(@.nnt=='1997PA040286')].directeurs[?(@.id=='026982765')]").exists())
                .andExpect(jsonPath("$.theses['auteur'][?(@.nnt=='1997PA040286')].directeurs[?(@.id=='026982765')].nom").value("Lemoine"))
                .andExpect(jsonPath("$.theses['auteur'][?(@.nnt=='1997PA040286')].directeurs[?(@.id=='026982765')].prenom").value("Serge"))
                .andExpect(jsonPath("$.theses['auteur'][?(@.nnt=='1997PA040286')].directeurs[?(@.id=='026982765')].has_idref").value(true))
                .andExpect(jsonPath("$.theses['auteur'][?(@.nnt=='1997PA040286')].sujets_rameau[*].libelle", Matchers.containsInRelativeOrder("Couleur (art)", "Réalité -- Dans l'art", "Peinture abstraite", "Orphisme (peinture)", "Delaunay, Robert (1885-1941)")))
                /***** Thèse n°2020PA01H073 *****/
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')]").exists())
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].role").value("directeur de thèse"))
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].status").value("soutenue"))
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].titre").value("La mode à l'épreuve de l'art : une historiographie des discours sur la mode en France : 1800-1930"))
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].etablissement_soutenance.nom").value("Paris 1"))
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].discipline").value("Histoire de l'art"))
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].date_soutenance").value("2020-12-01"))
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].auteurs[?(@.id=='221682724')]").exists())
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].auteurs[?(@.id=='221682724')].nom").value("Hammen"))
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].auteurs[?(@.id=='221682724')].prenom").value("Emilie"))
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].auteurs[?(@.id=='221682724')].has_idref").value(true))
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].directeurs[?(@.id=='050646176')]").exists())
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].directeurs[?(@.id=='050646176')].nom").value("Rousseau"))
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].directeurs[?(@.id=='050646176')].prenom").value("Pascal"))
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].directeurs[?(@.id=='050646176')].has_idref").value(true))
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].sujets_rameau[*].libelle", Matchers.containsInRelativeOrder(
                        "Mode",
                        "Mode",
                        "Mode",
                        "Haute couture")))
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].sujets.fr[*]", Matchers.containsInRelativeOrder(
                        "Mode",
                        "Historiographie",
                        "Discours",
                        "Presse de mode",
                        "Physiologies",
                        "Inventeur",
                        "Haute couture",
                        "Artification")))
                .andExpect(jsonPath("$.theses['directeur de thèse'][?(@.nnt=='2020PA01H073')].sujets.en[*]", Matchers.containsInRelativeOrder(
                        "Fashion",
                        "Historiography",
                        "Discourse",
                        "Fashion press",
                        "Physiologies",
                        "Inventor",
                        "Haute couture",
                        "Artification")))

                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')]").exists())
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].role").value("membre du jury"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].status").value("soutenue"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].titre").value("La mode à l'épreuve de l'art : une historiographie des discours sur la mode en France : 1800-1930"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].etablissement_soutenance.nom").value("Paris 1"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].discipline").value("Histoire de l'art"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].date_soutenance").value("2020-12-01"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].auteurs[?(@.id=='221682724')]").exists())
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].auteurs[?(@.id=='221682724')].nom").value("Hammen"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].auteurs[?(@.id=='221682724')].prenom").value("Emilie"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].auteurs[?(@.id=='221682724')].has_idref").value(true))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].directeurs[?(@.id=='050646176')]").exists())
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].directeurs[?(@.id=='050646176')].nom").value("Rousseau"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].directeurs[?(@.id=='050646176')].prenom").value("Pascal"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].directeurs[?(@.id=='050646176')].has_idref").value(true))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].sujets_rameau[*].libelle", Matchers.containsInRelativeOrder(
                        "Mode",
                        "Mode",
                        "Mode",
                        "Haute couture")))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].sujets.fr[*]", Matchers.containsInRelativeOrder(
                        "Mode",
                        "Historiographie",
                        "Discours",
                        "Presse de mode",
                        "Physiologies",
                        "Inventeur",
                        "Haute couture",
                        "Artification")))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2020PA01H073')].sujets.en[*]", Matchers.containsInRelativeOrder(
                        "Fashion",
                        "Historiography",
                        "Discourse",
                        "Fashion press",
                        "Physiologies",
                        "Inventor",
                        "Haute couture",
                        "Artification")))
                /***** Thèse n°2021SORUL154 *****/
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')]").exists())
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].role").value("rapporteur"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].status").value("soutenue"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].titre").value("Les artistes musicalistes : théories et pratiques d'une union des arts (1932-1960)"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].etablissement_soutenance.nom").value("Sorbonne université"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].discipline").value("Histoire de l'art"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].date_soutenance").value("2021-04-17"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].auteurs[?(@.id=='199688613')]").exists())
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].auteurs[?(@.id=='199688613')].nom").value("Sergent"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].auteurs[?(@.id=='199688613')].prenom").value("Marion"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].auteurs[?(@.id=='199688613')].has_idref").value(true))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].directeurs[?(@.id=='033730342')]").exists())
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].directeurs[?(@.id=='033730342')].nom").value("Pierre"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].directeurs[?(@.id=='033730342')].prenom").value("Arnauld"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].directeurs[?(@.id=='033730342')].has_idref").value(true))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].sujets_rameau[*].libelle", Matchers.containsInRelativeOrder(
                        "Musicalisme (peinture)",
                        "Art et musique",
                        "Art abstrait",
                        "Synesthésie",
                        "Valensi, Henry (1883-1960)")))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].sujets.fr[*]", Matchers.containsInRelativeOrder(
                        "Musicalisme",
                        "Artistes musicalistes",
                        "Art moderne",
                        "Art abstrait",
                        "Art total",
                        "Synesthésie",
                        "Esthétique scientifique",
                        "Occultisme")))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2021SORUL154')].sujets.en[*]", Matchers.containsInRelativeOrder(
                        "Musicalisme",
                        "Artistes musicalistes",
                        "Modern art",
                        "Abstract art",
                        "Total art",
                        "Synesthesia",
                        "Scientific aesthetics",
                        "Occultism")))

                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')]").exists())
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].role").value("président du jury"))
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].status").value("soutenue"))
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].titre").value("Les artistes musicalistes : théories et pratiques d'une union des arts (1932-1960)"))
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].etablissement_soutenance.nom").value("Sorbonne université"))
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].discipline").value("Histoire de l'art"))
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].date_soutenance").value("2021-04-17"))
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].auteurs[?(@.id=='199688613')]").exists())
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].auteurs[?(@.id=='199688613')].nom").value("Sergent"))
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].auteurs[?(@.id=='199688613')].prenom").value("Marion"))
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].auteurs[?(@.id=='199688613')].has_idref").value(true))
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].directeurs[?(@.id=='033730342')]").exists())
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].directeurs[?(@.id=='033730342')].nom").value("Pierre"))
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].directeurs[?(@.id=='033730342')].prenom").value("Arnauld"))
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].directeurs[?(@.id=='033730342')].has_idref").value(true))
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].sujets_rameau[*].libelle", Matchers.containsInRelativeOrder(
                        "Musicalisme (peinture)",
                        "Art et musique",
                        "Art abstrait",
                        "Synesthésie", "Valensi, Henry (1883-1960)")))
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].sujets.fr[*]", Matchers.containsInRelativeOrder(
                        "Musicalisme",
                        "Artistes musicalistes",
                        "Art moderne",
                        "Art abstrait",
                        "Art total",
                        "Synesthésie",
                        "Esthétique scientifique",
                        "Occultisme")))
                .andExpect(jsonPath("$.theses['président du jury'][?(@.nnt=='2021SORUL154')].sujets.en[*]", Matchers.containsInRelativeOrder(
                        "Musicalisme",
                        "Artistes musicalistes",
                        "Modern art",
                        "Abstract art",
                        "Total art",
                        "Synesthesia",
                        "Scientific aesthetics",
                        "Occultism")))
                /***** Thèse n°2019LYSE2095 *****/
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')]").exists())
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].role").value("rapporteur"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].status").value("soutenue"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].titre").value("Le making of de la photographie de mode (1932-2017) : culture matérielle, instance collective, image plurielle"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].etablissement_soutenance.nom").value("Lyon"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].discipline").value("Histoire"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].date_soutenance").value("2019-11-15"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].auteurs[?(@.id=='152651071')]").exists())
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].auteurs[?(@.id=='152651071')].nom").value("Van de Casteele"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].auteurs[?(@.id=='152651071')].prenom").value("Marlène"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].auteurs[?(@.id=='152651071')].has_idref").value(true))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].directeurs[?(@.id=='061620262')]").exists())
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].directeurs[?(@.id=='061620262')].nom").value("Claustres"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].directeurs[?(@.id=='061620262')].prenom").value("Annie"))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].directeurs[?(@.id=='061620262')].has_idref").value(true))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].sujets_rameau[*].libelle", Matchers.containsInRelativeOrder("Photographie de mode")))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].sujets.fr[*]", Matchers.containsInRelativeOrder(
                        "Photographie de mode",
                        "Image making",
                        "Micro-monde",
                        "Valeur culturelle",
                        "Trajectoire",
                        "Circulation",
                        "Pratiques collaboratives",
                        "Discours",
                        "Making of ;",
                        "Behind the scene",
                        "Vogue",
                        "Condé Nast",
                        "Production",
                        "Conventions",
                        "Négociation",
                        "Hiérarchie",
                        "Tensions productives",
                        "Réseaux",
                        "Collection",
                        "Exposition",
                        "Institutions muséales",
                        "Image plurielle",
                        "Objet spécifique")))
                .andExpect(jsonPath("$.theses['rapporteur'][?(@.nnt=='2019LYSE2095')].sujets.en[*]", Matchers.containsInRelativeOrder(
                        "Fashion photography",
                        "Image making",
                        "Micro-world",
                        "Cultural value",
                        "Trajectory",
                        "Circulation",
                        "Collaborative practices",
                        "Discourses",
                        "Making of",
                        "Behind the scene",
                        "Vogue",
                        "Condé Nast",
                        "Production",
                        "Conventions",
                        "Hierarchy",
                        "Productive tensions",
                        "Networks",
                        "Collection",
                        "Exhibition",
                        "Museum institutions",
                        "Plural image",
                        "Specific object",
                        "Negotiation")))
                /***** Thèse n°2017PA01H038 *****/
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')]").exists())
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].role").value("membre du jury"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].status").value("soutenue"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].titre").value("La photographie espagnole contemporaine de 1970 à 2010 : miroir d'un pays en quête d'identité"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].etablissement_soutenance.nom").value("Paris 1"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].discipline").value("Histoire de l'art"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].date_soutenance").value("2017-11-14"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].auteurs[?(@.id=='180858416')]").exists())
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].auteurs[?(@.id=='180858416')].nom").value("Conésa"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].auteurs[?(@.id=='180858416')].prenom").value("Héloïse"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].auteurs[?(@.id=='180858416')].has_idref").value(true))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].directeurs[?(@.id=='032074638')]").exists())
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].directeurs[?(@.id=='032074638')].nom").value("Poivert"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].directeurs[?(@.id=='032074638')].prenom").value("Michel"))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].directeurs[?(@.id=='032074638')].has_idref").value(true))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].sujets_rameau[*].libelle", Matchers.containsInRelativeOrder("Photographie")))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].sujets.fr[*]", Matchers.containsInRelativeOrder(
                        "Contemporaine",
                        "Espagne",
                        "Franquisme",
                        "Photographie",
                        "Local-Global",
                        "Interculturalité",
                        "Transterritorialité",
                        "Postmodernité")))
                .andExpect(jsonPath("$.theses['membre du jury'][?(@.nnt=='2017PA01H038')].sujets.en[*]", Matchers.containsInRelativeOrder(
                        "Contemporary",
                        "Spain",
                        "Francoism",
                        "Photography",
                        "Transterritoriality",
                        "Postmodernity")));
    }
}
