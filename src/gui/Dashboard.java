package gui;

import model.CRCGenerator;
import model.Channel;
import model.Packet;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
    private final InputPanel inputPanel = new InputPanel();
    private final AnimationPanel animationPanel = new AnimationPanel();
    private final StatisticsPanel statisticsPanel = new StatisticsPanel();
    private final LogPanel logPanel = new LogPanel();

    private int packetsSent, packetsReceived, crcErrors, retransmissions;

    public Dashboard() {
        setTitle("NetGuard CRC | Intelligent Network Transmission Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setSize(1280, 790);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout(8, 8));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 16));
        header.setBackground(Theme.PRIMARY);
        JLabel title = new JLabel("NETGUARD CRC  |  NETWORK TRANSMISSION LAB");
        title.setForeground(Color.WHITE);
        title.setFont(Theme.TITLE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setBackground(Theme.BACKGROUND);
        inputPanel.setPreferredSize(new Dimension(465, 400));
        statisticsPanel.setPreferredSize(new Dimension(250, 300));
        center.add(inputPanel, BorderLayout.WEST);
        center.add(animationPanel, BorderLayout.CENTER);
        center.add(statisticsPanel, BorderLayout.EAST);
        add(center, BorderLayout.CENTER);

        logPanel.setPreferredSize(new Dimension(1000, 155));
        add(logPanel, BorderLayout.SOUTH);

        registerEvents();
    }

    private void registerEvents() {
        inputPanel.getGenerateButton().addActionListener(e -> generate());
        inputPanel.getVerifyButton().addActionListener(e -> verifyManual());
        inputPanel.getTransmitButton().addActionListener(e -> transmit());
        inputPanel.getResetButton().addActionListener(e -> reset());
    }

    private String selectedPolynomial() {
        return CRCGenerator.getPolynomial(
                String.valueOf(inputPanel.getPolynomialBox().getSelectedItem()));
    }

    private boolean validBinary(String data) {
        if (data == null || !data.matches("[01]+")) {
            JOptionPane.showMessageDialog(this,
                    "Enter binary data containing only 0 and 1.",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void generate() {
        String data = inputPanel.getDataField().getText().trim();
        if (!validBinary(data)) return;

        String type = String.valueOf(inputPanel.getPolynomialBox().getSelectedItem());
        String polynomial = selectedPolynomial();
        String codeword = CRCGenerator.generateCRC(data, polynomial);
        inputPanel.getCodewordField().setText(codeword);
        inputPanel.getReceivedField().setText(codeword);

        logPanel.log("CRC generated | Type: " + type + " | Data bits: " + data.length());
        logPanel.log("Codeword length: " + codeword.length() + " bits");
        logPanel.log("Codeword: " + codeword);
        logPanel.log("Tip: change one bit in Received Codeword to test error detection.");
    }

    private void verifyManual() {
        String received = inputPanel.getReceivedField().getText().trim();
        if (!validBinary(received)) return;

        String polynomial = selectedPolynomial();
        if (received.length() < polynomial.length()) {
            JOptionPane.showMessageDialog(this,
                    "Received codeword is too short for this polynomial.");
            return;
        }

        String remainder = CRCGenerator.getRemainder(received, polynomial);
        boolean valid = CRCGenerator.verifyCRC(received, polynomial);
        logPanel.log("Manual CRC verification | Remainder: " + remainder);
        if (valid) {
            logPanel.log("RESULT: PASS — zero remainder; no error detected.");
            JOptionPane.showMessageDialog(this,
                    "CRC PASS\nRemainder: " + remainder,
                    "Verification Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            crcErrors++;
            logPanel.log("RESULT: ERROR — non-zero remainder detected.");
            animationPanel.startErrorTransmission();
            JOptionPane.showMessageDialog(this,
                    "CRC ERROR DETECTED\nRemainder: " + remainder,
                    "Verification Result", JOptionPane.ERROR_MESSAGE);
        }
        updateStatistics();
    }

    private void transmit() {
        String data = inputPanel.getDataField().getText().trim();
        if (!validBinary(data)) return;

        String polynomial = selectedPolynomial();
        String type = String.valueOf(inputPanel.getPolynomialBox().getSelectedItem());
        Packet packet = new Packet(data, polynomial);
        double rate = Double.parseDouble(
                String.valueOf(inputPanel.getErrorRateBox().getSelectedItem()).replace("%", "")) / 100.0;
        Channel channel = new Channel(rate);
        int count = (Integer) inputPanel.getPacketSpinner().getValue();

        logPanel.log("Starting transmission batch: " + count + " packet(s), " + type
                + ", channel error rate " + (int)(rate * 100) + "%.");

        for (int i = 1; i <= count; i++) {
            Packet current = new Packet(data, polynomial);
            packetsSent++;
            channel.transmit(current);

            if (current.isSuccessfullyReceived()) {
                packetsReceived++;
                logPanel.log("Packet " + i + ": delivered — CRC PASS.");
            } else {
                crcErrors++;
                retransmissions++;
                logPanel.log("Packet " + i + ": CRC ERROR — retransmitting.");
                channel.retransmit(current);
                packetsReceived++;
                logPanel.log("Packet " + i + ": delivered after retransmission.");
            }
        }

        if (rate > 0) animationPanel.startErrorTransmission();
        else animationPanel.startTransmission();
        updateStatistics();
    }

    private void updateStatistics() {
        statisticsPanel.setPacketsSent(packetsSent);
        statisticsPanel.setPacketsReceived(packetsReceived);
        statisticsPanel.setCRCErrors(crcErrors);
        statisticsPanel.setRetransmissions(retransmissions);
        statisticsPanel.setSuccessRate(packetsSent == 0 ? 0 : packetsReceived * 100.0 / packetsSent);
    }

    private void reset() {
        packetsSent = packetsReceived = crcErrors = retransmissions = 0;
        statisticsPanel.reset();
        animationPanel.resetAnimation();
        inputPanel.getCodewordField().setText("");
        inputPanel.getReceivedField().setText("");
        logPanel.clearLogs();
        logPanel.log("Simulator reset. Ready for a new experiment.");
    }
}
