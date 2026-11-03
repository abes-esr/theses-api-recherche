package fr.abes.thesesapirecherche.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;

public class FlexibleZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("uuuu-MM-dd'T'HH:mm:ss")
            .optionalStart().appendOffset("+HH:mm", "Z").optionalEnd()
            .optionalStart().appendPattern("X").optionalEnd()
            .toFormatter();

    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        if (text == null || text.isBlank()) return null;

        TemporalAccessor parsed = FORMATTER.parseBest(text, ZonedDateTime::from, LocalDateTime::from);
        if (parsed instanceof ZonedDateTime zdt) return zdt;
        return ((LocalDateTime) parsed).atZone(ZoneOffset.UTC); // default zone when no offset
    }
}