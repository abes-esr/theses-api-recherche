package fr.abes.thesesapirecherche.theses.converters;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseLiteResponseDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;


@Component("TheseClientTheseMapper")
public class ClientTheseMapper {

    private static final DateTimeFormatter DD_MM_YYYY =
            DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.of("Europe/Paris"));

    public ClientTheseResponseDto theseToClientDto(These these) {
        return ClientTheseResponseDto.builder()
                .titrePrincipal(these.getTitrePrincipal())
                .nnt(these.getNnt())
                .doi(these.getDoi())
                .numSujet(these.getNumSujet())
                .numSujetSansS(these.getNumSujetSansS())
                .dateSoutenance(these.getDateSoutenance() != null ? these.getDateSoutenance().format(DD_MM_YYYY) : null)
                .datePremiereInscriptionDoctorat(these.getDatePremiereInscriptionDoctorat() != null ? these.getDatePremiereInscriptionDoctorat().format(DD_MM_YYYY) : null)
                .discipline(these.getDiscipline())
                .resumes(these.getResumes())
                .titres(these.getTitres())
                .etabSoutenance(TheseMappingHelper.organismeToDto(these.getEtabSoutenance()))
                .codeEtab(these.getCodeEtab())
                .etabCotutelle(TheseMappingHelper.organismesToDto(these.getEtabsCotutelle()))
                .partenairesRecherche(TheseMappingHelper.organismesToDto(these.getPartenairesRecherche()))
                .mapSujets(TheseMappingHelper.formatKeywords(these))
                .membresJury(TheseMappingHelper.personnesToDto(these.getMembresJury()))
                .rapporteurs(TheseMappingHelper.personnesToDto(these.getRapporteurs()))
                .auteurs(TheseMappingHelper.personnesToDto(these.getAuteurs()))
                .directeurs(TheseMappingHelper.personnesToDto(these.getDirecteurs()))
                .dateCines(these.getDateCines() != null ? DD_MM_YYYY.format(these.getDateCines()) : null)
                .langues(these.getLangues())
                .oaiSetNames(these.getOaiSetNames())
                .cas(these.getCas())
                .accessible(these.getAccessible())
                .ecolesDoctorales(TheseMappingHelper.organismesToDto(these.getEcolesDoctorales()))
                .presidentJury(TheseMappingHelper.personneToDto(these.getPresidentJury()))
                .source(these.getSource())
                .status(these.getStatus())
                .isSoutenue(these.getIsSoutenue())
                .build();
    }

    public ClientTheseLiteResponseDto theseToClientLiteDto(These uneThese) {
        return ClientTheseLiteResponseDto.builder()
                .id(uneThese.getIdDoc())
                .titrePrincipal(uneThese.getTitrePrincipal())
                .nnt(uneThese.getNnt())
                .doi(uneThese.getDoi())
                .numSujet(uneThese.getNumSujet())
                .numSujetSansS(uneThese.getNumSujetSansS())
                .dateSoutenance(uneThese.getDateSoutenance() != null ? uneThese.getDateSoutenance().format(DD_MM_YYYY) : null)
                .datePremiereInscriptionDoctorat(uneThese.getDatePremiereInscriptionDoctorat() != null ? uneThese.getDatePremiereInscriptionDoctorat().format(DD_MM_YYYY) : null)
                .discipline(uneThese.getDiscipline())
                .titres(uneThese.getTitres())
                .etabSoutenance(TheseMappingHelper.organismeToDto(uneThese.getEtabSoutenance()))
                .codeEtab(uneThese.getCodeEtab())
                .etabCotutelle(TheseMappingHelper.organismesToDto(uneThese.getEtabsCotutelle()))
                .partenairesRecherche(TheseMappingHelper.organismesToDto(uneThese.getPartenairesRecherche()))
                .mapSujets(TheseMappingHelper.formatKeywords(uneThese))
                .membresJury(TheseMappingHelper.personnesToDto(uneThese.getMembresJury()))
                .rapporteurs(TheseMappingHelper.personnesToDto(uneThese.getRapporteurs()))
                .auteurs(TheseMappingHelper.personnesToDto(uneThese.getAuteurs()))
                .directeurs(TheseMappingHelper.personnesToDto(uneThese.getDirecteurs()))
                .dateCines(uneThese.getDateCines() != null ? DD_MM_YYYY.format(uneThese.getDateCines()) : null)
                .langues(uneThese.getLangues())
                .oaiSetNames(uneThese.getOaiSetNames())
                .cas(uneThese.getCas())
                .accessible(uneThese.getAccessible())
                .ecolesDoctorales(TheseMappingHelper.organismesToDto(uneThese.getEcolesDoctorales()))
                .presidentJury(TheseMappingHelper.personneToDto(uneThese.getPresidentJury()))
                .source(uneThese.getSource())
                .status(uneThese.getStatus())
                .isSoutenue(uneThese.getIsSoutenue())
                .build();
    }

    public List<ClientTheseLiteResponseDto> theseToClientLiteDto(List<These> theseList) {
        return theseList.stream().map(this::theseToClientLiteDto).toList();
    }
}
