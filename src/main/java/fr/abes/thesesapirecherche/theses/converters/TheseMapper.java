package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.dto.TheseResponseDto;
import fr.abes.thesesapirecherche.theses.model.Sujet;
import fr.abes.thesesapirecherche.theses.model.SujetsRameau;
import fr.abes.thesesapirecherche.theses.model.SujetsToMap;
import fr.abes.thesesapirecherche.theses.model.These;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheseMapper {
    ThesePersonneMapper personneMapper = new ThesePersonneMapper();
    OrganismeMapper organismeMapper = new OrganismeMapper();

    private Map<String, List<SujetsToMap>> formatKeywords(These these) {
        Map<String, List<SujetsToMap>> mapSujets = new HashMap<>();
        for (Sujet s : these.getSujets()) {
            List<SujetsToMap> l = new ArrayList<>();
            if (mapSujets.get(s.getLangue()) != null) l = mapSujets.get(s.getLangue());
            l.add(new SujetsToMap(s.getLibelle(), SujetsToMap.Type.sujet, s.getLibelle()));
            mapSujets.put(s.getLangue(), l);
        }
        for (SujetsRameau s : these.getSujetsRameau()) {
            List<SujetsToMap> l = new ArrayList<>();
            if (mapSujets.get("fr") != null) l = mapSujets.get("fr");
            l.add(0, new SujetsToMap(s.getLibelle(), SujetsToMap.Type.sujetsRameau, "\"" + s.getLibelle() + "\" ET " + s.getPpn()));
            mapSujets.put("fr", l);
        }

        return mapSujets;
    }

    public TheseResponseDto theseToDto(These these) {
        return TheseResponseDto.builder()
                .titrePrincipal(these.getTitrePrincipal())
                .nnt(these.getNnt())
                .numSujet(these.getNumSujet())
                .dateSoutenance(these.getDateSoutenance())
                .datePremiereInscriptionDoctorat(these.getDatePremiereInscriptionDoctorat())
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

    public List<TheseResponseDto> thesesToDto(List<These> personnes) {
        List<TheseResponseDto> results = new ArrayList<>();
        for (These item : personnes) {
            results.add(theseToDto(item));
        }
        return results;
    }
}
