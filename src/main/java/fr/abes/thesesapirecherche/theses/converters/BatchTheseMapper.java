package fr.abes.thesesapirecherche.theses.converters;

import org.springframework.stereotype.Component;

import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.theses.dto.batch.BatchTheseResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;


@Component 
public class BatchTheseMapper {
    

    public BatchTheseResponseDto theseToBatchDto(Hit<These> theseHit) {
        These these = theseHit.source();
        return BatchTheseResponseDto.builder()
                .id(theseHit.id())
                .titrePrincipal(these.getTitrePrincipal())
                .nnt(these.getNnt())
                .doi(these.getDoi())
                .numSujet(these.getNumSujet())
                .numSujetSansS(these.getNumSujetSansS())
                .dateSoutenance(these.getDateSoutenance())
                .datePremiereInscriptionDoctorat(these.getDatePremiereInscriptionDoctorat())
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
                .dateCines(these.getDateCines())
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
