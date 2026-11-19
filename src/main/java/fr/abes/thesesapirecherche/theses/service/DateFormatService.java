package fr.abes.thesesapirecherche.theses.service;

import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class DateFormatService {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");   // immutable & thread-safe

    private static final ZoneId ZONE = ZoneId.of("Europe/Paris"); // or ZoneOffset.UTC, be explicit

    public String formatDate(Date date) {
        return date == null
                ? null
                : DATE_FORMATTER.format(date.toInstant().atZone(ZONE));
    }
}
