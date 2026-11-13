package fr.abes.thesesapirecherche.personnes.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.abes.thesesapirecherche.dto.Facet;
import fr.abes.thesesapirecherche.personnes.dto.*;
import fr.abes.thesesapirecherche.personnes.model.RecherchePersonne;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.personnes.builder.SearchPersonneQueryBuilder;
import fr.abes.thesesapirecherche.personnes.converters.ClientTheseMapper;
import fr.abes.thesesapirecherche.personnes.converters.PersonneMapper;
import fr.abes.thesesapirecherche.personnes.dto.client.ClientPersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.client.ClientTheseResponseDto;
import fr.abes.thesesapirecherche.personnes.model.Personne;
import fr.abes.thesesapirecherche.personnes.model.ThesePersonne;
import fr.abes.thesesapirecherche.theses.model.These;

@Service
@RequiredArgsConstructor
public class PersonnesService {

    private final SearchPersonneQueryBuilder searchPersonneQueryBuilder;

    private final ClientTheseMapper clientTheseMapper;

    private final PersonneMapper personneMapper;

    public RechercheResponseDto rechercherPersonne(String query, String filtres, int debut, int nombre, String tri) throws Exception {
        SearchResponse<RecherchePersonne> res = searchPersonneQueryBuilder.rechercher(query, filtres, debut, nombre, tri);

        return RechercheResponseDto.builder()
                .personnes(res.hits().hits().stream()
                        .map(personneMapper::personnesToDto)
                        .collect(Collectors.toList()))
                .totalHits(res.hits().total().value())
                .took(res.took()).build();
    }
    


    // récupère une personne d'après son id
    public PersonneResponseDto getPersonne(String id) throws Exception{
        // on récupère la personne via ElasticSearch
        SearchResponse<Personne> response = searchPersonneQueryBuilder.getPersonne(id);
        Hit<Personne> p = response.hits().hits().get(0);

        return personneMapper.personneToDto(p);
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

    public long getStatsPersonnes() throws Exception {
        return searchPersonneQueryBuilder.getStatsPersonnes();
    }

    public SuggestionResponseDto completion(String q) throws Exception {
        return searchPersonneQueryBuilder.completion(q);
    }

    public List<Facet> facets(String query, String filtres) throws Exception {

        return searchPersonneQueryBuilder.facets(query, filtres);
    }

}
