package fr.abes.thesesapirecherche.theses.repository;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public record GroupeOrganisme(
        OrganismeGeneric etabSoutenance,
        List<OrganismeGeneric> etabsCotutelle,
        List<OrganismeGeneric> partenairesRecherche,
        List<OrganismeGeneric> ecolesDoctorales
) {
    /**
     * Cherche, dans le record {@link GroupeOrganisme}, le 1er ppn égale au paramètre et renvoie le nom correspondant, que se soit
     * un établissement de soutenance, un établissement de cotutelle, un partenaire de recherche ou une école doctorale.
     *
     * @param ppn d'un organisme quelconque
     * @return le nom correspondant à l'organisme avec l'id ppn, null s'il n'est pas présent dans la structure de donnée
     */
    public String firstMatch(String ppn) {
        if (etabSoutenance != null && ppn.equals(etabSoutenance.ppn())) {
            return etabSoutenance.nom();
        } else if (etabsCotutelle != null && !etabsCotutelle.isEmpty()) {
            Optional<OrganismeGeneric> res = etabsCotutelle.stream().filter(unOrganisme -> ppn.equals(unOrganisme.ppn())).findFirst();
            if (res.isPresent()) return res.get().nom();
        }
        else if (partenairesRecherche != null && !partenairesRecherche.isEmpty()) {
            Optional<OrganismeGeneric> res = partenairesRecherche.stream()
                    .filter(unOrganisme -> ppn.equals(unOrganisme.ppn()))
                    .findFirst();
            if (res.isPresent()) return res.get().nom();
        } else if (ecolesDoctorales != null && !ecolesDoctorales.isEmpty()) {
            Optional<OrganismeGeneric> res = ecolesDoctorales.stream()
                    .filter(unOrganisme -> ppn.equals(unOrganisme.ppn()))
                    .findFirst();
            if (res.isPresent()) return res.get().nom();
        }
        return null;
    }
}