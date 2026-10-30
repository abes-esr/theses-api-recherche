package fr.abes.thesesapirecherche.theses.repository;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import fr.abes.thesesapirecherche.theses.model.These;

/**
 * Ceci est un fragment d'un repository Spring Data
 */
public interface TheseRepositoryCustom {
    SearchResponse<These> rechercher(String chaine, Integer debut, Integer nombre, String tri, String filtres);
    String rechercherOrganisme(String ppn);
}
