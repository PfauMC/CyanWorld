package ru.cyanworld.cyan1dex.query;

import java.net.*;

public class MCQuery {
    static final byte HANDSHAKE = 9;
    static final byte STAT = 0;
    String serverAddress = "localhost";
    int queryPort = 25565;
    int localPort = 25566;
    private DatagramSocket socket = null;
    private int token;

    public MCQuery() {
    }

    public MCQuery(String address) {
        this(address, 25565);
    }

    public MCQuery(String address, int port) {
        this.serverAddress = address;
        this.queryPort = port;
    }

    static void printBytes(byte[] arr) {
        for (byte b : arr) {
            System.out.print(b + " ");
        }
        System.out.println();
    }

    static void printHex(byte[] arr) {
        System.out.println(MCQuery.toHex(arr));
    }

    static String toHex(byte[] b) {
        String out = "";
        for (byte bb : b) {
            out = out + String.format("%02X ", Byte.valueOf(bb));
        }
        return out;
    }

    public static void main(String[] args) {
        MCQuery mc = new MCQuery();
        if (mc.basicStat() == null) {
            return;
        }
        System.out.println(mc.basicStat().toString());
        System.out.println(mc.basicStat().asJSON());
        System.out.println("=====================");
        System.out.println(mc.fullStat().toString());
        System.out.println(mc.fullStat().asJSON());
    }

    private void handshake() {
        QueryRequest req = new QueryRequest();
        req.type = 9;
        req.sessionID = this.generateSessionID();
        int val = 11 - req.toBytes().length;
        byte[] input = ByteUtils.padArrayEnd(req.toBytes(), val);
        byte[] result = this.sendUDP(input);
        if (result == null) {
            return;
        }
        this.token = Integer.parseInt(new String(result).trim());
    }

    public QueryResponse basicStat() {
        this.handshake();
        QueryRequest req = new QueryRequest();
        req.type = 0;
        req.sessionID = this.generateSessionID();
        req.setPayload(this.token);
        byte[] send = req.toBytes();
        byte[] result = this.sendUDP(send);
        if (result == null) {
            return null;
        }
        return new QueryResponse(result, false);
    }

    public QueryResponse fullStat() {
        this.handshake();
        QueryRequest req = new QueryRequest();
        req.type = 0;
        req.sessionID = this.generateSessionID();
        req.setPayload(this.token);
        req.payload = ByteUtils.padArrayEnd(req.payload, 4);
        byte[] send = req.toBytes();
        byte[] result = this.sendUDP(send);
        QueryResponse res = new QueryResponse(result, true);
        return res;
    }

    private byte[] sendUDP(byte[] input) {
        try {
            while (this.socket == null) {
                try {
                    this.socket = new DatagramSocket(this.localPort);
                } catch (BindException e) {
                    ++this.localPort;
                }
            }
            InetAddress address = InetAddress.getByName(this.serverAddress);
            DatagramPacket packet1 = new DatagramPacket(input, input.length, address, this.queryPort);
            this.socket.send(packet1);
            byte[] out = new byte[1024];
            DatagramPacket packet = new DatagramPacket(out, out.length);
            this.socket.setSoTimeout(1000);
            this.socket.receive(packet);
            return packet.getData();
        } catch (SocketException e2) {
            e2.printStackTrace();
        } catch (SocketTimeoutException e2) {
        } catch (UnknownHostException e) {
            System.err.println("Unknown host!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private int generateSessionID() {
        return 1;
    }

    protected void finalize() {
        this.socket.close();
    }
}
