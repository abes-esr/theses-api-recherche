package fr.abes.thesesapirecherche.theses.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ValueConverter;

//import org.springframework.data.elasticsearch.annotations.DateFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
// TODO introduire indexName de façon plus clean ???
@Document(indexName = "theses3", createIndex = false)
public class These {
    @Id
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
    @Field(type = FieldType.Date, format = org.springframework.data.elasticsearch.annotations.DateFormat.date)
    private LocalDate dateSoutenance;
    @Field(type = FieldType.Date, format = org.springframework.data.elasticsearch.annotations.DateFormat.date)
    private LocalDate dateFinEmbargo;
    @Field(type = FieldType.Date, format = org.springframework.data.elasticsearch.annotations.DateFormat.date)
    private LocalDate dateFiltre;
    @Field(type = FieldType.Date, format = org.springframework.data.elasticsearch.annotations.DateFormat.date)
    private LocalDate datePremiereInscriptionDoctorat;
    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd'T'HH:mm:ssX||uuuu-MM-dd'T'HH:mm:ss")
    @ValueConverter(CustomZonedDateTimeConverter.class)
    private ZonedDateTime dateCines;
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
}
