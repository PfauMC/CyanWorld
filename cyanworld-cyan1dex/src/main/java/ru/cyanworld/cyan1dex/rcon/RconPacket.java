package ru.cyanworld.cyan1dex.rcon;

import java.io.*;
import java.net.SocketException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RconPacket {
    public static final int SERVERDATA_EXECCOMMAND = 2;
    public static final int SERVERDATA_AUTH = 3;
    private final int requestId;
    private final int type;
    private final byte[] payload;

    private RconPacket(int requestId, int type, byte[] payload) {
        this.requestId = requestId;
        this.type = type;
        this.payload = payload;
    }

    protected static RconPacket send(RconManager rcon, int type, byte[] payload) throws IOException {
        try {
            RconPacket.write(rcon.getSocket().getOutputStream(), rcon.getRequestId(), type, payload);
        } catch (SocketException se) {
            rcon.getSocket().close();
            throw se;
        }
        return RconPacket.read(rcon.getSocket().getInputStream());
    }

    private static void write(OutputStream out, int requestId, int type, byte[] payload) throws IOException {
        int bodyLength = RconPacket.getBodyLength(payload.length);
        int packetLength = RconPacket.getPacketLength(bodyLength);
        ByteBuffer buffer = ByteBuffer.allocate(packetLength);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(bodyLength);
        buffer.putInt(requestId);
        buffer.putInt(type);
        buffer.put(payload);
        buffer.put((byte) 0);
        buffer.put((byte) 0);
        out.write(buffer.array());
        out.flush();
    }

    private static RconPacket read(InputStream in) throws IOException {
        byte[] header = new byte[12];
        in.read(header);
        try {
            ByteBuffer buffer = ByteBuffer.wrap(header);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            int length = buffer.getInt();
            int requestId = buffer.getInt();
            int type = buffer.getInt();
            byte[] payload = new byte[length - 4 - 4 - 2];
            DataInputStream dis = new DataInputStream(in);
            dis.readFully(payload);
            dis.read(new byte[2]);
            return new RconPacket(requestId, type, payload);
        } catch (EOFException | BufferUnderflowException ex) {
            System.out.println("[Cyan1dex.RCON] Cannot read the whole packet");
            ex.printStackTrace();
            return null;
        }
    }

    private static int getPacketLength(int bodyLength) {
        return 4 + bodyLength;
    }

    private static int getBodyLength(int payloadLength) {
        return 8 + payloadLength + 2;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public int getType() {
        return this.type;
    }

    public byte[] getPayload() {
        return this.payload;
    }
}
