package ru.cyanworld.cyan1dex.query;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class ByteUtils {
    public static byte[] subarray(byte[] in, int a, int b) {
        if (b - a > in.length) {
            return in;
        }
        byte[] out = new byte[b - a + 1];
        if (b + 1 - a >= 0) System.arraycopy(in, a, out, a - a, b + 1 - a);
        return out;
    }

    public static byte[] trim(byte[] arr) {
        int i;
        if (arr[0] != 0 && arr[arr.length] != 0) {
            return arr;
        }
        int begin = 0;
        int end = arr.length;
        for (i = 0; i < arr.length; ++i) {
            if (arr[i] == 0) continue;
            begin = i;
            break;
        }
        for (i = arr.length - 1; i >= 0; --i) {
            if (arr[i] == 0) continue;
            end = i;
            break;
        }
        return ByteUtils.subarray(arr, begin, end);
    }

    public static byte[][] split(byte[] input) {
        int i;
        ArrayList<byte[]> temp = new ArrayList<byte[]>();
        byte[][] output = new byte[input.length][input.length];
        boolean out_index = false;
        int index_cache = 0;
        for (i = 0; i < input.length; ++i) {
            if (input[i] != 0) continue;
            byte[] b = ByteUtils.subarray(input, index_cache, i - 1);
            temp.add(b);
            index_cache = i + 1;
        }
        if (index_cache != 0) {
            byte[] b = ByteUtils.subarray(input, index_cache, input.length - 1);
            temp.add(b);
        }
        output = new byte[temp.size()][input.length];
        for (i = 0; i < temp.size(); ++i) {
            output[i] = temp.get(i);
        }
        return output;
    }

    public static byte[] padArrayEnd(byte[] arr, int amount) {
        int i;
        byte[] arr2 = new byte[arr.length + amount];
        for (i = 0; i < arr.length; ++i) {
            arr2[i] = arr[i];
        }
        for (i = arr.length; i < arr2.length; ++i) {
            arr2[i] = 0;
        }
        return arr2;
    }

    public static short bytesToShort(byte[] b) {
        ByteBuffer buf = ByteBuffer.wrap(b, 0, 2);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getShort();
    }

    public static byte[] intToBytes(int in) {
        byte[] b = new byte[]{(byte) (in >>> 24 & 255), (byte) (in >>> 16 & 255), (byte) (in >>> 8 & 255), (byte) (in >>> 0 & 255)};
        return b;
    }

    public static int bytesToInt(byte[] in) {
        return ByteBuffer.wrap(in).getInt();
    }
}
