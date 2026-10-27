package fr.abes.thesesapirecherche.exception;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(String msg) {
        super(msg);
    }
}
