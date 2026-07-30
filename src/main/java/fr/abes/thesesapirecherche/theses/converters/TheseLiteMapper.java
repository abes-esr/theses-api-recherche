package fr.abes.thesesapirecherche.theses.converters;

import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.personnes.dto.ThesePersonneLiteResponseDto;
import fr.abes.thesesapirecherche.theses.dto.TheseLiteResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;

public class TheseLiteMapper {

    protected void fillLiteFields(Hit<These> theseHit, TheseLiteResponseDto dto) {
                These these = theseHit.source();

                dto.setId(theseHit.id());;
                dto.setTitrePrincipal(these.getTitrePrincipal());
                dto.setTitreEN(these.getTitres().get("en") != null ? these.getTitres().get("en") : "");
                dto.setEtabSoutenanceN(these.getEtabSoutenanceN());
                dto.setEtabSoutenancePpn(these.getEtabSoutenancePpn());
                dto.setDateSoutenance(these.getDateSoutenance());
                dto.setDatePremiereInscriptionDoctorat(these.getDatePremiereInscriptionDoctorat());
                dto.setDateCines(these.getDateCines());
                dto.setAuteurs(TheseMappingHelper.personnesToDto(these.getAuteurs()));
                dto.setDirecteurs(TheseMappingHelper.personnesToDto(these.getDirecteurs()));
                dto.setRapporteurs(TheseMappingHelper.personnesToDto(these.getRapporteurs()));
                dto.setExaminateurs(TheseMappingHelper.personnesToDto(these.getMembresJury()));
                dto.setPresident(TheseMappingHelper.personneToDto(these.getPresidentJury()));
                dto.setNnt(these.getNnt());
                dto.setDoi(these.getDoi());
                dto.setNumSujetSansS(these.getNumSujetSansS());
                dto.setDiscipline(these.getDiscipline());
                dto.setStatus(these.getStatus());
                dto.setCas(these.getCas());
                dto.setEcolesDoctorale(TheseMappingHelper.organismesToDto(these.getEcolesDoctorales()));
                dto.setPartenairesDeRecherche(TheseMappingHelper.organismesToDto(these.getPartenairesRecherche()));
                dto.setSujets(these.getSujets());
                dto.setSujetsRameau(these.getSujetsRameau());
                dto.setOaiSetNames(these.getOaiSetNames());
    }

    public TheseLiteResponseDto theseLiteToDto(Hit<These> theseHit) {
        TheseLiteResponseDto dto = TheseLiteResponseDto.builder().build();
        fillLiteFields(theseHit, dto);
        return dto;
    }
}
