package fr.abes.thesesapirecherche.personnes.converters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import fr.abes.thesesapirecherche.personnes.dto.client.ClientTheseResponseDto;
import fr.abes.thesesapirecherche.personnes.model.ThesePersonne;
import fr.abes.thesesapirecherche.theses.model.These;


@Component("PersonnesClientTheseMapper")
public class ClientTheseMapper {

    EtablissementMapper etablissementMapper = new EtablissementMapper();
    ThesePersonneLiteMapper personneMapper = new ThesePersonneLiteMapper();

    SujetRameauMapper sujetRameauMapper = new SujetRameauMapper();

    /**
     * Transforme une thèse en web dto
     *
     * @param these
     * @return
     */
    public ClientTheseResponseDto theseToClientDto(ThesePersonne these) {
        return ClientTheseResponseDto.builder()
                .id(these.getId())
                .titre(these.getTitre())
                .titres(these.getTitres())
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
                .build();
    }

    /**
     * Transforme une liste de thèse en web dto
     *
     * @param theses
     * @return
     */
    public Map<String, List<ClientTheseResponseDto>> thesesToClientDto(List<ThesePersonne> theses) {
        Map<String, List<ClientTheseResponseDto>> results = new HashMap<>();
        if (theses != null) {
            for (ThesePersonne item : theses) {
                if (results.containsKey(item.getRole())) {
                    results.get(item.getRole()).add(theseToClientDto(item));
                } else {
                    List<ClientTheseResponseDto> list = new ArrayList<>();
                    list.add(theseToClientDto(item));
                    results.put(item.getRole(), list);
                }
            }
        }

        //On tri les thèses par date
        for (String role : results.keySet()) {
            Collections.sort(results.get(role), Comparator.comparing(ClientTheseResponseDto::getDate_soutenanceTri).reversed());
        }
        return results;
    }



    /**
     * Enrichit le dto déjà crée avec une thèse de l'index theses (informations en +)
     *
     * @param these
     * @return
     */
    public void addTheseFieldsToDto(ClientTheseResponseDto theseDto, These these) {
        theseDto.setNnt(these.getNnt());
        theseDto.setCas(these.getCas());
        theseDto.setLangues(these.getLangues());
        theseDto.setAccessible(these.getAccessible());
        theseDto.setCodeEtab(these.getCodeEtab());
        theseDto.setDateCines(these.getDateCines());
        theseDto.setNumSujetSansS(these.getNumSujetSansS());
        theseDto.setOaiSetNames(these.getOaiSetNames());
        theseDto.setDoi(these.getDoi());
    }
    
}
