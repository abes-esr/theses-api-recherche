package fr.abes.thesesapirecherche.theses.service;

import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;

@Service
public class DateFormatService {
    public final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public String formatDate(Supplier<Date> func) {
        Date d = func.get();
        return d == null ? null : DATE_FORMAT.format(d);
    }
}
