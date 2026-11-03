package fr.abes.thesesapirecherche.theses.converters;

import org.springframework.stereotype.Component;

import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.theses.dto.batch.BatchTheseResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


@Component 
public class BatchTheseMapper {

    private static final DateTimeFormatter DD_MM_YYYY =
            DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.of("Europe/Paris"));

    public BatchTheseResponseDto theseToBatchDtoSingle(These these) {
        return these == null ? null : BatchTheseResponseDto.builder()
                .id(these.getIdDoc())
                .titrePrincipal(these.getTitrePrincipal())
                .nnt(these.getNnt())
                .doi(these.getDoi())
                .numSujet(these.getNumSujet())
                .numSujetSansS(these.getNumSujetSansS())
                .dateSoutenance(these.getDateSoutenance() != null ? these.getDateSoutenance().format(DD_MM_YYYY) : null)
                .datePremiereInscriptionDoctorat(these.getDatePremiereInscriptionDoctorat() != null ? these.getDatePremiereInscriptionDoctorat().format(DD_MM_YYYY) : null)
                .discipline(these.getDiscipline())
                .titres(these.getTitres())
                .resumes(these.getResumes())
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
}
