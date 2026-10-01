package fr.abes.thesesapirecherche.theses.converters;

import java.util.ArrayList;
import java.util.List;

import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseLiteResponseDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;

public class ClientTheseMapper {
    

    public ClientTheseResponseDto theseToClientDto(These these) {
        return ClientTheseResponseDto.builder()
                .titrePrincipal(these.getTitrePrincipal())
                .nnt(these.getNnt())
                .doi(these.getDoi())
                .numSujet(these.getNumSujet())
                .numSujetSansS(these.getNumSujetSansS())
                .dateSoutenance(these.getDateSoutenance())
                .datePremiereInscriptionDoctorat(these.getDatePremiereInscriptionDoctorat())
                .discipline(these.getDiscipline())
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
                .dateSoutenance(these.getDateSoutenance())
                .datePremiereInscriptionDoctorat(these.getDatePremiereInscriptionDoctorat())
                .discipline(these.getDiscipline())
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
