package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.dto.TheseDto;
import fr.abes.thesesapirecherche.theses.model.These;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.abes.thesesapirecherche.theses.service.DateFormats;
import org.springframework.stereotype.Component;

import static fr.abes.thesesapirecherche.theses.converters.TheseMappingHelper.formatKeywords;

@Component("TheseTheseMapper")
public class TheseMapper {
    PersonneRefMapper personneMapper = new PersonneRefMapper();
    OrganismeMapper organismeMapper = new OrganismeMapper();
    SujetsToMapMapper sujetsToMapMapper = new SujetsToMapMapper();

    public TheseDto theseToDto(These these) {
        return TheseDto.builder()
                .titrePrincipal(these.getTitrePrincipal())
                .nnt(these.getNnt())
                .doi(these.getDoi())
                .numSujet(these.getNumSujet())
                .dateSoutenance(DateFormats.formatDate(these::getDateSoutenance))
                .datePremiereInscriptionDoctorat(DateFormats.formatDate(these::getDatePremiereInscriptionDoctorat))
                .discipline(these.getDiscipline())
                .titres(these.getTitres())
                .resumes(these.getResumes())
                .etabSoutenance(organismeMapper.organismeToDto(these.getEtabSoutenance()))
                .codeEtab(these.getCodeEtab())
                .etabCotutelle(organismeMapper.organismesToDto(these.getEtabsCotutelle()))
                .partenairesRecherche(organismeMapper.organismesToDto(these.getPartenairesRecherche()))
                .mapSujets(formatKeywords(these).entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().stream()
                                        .map(model -> sujetsToMapMapper.sujetsToMapToDto(model))
                                        .collect(Collectors.toList())
                        )))
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