package kr.co.chience.envroadwithmvvm.util;

import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.IBeacon;
import java.util.List;

public class ScanUtil {

    public static String bluetoothScan(final byte[] scanRecord, String proximity) {

        List<ADStructure> structures = ADPayloadParser.getInstance().parse(scanRecord);

        String deviceUuid = null;
        String uuid = null;

        for (ADStructure structure : structures) {
            if (structure instanceof IBeacon) {
                IBeacon iBeacon = (IBeacon) structure;
                deviceUuid = iBeacon.getUUID().toString();

                if (deviceUuid.startsWith(proximity)) {
                    uuid = deviceUuid;
                    return uuid;
                }

            }

        }
        return uuid;
    }

}

