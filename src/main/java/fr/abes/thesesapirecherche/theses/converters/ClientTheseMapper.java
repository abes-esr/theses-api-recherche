package fr.abes.thesesapirecherche.theses.converters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.abes.thesesapirecherche.theses.service.DateFormats;
import org.springframework.stereotype.Component;

import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseLiteResponseDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;

import static fr.abes.thesesapirecherche.theses.converters.TheseMappingHelper.formatKeywords;


@Component("TheseClientTheseMapper")
public class ClientTheseMapper {
    SujetsToMapMapper sujetsToMapMapper = new SujetsToMapMapper();

    public ClientTheseResponseDto theseToClientDto(These these) {
        return ClientTheseResponseDto.builder()
                .titrePrincipal(these.getTitrePrincipal())
                .nnt(these.getNnt())
                .doi(these.getDoi())
                .numSujet(these.getNumSujet())
                .numSujetSansS(these.getNumSujetSansS())
                .dateSoutenance(DateFormats.formatDate(these::getDateSoutenance))
                .datePremiereInscriptionDoctorat(DateFormats.formatDate(these::getDatePremiereInscriptionDoctorat))
                .discipline(these.getDiscipline())
                .resumes(these.getResumes())
                .titres(these.getTitres())
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
                .dateCines(DateFormats.formatDate(these::getDateCines))
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

    public List<ClientTheseResponseDto> thesesToClientDto(List<These> personnes) {
        List<ClientTheseResponseDto> results = new ArrayList<>();
        for (These item : personnes) {
            results.add(theseToClientDto(item));
        }
        return results;
    }



    public ClientTheseLiteResponseDto theseToClientLiteDto(Hit<These> theseHit) {
        These these = theseHit.source();

        return ClientTheseLiteResponseDto.builder()
                .id(theseHit.id())
                .titrePrincipal(these.getTitrePrincipal())
                .nnt(these.getNnt())
                .doi(these.getDoi())
                .numSujet(these.getNumSujet())
                .numSujetSansS(these.getNumSujetSansS())
                .dateSoutenance(DateFormats.formatDate(these::getDateSoutenance))
                .datePremiereInscriptionDoctorat(DateFormats.formatDate(these::getDatePremiereInscriptionDoctorat))
                .discipline(these.getDiscipline())
                .titres(these.getTitres())
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
                .dateCines(DateFormats.formatDate(these::getDateCines))
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
