package fr.abes.thesesapirecherche.personnes.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.personnes.builder.SearchPersonneQueryBuilder;
import fr.abes.thesesapirecherche.personnes.converters.ThesePersonneEnhancedMapper;
import fr.abes.thesesapirecherche.personnes.dto.PersonneComputedFields;
import fr.abes.thesesapirecherche.personnes.dto.PersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.model.Personne;
import fr.abes.thesesapirecherche.personnes.model.ThesePersonne;
import fr.abes.thesesapirecherche.theses.model.These;

@Service
public class PersonnesService {
    
    @Autowired
    private final SearchPersonneQueryBuilder searchPersonneQueryBuilder;

    @Autowired
    private final ThesePersonneEnhancedMapper theseEnhancedMapper;

    
    public PersonnesService(SearchPersonneQueryBuilder searchPersonneQueryBuilder, ThesePersonneEnhancedMapper theseEnhancedMapper) {
        this.searchPersonneQueryBuilder = searchPersonneQueryBuilder;
        this.theseEnhancedMapper = theseEnhancedMapper;
    }



    public PersonneResponseDto getPersonne(String id, Boolean viewFull) throws Exception{
        PersonneResponseDto res = PersonneResponseDto.builder().build();


        // on récupère la personne via ElasticSearch
        SearchResponse<Personne> response = searchPersonneQueryBuilder.getPersonne(id);

        // on fait le mapping ici (à voir si on peut pas réutiliser des méthodes des mappers, là je peux pas trop à cause du "setTheses" qui est différent selon viewFull)
        Personne p = response.hits().hits().get(0).source();
        res.setId(response.hits().hits().get(0).id());
        res.setNom(p.getNom());
        res.setPrenom(p.getPrenom());
        res.setHasIdref(p.getHasIdref());

        res.setRoles(PersonneComputedFields.calculerStatistiquesRoles(p.getRoles()));
        res.setMotsCles(PersonneComputedFields.calculerMotsCles(p.getTheses()));


        // si c'est viewFull, il faut récupérer des infos qui ne se trouvent que dans l'index theses, puis faut faire le mapping
        if(viewFull){
            // on récupère d'abord les ids des thèses sur l'index theses (pour avoir toutes les informations exhaustives)
            List<String> theseIds = p.getTheses().stream()
                                                 .map(ThesePersonne::getId)
                                                 .toList();

            // récupération des thèses complètes
            SearchResponse<These> thesesHit = searchPersonneQueryBuilder.getThesesByIds(theseIds);


            // on indexe les thèses par id pour les retrouver rapidement
            Map<String, These> thesesById =
                thesesHit.hits().hits().stream()
                    .collect(Collectors.toMap(
                        Hit::id,
                        hit -> hit.source()
                    ));

                    thesesById.forEach((idT, these) -> {
                        System.out.println("clé = " + idT);
                        System.out.println("valeur = " + these);
                    });
                        
            // on enrichit les ThesesPersonne déjà récupérées lors de la 1ere requête ES
            for (ThesePersonne thesePersonne : p.getTheses()) {

                These these = thesesById.get(thesePersonne.getId());

                if (these == null) {
                    continue;
                }
                
                //mapping manuel de these.model.These vers personnes.model.ThesePersonne
                // TODO: à voir s'il faudrait pas mettre ce bout de code dans un mapper à part, mais bon ça ferait un mapper entre 2 model différents et on n'en a besoin que ici... je sais pas à voir
                thesePersonne.setNnt(these.getNnt());
                thesePersonne.setDoi(these.getDoi());
                thesePersonne.setNumSujetSansS(these.getNumSujetSansS());
                thesePersonne.setCodeEtab(these.getCodeEtab());
                thesePersonne.setDateCines(these.getDateCines());
                thesePersonne.setLangues(these.getLangues());
                thesePersonne.setAccessible(these.getAccessible());
                thesePersonne.setCas(these.getCas());

              
            }
             
        }

        //mapping de ThesePersonne (model) vers TheseEnhanced (DTO)
        res.setTheses(theseEnhancedMapper.thesesToEnhancedDto(p.getTheses()));

        return res;

    }




}
