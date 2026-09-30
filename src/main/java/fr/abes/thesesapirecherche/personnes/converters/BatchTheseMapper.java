package fr.abes.thesesapirecherche.personnes.converters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.abes.thesesapirecherche.personnes.dto.batch.BatchTheseResponseDto;
import fr.abes.thesesapirecherche.personnes.model.ThesePersonne;

public class BatchTheseMapper {

    EtablissementMapper etablissementMapper = new EtablissementMapper();
    ThesePersonneLiteMapper personneMapper = new ThesePersonneLiteMapper();

    SujetRameauMapper sujetRameauMapper = new SujetRameauMapper();

    /**
     * Transforme une thèse en web dto
     *
     * @param these
     * @return
     */
    public BatchTheseResponseDto theseToBatchDto(ThesePersonne these) {
        return BatchTheseResponseDto.builder()
                .id(these.getId())
                .titre(these.getTitre())
                .titres(these.getTitres())
                .resumes(these.getResumes())
                .role(these.getRole())
                .discipline(these.getDiscipline())
                .status(these.getStatus())
                .source(these.getSource())
                .etablissement_soutenance(etablissementMapper.etablissementToDto(these.getEtablissement_soutenance()))
                .etablissements_cotutelle(etablissementMapper.etablissementsToDto(these.getEtablissements_cotutelle()))
                .date_soutenance(these.getDate_soutenance())
                .date_inscription(these.getDate_inscription())
                .auteurs(personneMapper.personnesLiteToDto(these.getAuteurs()))
                .directeurs(personneMapper.personnesLiteToDto(these.getDirecteurs()))
                .sujets_rameau(sujetRameauMapper.sujetsRameauToDto(these.getSujets_rameau()))
                .sujets(these.getSujets())
                .oaiSetNames(these.getOaiSetNames())
                .numSujetSansS(these.getNumSujetSansS())
                .doi(these.getDoi())
                .nnt(these.getNnt())
                .codeEtab(these.getCodeEtab())
                .dateCines(these.getDateCines())
                .langues(these.getLangues())
                .accessible(these.getAccessible())
                .cas(these.getCas())
                .build();
    }

    /**
     * Transforme une liste de thèse en web dto
     *
     * @param theses
     * @return
     */
    public Map<String, List<BatchTheseResponseDto>> thesesToBatchDto(List<ThesePersonne> theses) {
        Map<String, List<BatchTheseResponseDto>> results = new HashMap<>();
        if (theses != null) {
            for (ThesePersonne item : theses) {
                if (results.containsKey(item.getRole())) {
                    results.get(item.getRole()).add(theseToBatchDto(item));
                } else {
                    List<BatchTheseResponseDto> list = new ArrayList<>();
                    list.add(theseToBatchDto(item));
                    results.put(item.getRole(), list);
                }
            }
        }

        //On tri les thèses par date
        for (String role : results.keySet()) {
            Collections.sort(results.get(role), Comparator.comparing(BatchTheseResponseDto::getDate_soutenanceTri).reversed());
        }
        return results;
    }
}
