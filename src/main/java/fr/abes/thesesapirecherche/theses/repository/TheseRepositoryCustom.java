package fr.abes.thesesapirecherche.theses.repository;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import fr.abes.thesesapirecherche.dto.Facet;
import fr.abes.thesesapirecherche.theses.model.These;

import java.io.IOException;
import java.util.List;

/**
 * Ceci est un fragment d'un repository Spring Data
 */
public interface TheseRepositoryCustom {
    SearchResponse<These> rechercher(String chaine, Integer debut, Integer nombre, String tri, String filtres);
    String rechercherOrganisme(String ppn);
    SearchResponse<These> rechercherAvecFacets(String chaine, Integer debut, Integer nombre,
                                                      String tri, String filtres);
    SearchResponse<Void> getSuggestions(String q) throws IOException;
    List<Facet> facets(String chaine, String filtres) throws Exception;
    long getStatsByStatus(Status status) throws IOException;
}
