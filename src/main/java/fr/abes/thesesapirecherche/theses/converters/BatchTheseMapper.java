package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.service.DateFormatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.theses.dto.batch.BatchTheseResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static fr.abes.thesesapirecherche.theses.converters.TheseMappingHelper.formatKeywords;


@Component
@RequiredArgsConstructor
public class BatchTheseMapper {
    private final SujetsToMapMapper sujetsToMapMapper;
    private final DateFormatService dateFormatService;

    public BatchTheseResponseDto theseToBatchDto(Hit<These> theseHit) {
        These these = theseHit.source();
        return BatchTheseResponseDto.builder()
                .id(theseHit.id())
                .titrePrincipal(Objects.requireNonNull(these).getTitrePrincipal())
                .nnt(these.getNnt())
                .doi(these.getDoi())
                .numSujet(these.getNumSujet())
                .numSujetSansS(these.getNumSujetSansS())
                .dateSoutenance(dateFormatService.formatDate(these::getDateSoutenance))
                .datePremiereInscriptionDoctorat(dateFormatService.formatDate(these::getDatePremiereInscriptionDoctorat))
                .discipline(these.getDiscipline())
                .titres(these.getTitres())
                .resumes(these.getResumes())
                .etabSoutenance(TheseMappingHelper.organismeToDto(these.getEtabSoutenance()))
                .codeEtab(these.getCodeEtab())
                .etabCotutelle(TheseMappingHelper.organismesToDto(these.getEtabsCotutelle()))
                .partenairesRecherche(TheseMappingHelper.organismesToDto(these.getPartenairesRecherche()))
                .mapSujets(formatKeywords(these).entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().stream()
                                        .map(model -> sujetsToMapMapper.sujetsToMapToDto(model))
                                        .collect(Collectors.toList())
                        )))
                .membresJury(TheseMappingHelper.personnesToDto(these.getMembresJury()))
                .rapporteurs(TheseMappingHelper.personnesToDto(these.getRapporteurs()))
                .auteurs(TheseMappingHelper.personnesToDto(these.getAuteurs()))
                .directeurs(TheseMappingHelper.personnesToDto(these.getDirecteurs()))
                .dateCines(dateFormatService.formatDate(these::getDateCines))
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
