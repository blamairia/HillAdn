import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AminoAcidConverter {
    private Map<String, List<String>> codonMap = new HashMap<>();
    private StringBuilder indexSequence = new StringBuilder();

    public AminoAcidConverter() {
        initializeCodonMap();
    }

    private void initializeCodonMap() {
        // Your codon mappings
        codonMap.put("A", Arrays.asList("GCU", "GCC", "GCA", "GCG"));
        codonMap.put("B", Arrays.asList("UAA", "UAG", "UGA"));
        codonMap.put("C", Arrays.asList("UGU", "UGC"));
        codonMap.put("D", Arrays.asList("GAU", "GAC"));
        codonMap.put("E", Arrays.asList("GAA", "GAG"));
        codonMap.put("F", Arrays.asList("uuu", "uuc"));
        codonMap.put("G", Arrays.asList("GGU", "GGC", "GGA", "GGG"));
        codonMap.put("H", Arrays.asList("CAU", "CAC"));
        codonMap.put("I", Arrays.asList("AUU", "AUC", "AUA"));
        codonMap.put("K", Arrays.asList("AAA", "AAG"));
        codonMap.put("L", Arrays.asList("cuu", "CUC", "CUA", "CUG"));
        codonMap.put("M", Arrays.asList("AUG")); // Assuming ".\\I" is "M"
        codonMap.put("N", Arrays.asList("AAU", "AAC"));
        codonMap.put("O", Arrays.asList("UUA", "UUG")); // Assuming "0" is "O"
        codonMap.put("P", Arrays.asList("CCU", "CCC", "CCA", "CCG"));
        codonMap.put("Q", Arrays.asList("CAA", "CAG"));
        codonMap.put("R", Arrays.asList("CGU", "CGC", "CGA", "CGG"));
        codonMap.put("S", Arrays.asList("ucu", "ucc", "UCA", "UCG"));
        codonMap.put("T", Arrays.asList("ACU", "ACC", "ACA", "ACG"));
        codonMap.put("U", Arrays.asList("AGA", "AGG"));
        codonMap.put("V", Arrays.asList("GUU", "GUC", "GUA", "GUG"));
        codonMap.put("W", Arrays.asList("UGG"));
        codonMap.put("X", Arrays.asList("AGU", "AGC"));
        codonMap.put("Y", List.of("UAU"));
        codonMap.put("Z", Arrays.asList("UAC"));
    }


    public String dnaToAminoAcid(String dna) {
        indexSequence.setLength(0); // Reset the indexSequence for new conversion
        StringBuilder aminoAcidSequence = new StringBuilder();

        for (int i = 0; i < dna.length(); i += 3) {
            if (i + 3 > dna.length()) {
                // Append incomplete codons directly to the index sequence
                indexSequence.append(dna.substring(i));
                break; // End processing
            }

            String codon = dna.substring(i, i + 3);
            boolean found = false;
            for (Map.Entry<String, List<String>> entry : codonMap.entrySet()) {
                if (entry.getValue().contains(codon)) {
                    aminoAcidSequence.append(entry.getKey());
                    indexSequence.append(entry.getValue().indexOf(codon)); // Numeric index for recognized codon
                    found = true;
                    break;
                }
            }
            if (!found) {
                aminoAcidSequence.append("-"); // Placeholder for skipped codon
                indexSequence.append(codon); // Retain the original codon in the sequence
            }
        }

        return aminoAcidSequence.toString();
    }

    public String getIndexSequence() {
        return indexSequence.toString();
    }

    public String aminoAcidToDNA(String aminoAcidSequence, String indexSequence) {
        StringBuilder dnaSequence = new StringBuilder();
        int aminoAcidPointer = 0;  // Pointer for amino acid sequence

        for (int i = 0; i < indexSequence.length(); i++) {
            char indexChar = indexSequence.charAt(i);

            if (Character.isDigit(indexChar)) {
                // It's a recognized codon
                if (aminoAcidPointer < aminoAcidSequence.length()) {
                    String aminoAcid = String.valueOf(aminoAcidSequence.charAt(aminoAcidPointer));
                    int codonIndex = Character.getNumericValue(indexChar);
                    List<String> codons = codonMap.get(aminoAcid);
                    if (codons != null && codonIndex >= 0 && codonIndex < codons.size()) {
                        dnaSequence.append(codons.get(codonIndex));
                    } else {
                        throw new IllegalArgumentException("Invalid amino acid or codon index: " + aminoAcid + ", " + codonIndex);
                    }
                    aminoAcidPointer++;  // Move to the next amino acid
                }
            } else {
                // It's an unrecognized or incomplete codon
                if (i + 2 < indexSequence.length() && !Character.isDigit(indexSequence.charAt(i + 1))) {
                    String codon = indexSequence.substring(i, i + 3);
                    dnaSequence.append(codon);
                    i += 2;  // Skip the next two characters as they are part of the codon
                } else {
                    // Single or double character unrecognized codon
                    dnaSequence.append(indexChar);
                }
            }
        }

        return dnaSequence.toString();
    }

}


