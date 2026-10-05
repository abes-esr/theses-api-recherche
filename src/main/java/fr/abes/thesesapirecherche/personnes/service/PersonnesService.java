package fr.abes.thesesapirecherche.personnes.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.personnes.builder.SearchPersonneQueryBuilder;
import fr.abes.thesesapirecherche.personnes.converters.ClientTheseMapper;
import fr.abes.thesesapirecherche.personnes.converters.PersonneMapper;
import fr.abes.thesesapirecherche.personnes.converters.TheseMapper;
import fr.abes.thesesapirecherche.personnes.dto.PersonneComputedFields;
import fr.abes.thesesapirecherche.personnes.dto.PersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.TheseResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.client.ClientPersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.client.ClientTheseResponseDto;
import fr.abes.thesesapirecherche.personnes.model.Personne;
import fr.abes.thesesapirecherche.personnes.model.ThesePersonne;
import fr.abes.thesesapirecherche.theses.model.These;

@Service
public class PersonnesService {
    
    @Autowired
    private SearchPersonneQueryBuilder searchPersonneQueryBuilder;

    @Autowired
    @Qualifier("PersonnesTheseMapper")
    private TheseMapper theseMapper;

    @Autowired
    @Qualifier("PersonnesClientTheseMapper")
    private ClientTheseMapper clientTheseMapper;

    @Autowired 
    private PersonneMapper personneMapper;

    
    


    // récupère une personne d'après son id
    public PersonneResponseDto getPersonne(String id) throws Exception{
        PersonneResponseDto res = PersonneResponseDto.builder().build();


        // on récupère la personne via ElasticSearch
        SearchResponse<Personne> response = searchPersonneQueryBuilder.getPersonne(id);

        // on fait le mapping ici TODO : refactorer propre en utilisant le mapper personne
        Personne p = response.hits().hits().get(0).source();
        
        res.setId(response.hits().hits().get(0).id());
        res.setNom(p.getNom());
        res.setPrenom(p.getPrenom());
        res.setHasIdref(p.getHasIdref());

        res.setRoles(PersonneComputedFields.calculerStatistiquesRoles(p.getRoles()));
        res.setMotsCles(PersonneComputedFields.calculerMotsCles(p.getTheses()));


        //mapping de ThesePersonne (model) vers TheseDto
        res.setTheses(theseMapper.thesesToDto(p.getTheses()));

        return res;
    }




    // récupère une personne d'après son id (pour les utilisateurs qui passeront direct par l'api et pas par le front, les thèses associées à la personnes sont décrites plus exhaustivement)
    public ClientPersonneResponseDto getPersonneForClient(String id) throws Exception{

        // on récupère la personne via ElasticSearch
        SearchResponse<Personne> response = searchPersonneQueryBuilder.getPersonne(id);
        Personne p = response.hits().hits().get(0).source();

        
        ClientPersonneResponseDto personneDto = personneMapper.personneToClientDto(response.hits().hits().get(0));


        // on récupère d'abord les ids des thèses (pour plus tard aller requêter sur l'index theses)
        List<String> theseIds = p.getTheses()
                                 .stream()
                                 .map(ThesePersonne::getId)
                                 .toList();


        // récupération des thèses complètes
        SearchResponse<These> thesesHit = searchPersonneQueryBuilder.getThesesByIds(theseIds);


        // on indexe les thèses par id pour les retrouver rapidement (prck que les model "These" ont pas d'id dans leurs champs)
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
        for (List<ClientTheseResponseDto> thesesPersonneDto : personneDto.getTheses().values()) {
            for(ClientTheseResponseDto thesePersonneDto : thesesPersonneDto){

                These these = thesesById.get(thesePersonneDto.getId());
                if (these == null) { continue; }
                clientTheseMapper.addTheseFieldsToDto(thesePersonneDto, these);
            }
        }

        return personneDto;
    }

}
