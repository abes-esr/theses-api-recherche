package fr.abes.thesesapirecherche.theses.model;

import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class These {

    String idDoc;
    String dateInsertionDansES;
    String cas;
    String titrePrincipal;
    String nnt;
    String doi;
    String numSujet;
    String numSujetSansS;
    String accessible;
    String status;
    Boolean isSoutenue;
    String source;
    String codeEtab;
    Date dateSoutenance;
    Date dateFinEmbargo;
    Date dateFiltre;
    Date datePremiereInscriptionDoctorat;
    Date dateCines;
    List<String> langues = new ArrayList<>();
    List<String> oaiSets = new ArrayList<>();

    List<String> oaiSetNames = new ArrayList<>();
    List<Sujet> sujets = new ArrayList<>();
    List<String> sujetsLibelle = new ArrayList<>();
    String discipline;
    Map<String, String> titres;
    Map<String, String> resumes;
    Organisme etabSoutenance;
    String etabSoutenanceN;
    String etabSoutenancePpn;
    List<Organisme> partenairesRecherche = new ArrayList<>();
    List<String> partenairesRechercheN = new ArrayList<>();
    List<String> partenairesRecherchePpn = new ArrayList<>();
    List<String> sujetsRameauLibelle = new ArrayList<>();
    List<String> sujetsRameauPpn = new ArrayList<>();
    List<SujetsRameau> sujetsRameau = new ArrayList<>();
    List<PersonneThese> membresJury = new ArrayList<>();
    List<String> membresJuryNP = new ArrayList<>();
    List<String> membresJuryPN = new ArrayList<>();
    List<String> membresJuryPpn = new ArrayList<>();
    List<PersonneThese> rapporteurs = new ArrayList<>();
    List<String> rapporteursNP = new ArrayList<>();
    List<String> rapporteursPN = new ArrayList<>();
    List<String> rapporteursPpn = new ArrayList<>();
    List<PersonneThese> auteurs = new ArrayList<>();
    List<String> auteursNP = new ArrayList<>();
    List<String> auteursPN = new ArrayList<>();
    List<String> auteursPpn = new ArrayList<>();
    List<PersonneThese> directeurs = new ArrayList<>();
    List<String> directeursNP = new ArrayList<>();
    List<String> directeursPN = new ArrayList<>();
    List<String> directeursPpn = new ArrayList<>();
    List<Organisme> ecolesDoctorales = new ArrayList<>();
    List<String> ecolesDoctoralesPpn = new ArrayList<>();
    List<String> ecolesDoctoralesN = new ArrayList<>();
    List<Organisme> etabsCotutelle = new ArrayList<>();
    List<String> etabsCotutellePpn = new ArrayList<>();
    List<String> etabsCotutelleN = new ArrayList<>();
    PersonneThese presidentJury;
    String presidentJuryNP;
    String presidentJuryPN;
    String presidentJuryPpn;
    String theseTravaux;

    // TODO: extraire ces fonctions dans un service
    public String getDateSoutenance() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateSoutenance != null ? dateFormat.format(dateSoutenance) : null;
    }

    public String getDatePremiereInscriptionDoctorat() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return datePremiereInscriptionDoctorat != null ? dateFormat.format(datePremiereInscriptionDoctorat) : null;
    }

    public String getDateCines() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateCines != null ? dateFormat.format(dateCines) : null;
    }
}
