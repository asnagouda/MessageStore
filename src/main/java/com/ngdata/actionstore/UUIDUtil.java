package com.ngdata.actionstore;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDUtil {
	public static void main(String[] args) {
		
		UUID uuid1 = UUID.randomUUID();
	    System.out.println( uuid1.toString() );
	    byte[] byteArr = convertUUID_To_16ByteArray(uuid1);
	    String strUUID = convert_16ByteArray_To_String(byteArr);
	    System.out.println("strUUID = " + strUUID);
	    
	    System.out.println( Double.MAX_VALUE );
	    System.out.println( Double.MIN_VALUE );
		
	}

    final protected static char[] hexArray = "0123456789abcdef".toCharArray();

    public static byte[] getUUIDAs16ByteArray() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        byte[] array = bb.array();
        return array;
    }

    /**
     * Get a 16 bytes binary array representation of given UUID.
     * @param uuid {@link UUID}
     * @return byte array
     */
    public static byte[] convertUUID_To_16ByteArray(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        if(null != uuid){
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
        }
        byte[] array = bb.array();
        return array;
    }
    
    /**
     * convert byte array of 16 bytes to UUID String with '-' at 8, 13, 18 and 23 offsets
     * @param byteArray
     * @return
     */
    public static String convert_16ByteArray_To_String(byte[] byteArray)  {
        StringBuilder sb = new StringBuilder("");
        if(null != byteArray && 16 == byteArray.length){
            sb.append(getUUIDByteArrayAsString(byteArray));
//            sb = sb.insert(8, '-');
//            sb = sb.insert(13, '-');
//            sb = sb.insert(18, '-');
//            sb = sb.insert(23, '-');
        }
        return sb.toString();
    }

    /**
     * Get a hex string value for the given bytes array
     * @return byte array
     */
    public static String getUUIDByteArrayAsString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Get the the specified string converted in the byte[] format.
     * @param uuid
     * @return
     * @throws DecoderException
     */
//    public static byte[] getUUIDByteArrayFromString(String uuid) throws DecoderException {
//        byte[] bytes = Hex.decodeHex(uuid.toCharArray());
//        return bytes;
//    }


}
