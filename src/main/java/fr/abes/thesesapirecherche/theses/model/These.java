package fr.abes.thesesapirecherche.theses.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class These {

    String cas;
    String titrePrincipal;
    String nnt;
    String accessible;
    String status;
    String source;
    String codeEtab;
    Date dateSoutenance;
    Date dateFinEmbargo;
    List<String> langues;
    List<String> oaiSets;
    List<String> sujetsFR;
    List<String> sujetsEN;
    String discipline;
    Map<String, String> titres;
    Map<String, String> resumes;
    Organisme etabSoutenance;
    String etabSoutenanceN;
    List<Organisme> partenairesRecherche;
    List<String> partenairesRechercheN;
    List<String> sujetsRameau;
    List<PersonneThese> membresJury;
    List<String> membresJuryNP;
    List<PersonneThese> rapporteurs;
    List<String> rapporteursNP;
    List<PersonneThese> auteurs;
    List<String> auteursNP;
    List<PersonneThese> directeurs;
    List<String> directeursNP;

    List<Organisme> ecolesDoctorales;
    List<String> ecolesDoctoralesN;
    List<Organisme> etabsCotutelle;
    List<String> etabsCotutelleN;
    PersonneThese presidentJury;
    String presidentJuryNP;
    String theseTravaux;

    public String getDateSoutenance() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateSoutenance != null ? dateFormat.format(dateSoutenance) : null;
    }
}
