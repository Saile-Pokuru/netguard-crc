# NetGuard CRC — Intelligent Network Transmission Simulator

A standalone Java Swing project demonstrating CRC-8, CRC-16, CRC-32, and CRC-64 error detection.

## Requirements
- JDK 11 or newer
- No external libraries

## Run on macOS / Linux
Open Terminal and enter the project folder, then run:

```bash
javac -d out src/model/*.java src/gui/*.java src/app/*.java
java -cp out app.Main
```

## Demo workflow
1. Enter binary data, for example `11010101`.
2. Choose CRC-8, CRC-16, CRC-32, or CRC-64.
3. Click **Generate CRC**. The generated codeword appears in both codeword fields.
4. To test a clean packet, click **Verify CRC** without changing the received codeword.
5. To demonstrate detection, flip one bit in **Received Codeword** and click **Verify CRC**.
6. **Transmit** simulates a channel using the selected error rate. Set it to 0% to demonstrate a clean channel; higher rates introduce simulated bit corruption.
7. **Reset** clears counters and the event log.

## What makes this version distinct
- CRC-64 / ECMA-182 polynomial option in addition to CRC-8/16/32.
- Separate generated and received codeword fields for manual corruption experiments.
- CRC remainder status, clear pass/error result, packet counters, retransmission count, and event log.
- Resizable layout and packet animation.

## Important technical note
CRC detects many transmission errors; it does not correct errors and cannot guarantee detection of every possible multi-bit corruption. A CRC result of PASS means the received bit string has a zero remainder for the selected polynomial, not that the sender's identity or the data's authenticity has been verified.
