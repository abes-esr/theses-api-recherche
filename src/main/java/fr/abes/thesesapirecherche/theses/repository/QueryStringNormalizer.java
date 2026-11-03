package fr.abes.thesesapirecherche.theses.repository;


import java.util.Objects;


final class QueryStringNormalizer {

    public static String normalize(String input) {
        Objects.requireNonNull(input, "input must not be null");

        StringBuilder out = new StringBuilder(input.length());
        boolean inQuotes = false;
        boolean pendingSpace = false;
        int openQuoteIndex = -1;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '\\') {
                // Keep an escaped character verbatim; drop a lone trailing backslash
                if (i + 1 < input.length()) {
                    flushSpace(out, pendingSpace);
                    pendingSpace = false;
                    out.append(c).append(input.charAt(++i));
                }
                continue;
            }

            if (c == '"') {
                flushSpace(out, pendingSpace);
                pendingSpace = false;
                inQuotes = !inQuotes;
                if (inQuotes) {
                    openQuoteIndex = out.length();
                }
                out.append(c);
            } else if (!inQuotes && Character.isWhitespace(c)) {
                pendingSpace = true;            // collapse runs, trim edges
            } else {
                flushSpace(out, pendingSpace);
                pendingSpace = false;
                out.append(c);
            }
        }

        if (inQuotes) {
            out.deleteCharAt(openQuoteIndex);   // unbalanced quote: drop it
        }
        String output = out.toString();
        return cleanDoi(output);
    }

    private static void flushSpace(StringBuilder out, boolean pendingSpace) {
        if (pendingSpace && !out.isEmpty()) {
            out.append(' ');
        }
    }

    /**
     * Supprime "https://doi.org/" ou "doi.org/" de la query pour pouvoir matcher avec
     * le champ doi qui ne contient que le doi simple
     * @param doiUrl
     * @return ce qu'il y a après doi.org/
     */
    public static String cleanDoi(String doiUrl) {
        if (doiUrl == null) {
            return null;
        }
        return doiUrl.replace("https://doi.org/", "")
                .replace("http://doi.org/", "")
                .replace("doi.org/", "");
    }
}