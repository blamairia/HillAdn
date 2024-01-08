public class DNAConverter {
    public static String binaryToDNA(String binary) {
        StringBuilder dna = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 2) {
            String bitPair = binary.substring(i, i + 2);
            switch (bitPair) {
                case "00":
                    dna.append("A");
                    break;
                case "01":
                    dna.append("C");
                    break;
                case "10":
                    dna.append("G");
                    break;
                case "11":
                    dna.append("T");
                    break;
            }
        }
        return dna.toString();
    }

    public static String dnaToBinary(String dna) {
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < dna.length(); i++) {
            char nucleotide = dna.charAt(i);
            switch (nucleotide) {
                case 'A':
                    binary.append("00");
                    break;
                case 'C':
                    binary.append("01");
                    break;
                case 'G':
                    binary.append("10");
                    break;
                case 'T':
                    binary.append("11");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid DNA nucleotide: " + nucleotide);
            }
        }
        return binary.toString();
    }
}
