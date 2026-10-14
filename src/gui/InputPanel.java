package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class InputPanel extends JPanel {
    private final JTextField dataField = new JTextField("11010101", 20);
    private final JTextField codewordField = new JTextField();
    private final JTextField receivedField = new JTextField();
    private final JComboBox<String> polynomialBox =
            new JComboBox<>(new String[]{"CRC-8", "CRC-16", "CRC-32", "CRC-64"});
    private final JComboBox<String> errorRateBox =
            new JComboBox<>(new String[]{"0%", "5%", "10%", "20%", "30%"});
    private final JSpinner packetSpinner =
            new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
    private final JButton generateButton = new JButton("Generate CRC");
    private final JButton verifyButton = new JButton("Verify CRC");
    private final JButton transmitButton = new JButton("Transmit");
    private final JButton resetButton = new JButton("Reset");

    public InputPanel() {
        setLayout(new GridBagLayout());
        setBackground(Theme.CARD);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.PRIMARY, 2),
                "Transmission Settings", TitledBorder.LEFT, TitledBorder.TOP,
                Theme.HEADER, Theme.TEXT));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(7, 8, 7, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;

        addRow(c, 0, "Binary Data", dataField);
        addRow(c, 1, "CRC Polynomial", polynomialBox);
        codewordField.setEditable(false);
        addRow(c, 2, "Generated Codeword", codewordField);
        addRow(c, 3, "Received Codeword", receivedField);
        addRow(c, 4, "Channel Error Rate", errorRateBox);
        addRow(c, 5, "Packet Count", packetSpinner);

        JPanel buttons = new JPanel(new GridLayout(2, 2, 6, 6));
        buttons.setBackground(Theme.CARD);
        for (JButton b : new JButton[]{generateButton, verifyButton, transmitButton, resetButton}) {
            b.setFocusPainted(false);
            buttons.add(b);
        }
        c.gridx = 0; c.gridy = 6; c.gridwidth = 2; c.weightx = 1;
        add(buttons, c);

        JLabel hint = new JLabel("<html><b>Tip:</b> change one received bit, then verify.</html>");
        hint.setForeground(Theme.MUTED);
        hint.setFont(Theme.BODY);
        c.gridy = 7;
        add(hint, c);
    }

    private void addRow(GridBagConstraints c, int row, String label, Component input) {
        c.gridwidth = 1; c.weightx = 0; c.gridx = 0; c.gridy = row;
        JLabel l = new JLabel(label);
        l.setForeground(Theme.TEXT); l.setFont(Theme.BODY);
        add(l, c);
        c.gridx = 1; c.weightx = 1;
        add(input, c);
    }

    public JTextField getDataField() { return dataField; }
    public JTextField getCodewordField() { return codewordField; }
    public JTextField getReceivedField() { return receivedField; }
    public JComboBox<String> getPolynomialBox() { return polynomialBox; }
    public JComboBox<String> getErrorRateBox() { return errorRateBox; }
    public JSpinner getPacketSpinner() { return packetSpinner; }
    public JButton getGenerateButton() { return generateButton; }
    public JButton getVerifyButton() { return verifyButton; }
    public JButton getTransmitButton() { return transmitButton; }
    public JButton getResetButton() { return resetButton; }
}
