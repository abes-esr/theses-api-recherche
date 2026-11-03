package fr.abes.thesesapirecherche.theses.repository;

import fr.abes.thesesapirecherche.theses.model.These;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Collection;
import java.util.List;

public interface TheseRepository extends ElasticsearchRepository<These, String>, TheseRepositoryCustom {

    List<These> findByEtabSoutenancePpnAndStatus(String etabSoutenancePpn, String status);
    List<These> findByEtabsCotutellePpnAndStatus(String etabsCocutellePpn, String status);
    List<These> findByPartenairesRecherchePpnAndStatus(String partenairesRecherchePpn, String status);
    List<These> findByEcolesDoctoralesPpnAndStatus(String partenairesRecherchePpn, String status);
}
