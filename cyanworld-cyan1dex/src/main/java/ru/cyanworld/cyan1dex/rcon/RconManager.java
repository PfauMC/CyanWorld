package ru.cyanworld.cyan1dex.rcon;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class RconManager {
    private final Object sync = new Object();
    private final Random rand = new Random();
    public int port;
    private int requestId;
    private Socket socket;
    private Charset charset = StandardCharsets.UTF_8;

    public RconManager(String host, int port, byte[] password) throws IOException {
        this.connect(host, port, password);
        this.port = port;
    }

    public void connect(String host, int port, byte[] password) throws IOException {
        if (host == null || host.trim().isEmpty()) {
            throw new IllegalArgumentException("Host can't be null or empty");
        }
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Port is out of range");
        }
        Object object = this.sync;
        synchronized (object) {
            this.requestId = this.rand.nextInt();
            this.socket = new Socket(host, port);
        }
        RconPacket res = this.send(3, password);
        if (res.getRequestId() == -1) {
            System.out.print("Password rejected by server");
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void disconnect() throws IOException {
        Object object = this.sync;
        synchronized (object) {
            this.socket.close();
        }
    }

    public String command(String payload) throws IOException {
        if (payload == null || payload.trim().isEmpty()) {
            throw new IllegalArgumentException("Payload can't be null or empty");
        }
        RconPacket response = this.send(2, payload.getBytes());
        return new String(response.getPayload(), this.getCharset());
    }

    private RconPacket send(int type, byte[] payload) throws IOException {
        Object object = this.sync;
        synchronized (object) {
            return RconPacket.send(this, type, payload);
        }
    }

    public int getRequestId() {
        return this.requestId;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }
}
