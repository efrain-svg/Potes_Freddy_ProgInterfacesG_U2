package vista.i18n;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class I18n {

    public static final String LANG_ES = "es";
    public static final String LANG_EN = "en";
    public static final String LANG_PT = "pt";

    private Locale locale;
    private ResourceBundle bundle;

    public I18n() {
        setLanguage(LANG_ES);
    }

    public void setLanguage(String languageCode) {
        if (LANG_EN.equalsIgnoreCase(languageCode)) {
            locale = Locale.ENGLISH;
        } else if (LANG_PT.equalsIgnoreCase(languageCode)) {
            locale = Locale.forLanguageTag("pt-BR");
        } else {
            locale = Locale.forLanguageTag("es-EC");
        }
        bundle = cargarBundle(locale);
    }

    private ResourceBundle cargarBundle(Locale target) {
        try {
            return ResourceBundle.getBundle("i18n.messages", target);
        } catch (MissingResourceException ex) {
            String path = System.getProperty("user.dir")
                + File.separator + "src"
                + File.separator + "i18n"
                + File.separator + "messages_" + target.getLanguage() + ".properties";
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8)) {
                return new PropertyResourceBundle(reader);
            } catch (IOException ioEx) {
                throw ex;
            }
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public String t(String key) {
        return bundle.getString(key);
    }

    public String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale);
        return formatter.format(date);
    }
}



