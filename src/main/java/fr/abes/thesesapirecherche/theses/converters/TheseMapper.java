package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.dto.TheseDto;
import fr.abes.thesesapirecherche.theses.model.These;

import fr.abes.thesesapirecherche.theses.service.DateFormatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static fr.abes.thesesapirecherche.theses.converters.TheseMappingHelper.formatKeywords;

@Component("TheseTheseMapper")
@RequiredArgsConstructor
public class TheseMapper {
    PersonneRefMapper personneMapper = new PersonneRefMapper();
    OrganismeMapper organismeMapper = new OrganismeMapper();
    private final DateFormatService dateFormatService;

    public TheseDto theseToDto(These these) {
        return TheseDto.builder()
                .titrePrincipal(these.getTitrePrincipal())
                .nnt(these.getNnt())
                .doi(these.getDoi())
                .numSujet(these.getNumSujet())
                .dateSoutenance(dateFormatService.formatDate(these.getDateSoutenance()))
                .datePremiereInscriptionDoctorat(dateFormatService.formatDate(these.getDatePremiereInscriptionDoctorat()))
                .discipline(these.getDiscipline())
                .titres(these.getTitres())
                .resumes(these.getResumes())
                .etabSoutenance(organismeMapper.organismeToDto(these.getEtabSoutenance()))
                .codeEtab(these.getCodeEtab())
                .etabCotutelle(organismeMapper.organismesToDto(these.getEtabsCotutelle()))
                .partenairesRecherche(organismeMapper.organismesToDto(these.getPartenairesRecherche()))
                .mapSujets(formatKeywords(these))
                .membresJury(personneMapper.personnesToDto(these.getMembresJury()))
                .rapporteurs(personneMapper.personnesToDto(these.getRapporteurs()))
                .auteurs(personneMapper.personnesToDto(these.getAuteurs()))
                .directeurs(personneMapper.personnesToDto(these.getDirecteurs()))
                .cas(these.getCas())
                .langues(these.getLangues())
                .accessible(these.getAccessible())
                .ecolesDoctorales(organismeMapper.organismesToDto(these.getEcolesDoctorales()))
                .presidentJury(personneMapper.personneToDto(these.getPresidentJury()))
                .source(these.getSource())
                .status(these.getStatus())
                .isSoutenue(these.getIsSoutenue())
                .build();
    }
}