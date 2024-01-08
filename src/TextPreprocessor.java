public class TextPreprocessor {
    public static String trimText(String text) {
        return text.replaceAll("\\s+", "").toUpperCase();
    }
}
