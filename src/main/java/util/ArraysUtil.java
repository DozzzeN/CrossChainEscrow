package util;

public class ArraysUtil {
    /**
     * 合并字节数组
     *
     * @param values 要合并的字节数组
     * @return 合并过后的字节数组
     */
    public static byte[] mergeByte(byte[]... values) {
        int byte_length = 0;
        for (byte[] bytes : values) {
            byte_length += bytes.length;
        }

        byte[] mergedByte = new byte[byte_length];
        int countlength = 0;
        for (byte[] value : values) {
            System.arraycopy(value, 0, mergedByte, countlength, value.length);
            countlength += value.length;
        }
        return mergedByte;
    }

    /**
     * 分割数组
     *
     * @param value 分割的字节数组
     * @param size  分割的个数
     */
    public static byte[][] splitByte(byte[] value, int... size) {
        byte[][] result = new byte[size.length][];

        int index = 0;
        int i, j;
        for (i = 0; i < size.length; i++) {
            byte[] mid_data = new byte[size[i]];
            for (j = 0; j < size[i]; j++) {
                mid_data[j] = value[index];
                index++;
            }
            result[i] = mid_data;
        }
        return result;
    }

    public static boolean isEqual(byte[] b1, byte[] b2) {
        if (b1 == null && b2 == null) return true;
        if (b1 == null || b2 == null) return false;
        if (b1.length != b2.length) return false;
        for (int i = 0; i < b1.length; i++) {
            if (b1[i] != b2[i]) {
                return false;
            }
        }
        return true;
    }
}