package fr.abes.thesesapirecherche.theses.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Component
public class DbRequests {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List getMailAddress(String ppnEtab, String source) {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        TimeZone.setDefault(timeZone);

        List<Map<String, Object>> res = jdbcTemplate.queryForList("SELECT EMAIL FROM COMPTE WHERE PPN = ? AND LOWER(SOURCE) = ?", ppnEtab, source.toLowerCase());
        List<String> to = new ArrayList<>();
        for (Map<String, Object> m : res
        ) {
            to.add(m.get("EMAIL").toString());
        }
        return to;
    }

    public String checkIfNNT(String numSujet) {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        TimeZone.setDefault(timeZone);

        String nnt = null;

        nnt = DataAccessUtils.singleResult(jdbcTemplate.queryForList("SELECT NNT FROM DOCUMENT WHERE NUMSUJET = ?", String.class, numSujet));

        return nnt;
    }
}
