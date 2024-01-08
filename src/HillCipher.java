import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;
public class HillCipher {
    private int[][] keyMatrix;
    private int[][] inverseKeyMatrix;

    public HillCipher(int[][] keyMatrix) {
        if (keyMatrix.length != 2 || keyMatrix[0].length != 2 || keyMatrix[1].length != 2) {
            throw new IllegalArgumentException("Key matrix must be 2x2");
        }
        this.keyMatrix = keyMatrix;
        System.out.println("Initialized Hill Cipher with key matrix: " + matrixToString(keyMatrix));
        this.inverseKeyMatrix = reverseMatrix(keyMatrix);
    }

    private static String matrixToString(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : matrix) {
            sb.append("[");
            for (int value : row) {
                sb.append(value).append(" ");
            }
            sb.append("]\n");
        }
        return sb.toString().trim();
    }

    private static int[][] reverseMatrix(int[][] keyMatrix) {
        int det = (keyMatrix[0][0] * keyMatrix[1][1] - keyMatrix[0][1] * keyMatrix[1][0]) % 26;
        if (det < 0) det += 26;  // Ensure the determinant is positive

        // Check if determinant and 26 are coprime
        if (gcd(det, 26) != 1) {
            throw new IllegalArgumentException("Matrix is not invertible");
        }

        int detInverse = -1;
        for (int i = 0; i < 26; i++) {
            if ((det * i) % 26 == 1) {
                detInverse = i;
                break;
            }
        }

        int[][] reverseMatrix = new int[2][2];
        reverseMatrix[0][0] = keyMatrix[1][1] * detInverse % 26;
        reverseMatrix[0][1] = (26 - keyMatrix[0][1]) * detInverse % 26;
        reverseMatrix[1][0] = (26 - keyMatrix[1][0]) * detInverse % 26;
        reverseMatrix[1][1] = keyMatrix[0][0] * detInverse % 26;

        System.out.println("Calculated inverse matrix: " + matrixToString(reverseMatrix));
        return reverseMatrix;
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public Entry<String, Boolean> encrypt(String plaintext) {
        System.out.println("\n--- Encryption Process ---");
        System.out.println("Original plaintext: " + plaintext);

        plaintext = plaintext.replaceAll("[^a-zA-Z]", "").toUpperCase();
        System.out.println("Processed plaintext (non-letters removed, uppercase): " + plaintext);

        boolean addedX = false;
        if (plaintext.length() % 2 != 0) {
            plaintext += "X"; // Padding with 'X'
            addedX = true;
            System.out.println("Padded plaintext: " + plaintext);
        }

        ArrayList<Integer> phraseToNum = new ArrayList<>();
        for (int i = 0; i < plaintext.length(); i++) {
            int numericValue = plaintext.charAt(i) - 'A';
            phraseToNum.add(numericValue);
            System.out.println("Converted '" + plaintext.charAt(i) + "' to numeric value: " + numericValue);
        }

        ArrayList<Integer> phraseEncoded = new ArrayList<>();
        for (int i = 0; i < phraseToNum.size(); i += 2) {
            int x = (keyMatrix[0][0] * phraseToNum.get(i) + keyMatrix[0][1] * phraseToNum.get(i + 1)) % 26;
            int y = (keyMatrix[1][0] * phraseToNum.get(i) + keyMatrix[1][1] * phraseToNum.get(i + 1)) % 26;
            System.out.println("Matrix multiplication step for block " + (i/2) + ": Resultant values - x: " + x + ", y: " + y);
            phraseEncoded.add(x);
            phraseEncoded.add(y);
        }

        StringBuilder encryptedText = new StringBuilder();
        for (Integer num : phraseEncoded) {
            encryptedText.append((char) (num + 'A'));
        }

        System.out.println("Final encrypted text: " + encryptedText);
        return new SimpleEntry<>(encryptedText.toString(), addedX);
    }

    public String decrypt(String ciphertext, boolean removeAddedX) {
        System.out.println("\n--- Decryption Process ---");
        System.out.println("Ciphertext: " + ciphertext);

        ciphertext = ciphertext.replaceAll("[^a-zA-Z]", "").toUpperCase();
        System.out.println("Processed ciphertext (non-letters removed, uppercase): " + ciphertext);

        ArrayList<Integer> phraseToNum = new ArrayList<>();
        for (int i = 0; i < ciphertext.length(); i++) {
            int numericValue = ciphertext.charAt(i) - 'A';
            phraseToNum.add(numericValue);
            System.out.println("Converted '" + ciphertext.charAt(i) + "' to numeric value: " + numericValue);
        }

        ArrayList<Integer> phraseDecoded = new ArrayList<>();
        for (int i = 0; i < phraseToNum.size(); i += 2) {
            int x = (inverseKeyMatrix[0][0] * phraseToNum.get(i) + inverseKeyMatrix[0][1] * phraseToNum.get(i + 1)) % 26;
            int y = (inverseKeyMatrix[1][0] * phraseToNum.get(i) + inverseKeyMatrix[1][1] * phraseToNum.get(i + 1)) % 26;
            System.out.println("Matrix multiplication step for block " + (i/2) + ": Resultant values - x: " + x + ", y: " + y);
            phraseDecoded.add(x);
            phraseDecoded.add(y);
        }

        StringBuilder decryptedText = new StringBuilder();
        for (Integer num : phraseDecoded) {
            decryptedText.append((char) (num + 'A'));
        }

        if (removeAddedX && decryptedText.length() > 0 && decryptedText.charAt(decryptedText.length() - 1) == 'X') {
            decryptedText.deleteCharAt(decryptedText.length() - 1); // Remove the added 'X'
        }

        System.out.println("Final decrypted text: " + decryptedText);
        return decryptedText.toString();
    }
}
