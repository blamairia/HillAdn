public class BinaryConverter {
    public static String textToBinary(String text) {
        StringBuilder binary = new StringBuilder();
        for (char character : text.toCharArray()) {
            binary.append(String.format("%8s", Integer.toBinaryString(character)).replace(' ', '0'));
        }
        return binary.toString();
    }
    public static String binaryToText(String binary) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 8) {
            int endIndex = Math.min(i + 8, binary.length());
            String byteStr = binary.substring(i, endIndex);
            if (byteStr.length() < 8) {
                // Pad the last byte if it's less than 8 bits
                byteStr = String.format("%-8s", byteStr).replace(' ', '0');
            }
            int charCode = Integer.parseInt(byteStr, 2);
            text.append((char) charCode);
        }
        return text.toString();
    }
}
