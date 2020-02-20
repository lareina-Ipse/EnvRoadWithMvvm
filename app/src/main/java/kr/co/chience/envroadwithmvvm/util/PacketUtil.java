package kr.co.chience.envroadwithmvvm.util;

public class PacketUtil {

    public static String hexStringToInteger(String hex) {
        return String.valueOf(Integer.parseInt(hex, 16));
    }

}
