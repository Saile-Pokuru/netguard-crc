package model;

public class Packet {
    private final String data;
    private final String polynomial;
    private final String codeword;
    private String receivedCodeword;
    private boolean corrupted;
    private boolean successfullyReceived;

    public Packet(String data, String polynomial) {
        this.data = data;
        this.polynomial = polynomial;
        this.codeword = CRCGenerator.generateCRC(data, polynomial);
        this.receivedCodeword = codeword;
        this.corrupted = false;
        this.successfullyReceived = true;
    }

    public String getData() { return data; }
    public String getPolynomial() { return polynomial; }
    public String getCodeword() { return codeword; }
    public String getReceivedCodeword() { return receivedCodeword; }
    public boolean isCorrupted() { return corrupted; }
    public boolean isSuccessfullyReceived() { return successfullyReceived; }

    public void setReceivedCodeword(String value) {
        receivedCodeword = value;
        verify();
    }

    public void introduceError() {
        if (receivedCodeword == null || receivedCodeword.isEmpty()) return;
        char[] bits = receivedCodeword.toCharArray();
        int index = bits.length / 2;
        bits[index] = bits[index] == '0' ? '1' : '0';
        receivedCodeword = new String(bits);
        verify();
    }

    public void verify() {
        successfullyReceived = CRCGenerator.verifyCRC(receivedCodeword, polynomial);
        corrupted = !successfullyReceived;
    }

    public void removeError() {
        receivedCodeword = codeword;
        verify();
    }
}
