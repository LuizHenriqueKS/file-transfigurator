package br.zul.filetransfigurator.util;

public class LimitUtils {

    private LimitUtils() {
    }

    public static String leftLimit(String text, int maxLength) {
        if (text.length() > maxLength) {
            int offset = text.length() - maxLength;
            return text.substring(offset);
        }
        return text;
    }

}
