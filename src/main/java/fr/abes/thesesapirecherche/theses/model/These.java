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

    String dateInsertionDansES;
    String cas;
    String titrePrincipal;
    String nnt;
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
    List<String> langues;
    List<String> oaiSets;

    List<String> oaiSetNames;
    List<Sujet> sujets;
    List<String> sujetsLibelle;
    String discipline;
    Map<String, String> titres;
    Map<String, String> resumes;
    Organisme etabSoutenance;
    String etabSoutenanceN;
    String etabSoutenancePpn;
    List<Organisme> partenairesRecherche;
    List<String> partenairesRechercheN;
    List<String> partenairesRecherchePpn;
    List<String> sujetsRameauLibelle;
    List<String> sujetsRameauPpn;
    List<SujetsRameau> sujetsRameau;
    List<PersonneThese> membresJury;
    List<String> membresJuryNP;
    List<String> membresJuryPN;
    List<String> membresJuryPpn = new ArrayList<>();
    List<PersonneThese> rapporteurs;
    List<String> rapporteursNP;
    List<String> rapporteursPN;
    List<String> rapporteursPpn = new ArrayList<>();
    List<PersonneThese> auteurs;
    List<String> auteursNP;
    List<String> auteursPN;
    List<String> auteursPpn = new ArrayList<>();
    List<PersonneThese> directeurs;
    List<String> directeursNP;
    List<String> directeursPN;
    List<String> directeursPpn = new ArrayList<>();
    List<Organisme> ecolesDoctorales;
    List<String> ecolesDoctoralesPpn;
    List<String> ecolesDoctoralesN;
    List<Organisme> etabsCotutelle;
    List<String> etabsCotutellePpn;
    List<String> etabsCotutelleN;
    PersonneThese presidentJury;
    String presidentJuryNP;
    String presidentJuryPN;
    String presidentJuryPpn;
    String theseTravaux;

    public String getDateSoutenance() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateSoutenance != null ? dateFormat.format(dateSoutenance) : null;
    }

    public String getDatePremiereInscriptionDoctorat() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return datePremiereInscriptionDoctorat != null ? dateFormat.format(datePremiereInscriptionDoctorat) : null;
    }
}
