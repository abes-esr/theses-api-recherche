package fr.abes.thesesapirecherche.theses.service;

import fr.abes.thesesapirecherche.theses.dto.*;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseResponseDto;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;

public final class CsvWriter<T> {

    private final String DELIMITEUR = ";";

    /**
     * Contient le nom du champ et sa fonction de formatage.<br>
     * Example d'utilisation, voir {@link ClientTheseResponseDtoCsvWriter}
     * @param header le nom d'un champ que l'on veut voir apparaitre dans le fichier .csv
     * @param customFormatter une fonction qui extrait un champ de {@link T} vers un String
     * @param <T> le type que l'on veut décrire sous format .csv
     */
    private record ColumnRecord<T>(String header, Function<T, String> customFormatter) {}

    private final List<ColumnRecord<T>> columnList = new ArrayList<>();

    private CsvWriter<T> setUpColumnExtraction(String header, Function<T, String> customFormatter) {
        this.columnList.add(new ColumnRecord<>(header, customFormatter));
        return this;
    }

    /**
     * <p>
     *     Décrit de façon générale, la forme d'un fichier .csv<br>
     *     Chaque nom de colonne sera entouré de double guillemets et séparé par des {@link DELIMITEUR}.
     * </p>
     * <p>
     *     La 1er ligne sera le header.
     *     À la fin le la 1er ligne, il n'y aura qu'un retour à la ligne.
     * </p>
     * <p>
     *     Pour les lignes suivantes, à la fin de chaque ligne, il y aura à la fois
     *     un {@link DELIMITEUR} ET un retour à la ligne.
     * </p>
     * @param rows liste des objects de type {@link T} qui composeront le String au format csv
     * @return String au format csv
     */
    public String writeToCsvFrom(List<T> rows) {
        StringBuilder sb = new StringBuilder();

        sb.append(this.columnList.stream()
                        .map(c -> format("\"%s\"", escape(c.header())))
                        .collect(Collectors.joining(DELIMITEUR)))
                .append("\n");

        // rows
        for (T row : rows) {
            sb.append(this.columnList.stream()
                            .map(c -> format("\"%s\"", escape(c.customFormatter().apply(row))))
                            .collect(Collectors.joining(DELIMITEUR)))
                    .append(format("%s\n", DELIMITEUR));
        }
        return sb.toString();
    }

    /**
     * Supprime les doubles guillemets français dans value
     *
     * @param value un String que l'on veut échapper
     * @return un String ou null si value est null
     */
    private static String escape(String value) {// fixme huge doubt
        if (value == null) return "";
        return value.replace("\"", "");
    }

    /**
     * Décrit comment transformer chaque attribut d'un objet ClientTheseResponseDto en une String.
     * Cette String sera inséré dans un fichier .csv<br>
     *
     * Les champs à valeurs multiples sont séparés par ||, SAUF le champ des partenaires de recherches
     * où les valeurs sont séparés par une virgule. Je ne sais pas pourquoi. Je conserve simplement le
     * comportement précédent.<br>
     *
     * Certains champs ont un ppn, ou PICA Production Number, il est inclus entre parenthèses que s'il est non null.
     * Example: Nom, Prénom (ppn)
     */
    public static final CsvWriter<ClientTheseResponseDto> ClientTheseResponseDtoCsvWriter = new CsvWriter<ClientTheseResponseDto>()
            .setUpColumnExtraction("NNT",      ClientTheseResponseDto::getNnt)
            .setUpColumnExtraction("DOI",    ClientTheseResponseDto::getDoi)
            .setUpColumnExtraction("NumSujet",   ClientTheseResponseDto::getNumSujet)
            .setUpColumnExtraction("NumSujetSansS", ClientTheseResponseDto::getNumSujetSansS)
            .setUpColumnExtraction("Auteurs", theses -> {
                List<String> acc = new ArrayList<>();
                for (PersonneRefDto auteurDto: theses.getAuteurs()) {
                    if (auteurDto.getPpn() == null)
                        acc.add(String.format("%s, %s", auteurDto.getNom(), auteurDto.getPrenom()));
                    else
                        acc.add(String.format("%s, %s (%s)", auteurDto.getNom(), auteurDto.getPrenom(), auteurDto.getPpn()));
                }
                return String.join(" || ", acc);
            })
            .setUpColumnExtraction("Titre FR", ClientTheseResponseDto::getTitrePrincipal)
            .setUpColumnExtraction("Titre EN", uneThese -> uneThese.getTitres().get("en"))
            .setUpColumnExtraction("Directeurs", uneThese -> {
                List<String> acc = new ArrayList<>();
                for (PersonneRefDto auteurDto: uneThese.getDirecteurs()) {
                    if (auteurDto.getPpn() == null)
                        acc.add(String.format("%s, %s", auteurDto.getNom(), auteurDto.getPrenom()));
                    else
                        acc.add(String.format("%s, %s (%s)", auteurDto.getNom(), auteurDto.getPrenom(), auteurDto.getPpn()));
                }
                return String.join(" || ", acc);
            })
            .setUpColumnExtraction("Discipline", ClientTheseResponseDto::getDiscipline)
            .setUpColumnExtraction("Date de soutenance", ClientTheseResponseDto::getDateSoutenance)
            .setUpColumnExtraction("Date de première inscription", ClientTheseResponseDto::getDatePremiereInscriptionDoctorat)
            .setUpColumnExtraction("Date d'archivage CINES", ClientTheseResponseDto::getDateCines)
            .setUpColumnExtraction("Etablissement de soutenance", uneThese -> {
                List<String> acc = new ArrayList<>();
                OrganismeRefDto uneOrg = uneThese.getEtabSoutenance();
                if (uneOrg.getPpn() == null)
                    acc.add(String.format("%s", uneOrg.getNom()));
                else
                    acc.add(String.format("%s (%s)", uneOrg.getNom(), uneOrg.getPpn()));

                return String.join(" || ", acc);
            })
            .setUpColumnExtraction("Code etablissement", ClientTheseResponseDto::getCodeEtab)
            .setUpColumnExtraction("Etablissement de cotutelle", uneThese -> {
                List<String> acc = new ArrayList<>();
                for (OrganismeRefDto org: uneThese.getEtabCotutelle()) {
                    if (org.getPpn() == null)
                        acc.add(String.format("%s", org.getNom()));
                    else
                        acc.add(String.format("%s (%s)", org.getNom(), org.getPpn()));
                }
                return String.join(" || ", acc);
            })
            .setUpColumnExtraction("Ecoles doctorales", uneThese -> {
                List<String> acc = new ArrayList<>();
                for (OrganismeRefDto org: uneThese.getEcolesDoctorales()) {
                    if (org.getPpn() == null)
                        acc.add(String.format("%s", org.getNom()));
                    else
                        acc.add(String.format("%s (%s)", org.getNom(), org.getPpn()));
                }
                return String.join(" || ", acc);
            })
            .setUpColumnExtraction("Partenaires de recherche", uneThese -> {
                List<String> acc = new ArrayList<>();
                for (OrganismeRefDto org: uneThese.getPartenairesRecherche()) {
                    if (org.getPpn() == null)
                        acc.add(String.format("%s", org.getNom()));
                    else
                        acc.add(String.format("%s (%s)", org.getNom(), org.getPpn()));
                }
                return String.join(", ", acc);
                /*
                !!!
                Attention, pour préserver le comportement déjà présent, le délimiteur entre plusieurs
                partenaires de recherche pour uneThese est une virgule. Ce qui unique pour ce champ-là,
                les autres champs utilisent || pour séparer les multiples éléments.
                !!!
                 */
            })
            .setUpColumnExtraction("Président du jury", uneThese -> {
                PersonneRefDto prez = uneThese.getPresidentJury();
                if (prez.getNom() == null) return "";
                else if (prez.getPpn() == null)
                    return (String.format("%s, %s", prez.getNom(), prez.getPrenom()));
                else
                    return String.format("%s, %s (%s)", prez.getNom(), prez.getPrenom(), prez.getPpn());
            })
            .setUpColumnExtraction("Rapporteurs", uneThese -> {
                List<String> acc = new ArrayList<>();
                for(PersonneRefDto unRapporteur : uneThese.getRapporteurs()) {
                    if (unRapporteur.getPpn() == null)
                        acc.add(String.format("%s, %s", unRapporteur.getNom(), unRapporteur.getPrenom()));
                    else
                        acc.add(String.format("%s, %s (%s)", unRapporteur.getNom(), unRapporteur.getPrenom(), unRapporteur.getPpn()));
                }
                return String.join(" || ", acc);
            })
            .setUpColumnExtraction("Examinateurs", uneThese -> {
                List<String> acc = new ArrayList<>();
                for(PersonneRefDto unMembreJury : uneThese.getMembresJury()) {
                    if (unMembreJury.getPpn() == null)
                        acc.add(String.format("%s, %s", unMembreJury.getNom(), unMembreJury.getPrenom()));
                    else
                        acc.add(String.format("%s, %s (%s)", unMembreJury.getNom(), unMembreJury.getPrenom(), unMembreJury.getPpn()));
                }
                return String.join(" || ", acc);
            })
            .setUpColumnExtraction("Mots clés FR", uneThese -> {
                List<String> acc = new ArrayList<>();
                if (!uneThese.getMapSujets().containsKey("fr")) return "";
                for(SujetDto sujetDto : uneThese.getMapSujets().get("fr")) {
                    acc.add(sujetDto.getKeyword());
                }
                return String.join(" || ", acc);
            })
            .setUpColumnExtraction("Mots clés EN", uneThese -> {
                List<String> acc = new ArrayList<>();
                if (!uneThese.getMapSujets().containsKey("en")) return "";
                for(SujetDto sujetDto : uneThese.getMapSujets().get("en")) {
                    acc.add(sujetDto.getKeyword());
                }
                return String.join(" || ", acc);
            })
            .setUpColumnExtraction("Langue", uneThese -> String.join(" || ", uneThese.getLangues()))
            .setUpColumnExtraction("Noms des sets OAI", uneThese -> String.join(" || ", uneThese.getOaiSetNames()))
            .setUpColumnExtraction("Source", ClientTheseResponseDto::getSource)
            .setUpColumnExtraction("Statut", uneThese -> "soutenue".equals(uneThese.getStatus()) ? "soutenue" : "en cours")
            .setUpColumnExtraction("Cas", ClientTheseResponseDto::getCas)
            .setUpColumnExtraction("Accessible", ClientTheseResponseDto::getAccessible);
}