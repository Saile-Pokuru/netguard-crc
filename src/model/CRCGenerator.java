package model;

import java.math.BigInteger;

public final class CRCGenerator {
    private CRCGenerator() {}

    /*
     * Polynomial bit strings include the leading x^degree bit:
     * CRC-8:  x^8 + x^2 + x + 1
     * CRC-16: x^16 + x^15 + x^2 + 1 (same polynomial as earlier project version)
     * CRC-32: IEEE 802.3 generator polynomial
     * CRC-64: CRC-64/ECMA-182 generator polynomial
     */
    public static String getPolynomial(String type) {
        switch (type) {
            case "CRC-8":
                return "100000111";
            case "CRC-16":
                return "11000000000000101";
            case "CRC-32":
                return "100000100110000010001110110110111";
            case "CRC-64":
                return "1" + String.format("%64s",
                        new BigInteger("42F0E1EBA9EA3693", 16).toString(2))
                        .replace(' ', '0');
            default:
                throw new IllegalArgumentException("Unsupported CRC type: " + type);
        }
    }

    public static String generateCRC(String data, String polynomial) {
        validateBits(data, "Data");
        validatePolynomial(polynomial);

        int degree = polynomial.length() - 1;
        StringBuilder work = new StringBuilder(data);
        work.append("0".repeat(degree));

        char[] dividend = work.toString().toCharArray();
        char[] divisor = polynomial.toCharArray();

        for (int i = 0; i <= dividend.length - divisor.length; i++) {
            if (dividend[i] == '1') {
                for (int j = 0; j < divisor.length; j++) {
                    dividend[i + j] = dividend[i + j] == divisor[j] ? '0' : '1';
                }
            }
        }

        String remainder = new String(dividend,
                dividend.length - degree, degree);
        return data + remainder;
    }

    public static boolean verifyCRC(String receivedCodeword, String polynomial) {
        if (receivedCodeword == null || receivedCodeword.isEmpty()
                || !receivedCodeword.matches("[01]+")) {
            return false;
        }
        validatePolynomial(polynomial);
        if (receivedCodeword.length() < polynomial.length()) {
            return false;
        }

        char[] dividend = receivedCodeword.toCharArray();
        char[] divisor = polynomial.toCharArray();

        for (int i = 0; i <= dividend.length - divisor.length; i++) {
            if (dividend[i] == '1') {
                for (int j = 0; j < divisor.length; j++) {
                    dividend[i + j] = dividend[i + j] == divisor[j] ? '0' : '1';
                }
            }
        }

        for (int i = dividend.length - (polynomial.length() - 1);
             i < dividend.length; i++) {
            if (dividend[i] == '1') return false;
        }
        return true;
    }

    public static String getRemainder(String receivedCodeword, String polynomial) {
        if (receivedCodeword == null || receivedCodeword.isEmpty()
                || !receivedCodeword.matches("[01]+")) {
            return "Invalid binary input";
        }
        if (receivedCodeword.length() < polynomial.length()) {
            return "Codeword is shorter than the polynomial";
        }
        char[] dividend = receivedCodeword.toCharArray();
        char[] divisor = polynomial.toCharArray();

        for (int i = 0; i <= dividend.length - divisor.length; i++) {
            if (dividend[i] == '1') {
                for (int j = 0; j < divisor.length; j++) {
                    dividend[i + j] = dividend[i + j] == divisor[j] ? '0' : '1';
                }
            }
        }
        return new String(dividend, dividend.length - (polynomial.length() - 1),
                polynomial.length() - 1);
    }

    private static void validateBits(String value, String label) {
        if (value == null || !value.matches("[01]+")) {
            throw new IllegalArgumentException(label + " must contain only 0 and 1.");
        }
    }

    private static void validatePolynomial(String polynomial) {
        if (polynomial == null || polynomial.length() < 2
                || !polynomial.matches("1[01]+")) {
            throw new IllegalArgumentException("Invalid CRC polynomial.");
        }
    }
}
