package kr.co.chience.envroadwithmvvm.util;

public class PacketUtil {

    public static String cdc(String uuid) {
        return hexStringToInteger(uuid.substring(9, 11)) + " lx";
    }

    public static String mic(String uuid) {
        if (Integer.parseInt(uuid.substring(11, 13)) <= 0) {
            return "소리 없음";
        }
        return "소리 있음";
    }

    public static String voc(String uuid) {
        return hexStringToInteger(uuid.substring(14, 16)) + hexStringToInteger(uuid.substring(16, 18)) + " ppb";
    }
    public static String co2(String uuid) {
        return hexStringToInteger(uuid.substring(19, 21)) + hexStringToInteger(uuid.substring(21, 23)) + " ppm";
    }
    public static String temp(String uuid) {
        return hexStringToInteger(uuid.substring(24, 26)) + " ºC";
    }
    public static String att(String uuid) {

        if (uuid.substring(26, 28).equals("ff")) {
            return  "-" + hexStringToInteger(uuid.substring(28, 30)) + hexStringToInteger(uuid.substring(30, 32)) + " hPa";
        }
        return "+" + hexStringToInteger(uuid.substring(28, 30)) + hexStringToInteger(uuid.substring(30, 32)) + " hPa" ;
    }
    public static String humInt(String uuid) {
        return hexStringToInteger(uuid.substring(32, 34)) + " %RH";
    }
    public static String humDec(String uuid) {
        return hexStringToInteger(uuid.substring(34, 36)) + " %RH";
    }

    public static String hexStringToInteger(String hex) {
        return String.valueOf(Integer.parseInt(hex, 16));
    }

}
