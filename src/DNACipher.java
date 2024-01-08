import java.util.Map;
import java.util.Scanner;

public class DNACipher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your plaintext:");
        String plaintext = scanner.nextLine();

        // Step 1: Trimming
        String trimmedText = TextPreprocessor.trimText(plaintext);

        // Step 2: Convert to Binary
        String binaryText = BinaryConverter.textToBinary(trimmedText);

        // Step 3: Convert to DNA
        String dnaText = DNAConverter.binaryToDNA(binaryText);

        // Step 4: Convert to Amino Acid
        AminoAcidConverter aminoAcidConverter = new AminoAcidConverter();
        String aminoAcidText = aminoAcidConverter.dnaToAminoAcid(dnaText);
        String indexSequence = aminoAcidConverter.getIndexSequence();

        // Get Hill Cipher Key from User


        // Output other details
        System.out.println("Trimmed Text: " + trimmedText);
        System.out.println("Binary Text: " + binaryText);
        System.out.println("DNA Text: " + dnaText);
        System.out.println("Amino Acid Encrypted Text: " + aminoAcidText);
        System.out.println("Codon Index Sequence: " + indexSequence);



        System.out.println("Enter 4 numbers for the Hill Cipher Key Matrix (2x2):");
        int[][] keyMatrix = new int[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                keyMatrix[i][j] = scanner.nextInt();
            }
        }
        try {
            HillCipher hillCipher = new HillCipher(keyMatrix);
            // Encryption
            Map.Entry<String, Boolean> encryptedData = hillCipher.encrypt(aminoAcidText);
            String hillEncryptedText = encryptedData.getKey();
            boolean addedX = encryptedData.getValue();
            System.out.println("Hill Cipher Encrypted Text: " + hillEncryptedText);
            // Decryption
            String hillDecryptedText = hillCipher.decrypt(hillEncryptedText, addedX);
            // Convert back from Amino Acid to DNA using the index sequence
            String decryptedDnaText = aminoAcidConverter.aminoAcidToDNA(hillDecryptedText, indexSequence);
            System.out.println("aminoAcidConverter.aminoAcidToDNA Text: " + decryptedDnaText);
            // Convert DNA back to Binary
            String decryptedBinaryText = DNAConverter.dnaToBinary(decryptedDnaText);
            System.out.println(" DNAConverter.dnaToBinary decryptedBinary : " + decryptedBinaryText);
            // Convert Binary back to Text
            String decryptedPlainText = BinaryConverter.binaryToText(decryptedBinaryText);
            System.out.println("BinaryConverter.binaryToText : " + decryptedBinaryText);

            System.out.println("Hill Cipher Decrypted Text: " + decryptedPlainText);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            // Exit or ask for new input
            scanner.close();
            return;
        }
        scanner.close();
    }
}
