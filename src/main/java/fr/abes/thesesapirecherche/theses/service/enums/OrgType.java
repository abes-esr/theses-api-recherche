package fr.abes.thesesapirecherche.theses.service.enums;

public enum OrgType {
    ETABLISSEMENT_COTUTELLE("etabsCotutellePpn"),
    ETABLISSEMENT_SOUTENANCE("etabSoutenancePpn"),
    ECOLE_DOCTORALE("ecolesDoctoralesPpn"),
    PARTENAIRE_RECHERCHE("partenairesRecherchePpn");

    public final String fieldName;

    OrgType(String fieldName) {
        this.fieldName = fieldName;
    }
}
