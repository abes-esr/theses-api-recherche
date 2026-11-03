package fr.abes.thesesapirecherche.theses.repository;

public enum Status {
    EN_COURS("enCours"),
    SOUTENUE("soutenue");

    public final String val;

    Status(String val) {
        this.val = val;
    }
}
