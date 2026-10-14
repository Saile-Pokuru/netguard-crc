package model;

import java.util.Random;

public class Channel {
    private final double errorRate;
    private final Random random = new Random();

    public Channel(double errorRate) {
        if (errorRate < 0 || errorRate > 1) {
            throw new IllegalArgumentException("Error rate must be between 0 and 1.");
        }
        this.errorRate = errorRate;
    }

    public Packet transmit(Packet packet) {
        if (random.nextDouble() < errorRate) {
            packet.introduceError();
        } else {
            packet.verify();
        }
        return packet;
    }

    public Packet retransmit(Packet packet) {
        packet.removeError();
        return packet;
    }
}
