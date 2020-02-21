package kr.co.chience.envroadwithmvvm.util;

public class PacketUtil {

    public static String cdc(String uuid) {
        return hexStringToInteger(uuid.substring(9, 11));
    }

    public static String mic(String uuid) {
        return hexStringToInteger(uuid.substring(11, 13));
    }

    public static String voc(String uuid) {
        return hexStringToInteger(uuid.substring(14, 16)) + hexStringToInteger(uuid.substring(16, 18));
    }
    public static String co2(String uuid) {
        return hexStringToInteger(uuid.substring(19, 21)) + hexStringToInteger(uuid.substring(21, 23));
    }
    public static String temp(String uuid) {
        return hexStringToInteger(uuid.substring(24, 26));
    }
    public static String att(String uuid) {

        if (uuid.substring(26, 28).equals("ff")) {
            return  "-" + hexStringToInteger(uuid.substring(28, 30)) + hexStringToInteger(uuid.substring(30, 32));
        }
        return "+" + hexStringToInteger(uuid.substring(28, 30)) + hexStringToInteger(uuid.substring(30, 32));
    }
    public static String humInt(String uuid) {
        return hexStringToInteger(uuid.substring(32, 34));
    }
    public static String humDec(String uuid) {
        return hexStringToInteger(uuid.substring(34, 36));
    }

    public static String hexStringToInteger(String hex) {
        return String.valueOf(Integer.parseInt(hex, 16));
    }

}
