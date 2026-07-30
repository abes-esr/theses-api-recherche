package fr.abes.thesesapirecherche.personnes.converters;

import fr.abes.thesesapirecherche.personnes.dto.TheseResponseDto;
import fr.abes.thesesapirecherche.personnes.model.ThesePersonne;


/**
 * Convertisseur de format pour les objets Thèses
 */
public class TheseMapper {

    EtablissementMapper etablissementMapper = new EtablissementMapper();
    ThesePersonneLiteMapper personneMapper = new ThesePersonneLiteMapper();

    SujetRameauMapper sujetRameauMapper = new SujetRameauMapper();

    //remplit les champs du dto selon un objet java (reçu par ES)
    protected void fillNormalFields(ThesePersonne these, TheseResponseDto dto) {
        
                dto.setId(these.getId());
                dto.setTitre(these.getTitre());
                dto.setTitres(these.getTitres());
                dto.setResumes(these.getResumes());
                dto.setRole(these.getRole());
                dto.setDiscipline(these.getDiscipline());
                dto.setStatus(these.getStatus());
                dto.setSource(these.getSource());
                dto.setEtablissement_soutenance(etablissementMapper.etablissementToDto(these.getEtablissement_soutenance()));
                dto.setEtablissements_cotutelle(etablissementMapper.etablissementsToDto(these.getEtablissements_cotutelle()));
                dto.setDate_soutenance(these.getDate_soutenance());
                dto.setDate_inscription(these.getDate_inscription());
                dto.setAuteurs(personneMapper.personnesLiteToDto(these.getAuteurs()));
                dto.setDirecteurs(personneMapper.personnesLiteToDto(these.getDirecteurs()));
                dto.setSujets_rameau(sujetRameauMapper.sujetsRameauToDto(these.getSujets_rameau()));
                dto.setSujets(these.getSujets());
                dto.setOaiSetNames(these.getOaiSetNames());
  
    }

    public TheseResponseDto theseNormalToDto(ThesePersonne these) {
        TheseResponseDto dto = TheseResponseDto.builder().build();
        fillNormalFields(these, dto);
        return dto;
    }

    
}
