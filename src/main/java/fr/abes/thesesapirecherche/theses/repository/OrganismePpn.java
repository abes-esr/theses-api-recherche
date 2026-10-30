package fr.abes.thesesapirecherche.theses.repository;

import java.util.List;

public record OrganismePpn(
        String etabSoutenancePpn,
        List<String> etabsCotutellePpn,
        List<String> partenairesRecherchePpn,
        List<String> ecolesDoctoralesPpn
) {
    public String firstMatch() {
        if (etabSoutenancePpn != null) return etabSoutenancePpn;
        if (etabsCotutellePpn != null && !etabsCotutellePpn.isEmpty()) return etabsCotutellePpn.get(0);
        if (partenairesRecherchePpn != null && !partenairesRecherchePpn.isEmpty()) return partenairesRecherchePpn.get(0);
        return ecolesDoctoralesPpn != null && !ecolesDoctoralesPpn.isEmpty() ? ecolesDoctoralesPpn.get(0) : null;
    }
}