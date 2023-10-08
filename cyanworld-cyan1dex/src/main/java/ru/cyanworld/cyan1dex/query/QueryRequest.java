package ru.cyanworld.cyan1dex.query;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class QueryRequest {
    static byte[] MAGIC = new byte[]{-2, -3};
    byte type;
    int sessionID;
    byte[] payload;
    private ByteArrayOutputStream byteStream;
    private DataOutputStream dataStream;

    public QueryRequest() {
        int size = 1460;
        this.byteStream = new ByteArrayOutputStream(size);
        this.dataStream = new DataOutputStream(this.byteStream);
    }

    public QueryRequest(byte type) {
        this.type = type;
    }

    byte[] toBytes() {
        this.byteStream.reset();
        try {
            this.dataStream.write(MAGIC);
            this.dataStream.write(this.type);
            this.dataStream.writeInt(this.sessionID);
            this.dataStream.write(this.payloadBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.byteStream.toByteArray();
    }

    private byte[] payloadBytes() {
        if (this.type == 9) {
            return new byte[0];
        }
        return this.payload;
    }

    protected void setPayload(int load) {
        this.payload = ByteUtils.intToBytes(load);
    }
}
