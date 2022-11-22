package fr.abes.thesesapirecherche.theses.model;

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
    Map<String, String> sujets;
    String discipline;
    Map<String, String> titres;
    Map<String, String> resumes;
    Organisme etabSoutenance;
    List<Organisme> etabCotutelle;
    List<Organisme> partenairesRecherche;
    List<String> sujetsRameau;
    List<PersonneThese> membresJury;
    List<PersonneThese> rapporteurs;
    List<PersonneThese> auteurs;
    List<PersonneThese> directeurs;
    String cas;
    List<Organisme> ecolesDoctorales;
    List<Organisme> etabsCotutelle;
    PersonneThese presidentJury;

    public String getDateSoutenance() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateSoutenance != null ? dateFormat.format(dateSoutenance) : null;
    }
}