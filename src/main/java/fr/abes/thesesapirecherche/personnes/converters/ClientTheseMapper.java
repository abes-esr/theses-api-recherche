package fr.abes.thesesapirecherche.personnes.converters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.abes.thesesapirecherche.personnes.dto.client.ClientTheseResponseDto;
import fr.abes.thesesapirecherche.personnes.model.ThesePersonne;
import fr.abes.thesesapirecherche.theses.model.These;


@Component("PersonnesClientTheseMapper")
public class ClientTheseMapper {

    @Autowired 
    EtablissementMapper etablissementMapper;
    
    @Autowired 
    ThesePersonneLiteMapper personneMapper;
    
    @Autowired 
    SujetRameauMapper sujetRameauMapper;

    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter DD_MM_YYYY =
            DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.of("Europe/Paris"));

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
                .etablissementSoutenance(etablissementMapper.etablissementToDto(these.getEtablissementSoutenance()))
                .etablissementsCotutelle(etablissementMapper.etablissementsToDto(these.getEtablissementsCotutelle()))
                .dateSoutenance(these.getDateSoutenance())
                .dateInscription(these.getDateInscription())
                .auteurs(personneMapper.personnesLiteToDto(these.getAuteurs()))
                .directeurs(personneMapper.personnesLiteToDto(these.getDirecteurs()))
                .sujetsRameau(sujetRameauMapper.sujetsRameauToDto(these.getSujetsRameau()))
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

        if (theses == null){
            return results;
        }


        
        for (ThesePersonne item : theses) {
            if (results.containsKey(item.getRole())) {
                results.get(item.getRole()).add(theseToClientDto(item));
            } else {
                List<ClientTheseResponseDto> list = new ArrayList<>();
                list.add(theseToClientDto(item));
                results.put(item.getRole(), list);
            }
        }
        

        //On tri les thèses par date
        for (String role : results.keySet()) {
            results.get(role)
                    .sort(Comparator.comparing(x -> getDate_soutenanceTri(x.getDateSoutenance(), x.getDateInscription()), Comparator.reverseOrder()));
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
        theseDto.setDateCines(these.getDateCines() != null ? DD_MM_YYYY.format(these.getDateCines()) : null);
        theseDto.setNumSujetSansS(these.getNumSujetSansS());
        theseDto.setOaiSetNames(these.getOaiSetNames());
        theseDto.setDoi(these.getDoi());
    }

    static public String getDate_soutenanceTri(String dateSoutenance, String dateInscription) {
        if (dateSoutenance != null)
            return dateSoutenance;
        if (dateInscription != null) {
            // On ajoute 20 ans à la date pour mettre les thèses en préparation
            // avant les thèses soutenues
            LocalDate date = LocalDate.parse(dateInscription);
            return date.plusYears(20).format(dateFormat);
        }

        // Au cas où, on met la date du jour
        return dateFormat.format(LocalDateTime.now());
    }
    
}
