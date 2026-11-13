package fr.abes.thesesapirecherche.theses.service;

import fr.abes.thesesapirecherche.theses.dto.*;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseResponseDto;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

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
    private record ColumnRecord<T>(@NonNull String header, Function<T, String> customFormatter) {}

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
                        .map(c -> String.format("\"%s\"", c.header()))
                        .collect(Collectors.joining(DELIMITEUR)))
                .append("\n");

        // rows
        for (T row : rows) {
            sb.append(this.columnList.stream()
                            .map(c -> format("\"%s\"", Objects.toString(c.customFormatter().apply(row), "")))
                            .collect(Collectors.joining(DELIMITEUR)))
                    .append(format("%s\n", DELIMITEUR));
        }
        return sb.toString();
    }

    private static String formatPersonne(PersonneRefDto personne) {
        String name = joinNonEmpty(", ", personne.getNom(), personne.getPrenom());
        String ppn = Objects.toString(personne.getPpn(), "");
        return ppn.isEmpty() ? name : joinNonEmpty(" ", name, "(" + ppn + ")");
    }

    private static String formatOrganisme(OrganismeRefDto organismeRefDto) {
        String name = organismeRefDto.getNom();
        String ppn = Objects.toString(organismeRefDto.getPpn(), "");
        return ppn.isEmpty() ? name : joinNonEmpty(" ", name, "(" + ppn + ")");
    }

    private static String joinNonEmpty(String separator, String... parts) {
        return Arrays.stream(parts)
                .filter(StringUtils::isNotBlank)
                .map(s -> s.replace("\"", ""))
                .collect(Collectors.joining(separator));
    }

    /**
     * Décrit comment transformer chaque attribut d'un objet ClientTheseResponseDto en un String.
     * Ce String sera inséré dans un fichier .csv<br>
     *
     * Les champs à valeurs multiples sont séparés par ||, SAUF le champ des partenaires de recherches
     * où les valeurs sont séparés par une virgule. Je ne sais pas pourquoi. Je conserve simplement le
     * comportement précédent.<br>
     *
     * Certains champs ont un ppn, ou PICA Production Number, il est inclus entre parenthèses que s'il est non null.
     * Example: Nom, Prénom (ppn)
     */
    public static final CsvWriter<ClientTheseResponseDto> ClientTheseResponseDtoCsvWriter = new CsvWriter<ClientTheseResponseDto>()
            .setUpColumnExtraction("NNT", ClientTheseResponseDto::getNnt)
            .setUpColumnExtraction("DOI", ClientTheseResponseDto::getDoi)
            .setUpColumnExtraction("NumSujet", ClientTheseResponseDto::getNumSujet)
            .setUpColumnExtraction("NumSujetSansS", ClientTheseResponseDto::getNumSujetSansS)
            .setUpColumnExtraction("Auteurs", theses -> theses.getAuteurs().stream()
                    .map(CsvWriter::formatPersonne)
                    .map(formattedName -> formattedName.replace("\"", ""))
                    .collect(Collectors.joining(" || ")))
            .setUpColumnExtraction("Titre FR", these -> Objects.toString(these.getTitrePrincipal(), "").replace("\"", ""))
            .setUpColumnExtraction("Titre EN", uneThese -> Objects.toString(uneThese.getTitres().get("en"), "").replace("\"", ""))
            .setUpColumnExtraction("Directeurs", uneThese -> uneThese.getDirecteurs().stream()
                    .map(CsvWriter::formatPersonne)
                    .map(formattedName -> formattedName.replace("\"", ""))
                    .collect(Collectors.joining(" || ")))
            .setUpColumnExtraction("Discipline", these -> Objects.toString(these.getDiscipline(), "")
                    .replace("\"", ""))
            .setUpColumnExtraction("Date de soutenance", ClientTheseResponseDto::getDateSoutenance)
            .setUpColumnExtraction("Date de première inscription", ClientTheseResponseDto::getDatePremiereInscriptionDoctorat)
            .setUpColumnExtraction("Date d'archivage CINES", ClientTheseResponseDto::getDateCines)
            .setUpColumnExtraction("Etablissement de soutenance", uneThese -> Objects.toString(CsvWriter.formatOrganisme(uneThese.getEtabSoutenance()), "")
                    .replace("\"", ""))
            .setUpColumnExtraction("Code etablissement", these -> Objects.toString(these.getCodeEtab(), "")
                    .replace("\"", ""))
            .setUpColumnExtraction("Etablissement de cotutelle", uneThese -> uneThese.getEtabCotutelle().stream()
                    .map(CsvWriter::formatOrganisme)
                    .filter(Objects::nonNull)
                    .map(s -> s.replace("\"", "").replace("||", ""))
                    .collect(Collectors.joining(" || ")))
            .setUpColumnExtraction("Ecoles doctorales", uneThese -> uneThese.getEcolesDoctorales().stream()
                    .map(CsvWriter::formatOrganisme)
                    .filter(Objects::nonNull)
                    .map(s -> s.replace("\"", "").replace("||", " "))
                    .collect(Collectors.joining(" || ")))
            .setUpColumnExtraction("Partenaires de recherche", uneThese -> uneThese.getPartenairesRecherche().stream()
                            .map(CsvWriter::formatOrganisme)
                            .filter(Objects::nonNull)
                            .map(s -> s.replace("\"", ""))
                            .collect(Collectors.joining(", "))
                /*
                !!!
                Attention, pour préserver le comportement déjà présent, le délimiteur entre plusieurs
                partenaires de recherche pour uneThese est une virgule. Ce qui unique pour ce champ-là,
                les autres champs utilisent || pour séparer les multiples éléments.
                !!!
                 */
            )
            .setUpColumnExtraction("Président du jury", uneThese -> CsvWriter.formatPersonne(uneThese.getPresidentJury())
                    .replace("\"", ""))
            .setUpColumnExtraction("Rapporteurs", uneThese -> uneThese.getRapporteurs().stream()
                    .map(CsvWriter::formatPersonne)
                    .map(s -> s.replace("\"", ""))
                    .collect(Collectors.joining(" || ")))
            .setUpColumnExtraction("Examinateurs", uneThese -> uneThese.getMembresJury().stream()
                    .map(CsvWriter::formatPersonne)
                    .map(s -> s.replace("\"", ""))
                    .collect(Collectors.joining(" || ")))
            .setUpColumnExtraction("Mots clés FR", uneThese -> {
                List<String> acc = new ArrayList<>();
                if (!uneThese.getMapSujets().containsKey("fr")) return "";
                for(SujetDto sujetDto : uneThese.getMapSujets().get("fr")) {
                    acc.add(sujetDto.getKeyword().replace("\"", "").replace("||", " "));
                }
                return String.join(" || ", acc);
            })
            .setUpColumnExtraction("Mots clés EN", uneThese -> {
                List<String> acc = new ArrayList<>();
                if (!uneThese.getMapSujets().containsKey("en")) return "";
                for(SujetDto sujetDto : uneThese.getMapSujets().get("en")) {
                    acc.add(sujetDto.getKeyword().replace("\"", "").replace("||", " "));
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