package kr.co.chience.envroadwithmvvm.viewmodel;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.IBeacon;

import java.util.ArrayList;
import java.util.List;

import kr.co.chience.envroadwithmvvm.model.Data;
import kr.co.chience.envroadwithmvvm.util.LogUtil;
import kr.co.chience.envroadwithmvvm.util.PacketUtil;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    private MutableLiveData<List<Data>> datas = new MutableLiveData<>();

    BluetoothAdapter mBluetoothAdapter;
    BluetoothLeScanner mBluetoothLeScanner;
    BluetoothGatt mBluetoothGatt;
    BluetoothDevice mBluetoothDevice;
    String proximity = "aabbccdd";
    String uuid;
    String cdc, mic, voc, co2, temp, att = null, humInt, humDec;

    public MutableLiveData<List<Data>> getData() {
        setData();
        return datas;
    }

    public void setData() {
        List<Data> data = new ArrayList<>();
        data.add(new Data(cdc, mic, voc, co2, temp, att, humInt, humDec));
        this.datas.setValue(data);
    }


    public void stratScan() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setScanMode((ScanSettings.SCAN_MODE_LOW_LATENCY));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            builder.setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE);
        }
        ScanSettings settings = builder.build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner.startScan(null, settings, mScanCallback);
        }

        LogUtil.e(TAG, "Scan Start ::::::: ");
    }

    public void stopScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            mBluetoothLeScanner.stopScan(mScanCallback);
        }

        LogUtil.e(TAG, "Scan Stop ::::::: ");
    }


    /*Scanning*/
    ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            //BLE 알림이 발견되면 콜백
            try {
                ScanRecord scanRecord = result.getScanRecord();
                bluetoothScan(result.getDevice(), scanRecord.getBytes(), result.getRssi());
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, "Error ScanCallback ::::::: ");
            }

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            //배치 결과가 전달 될 때 콜백
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            //스캔을 시작 할 수 없을 때의 콜백
        }
    };

    private String bluetoothScan(BluetoothDevice device, final byte[] scanRecord, final int rssi) {
        LogUtil.e(TAG, "processScan ::::::: ");
        List<ADStructure> structures = ADPayloadParser.getInstance().parse(scanRecord);

        String deviceUuid = null;
        String deviceName = device.getName();
        String deviceAddress = device.getAddress();
        String deviceRssi = String.valueOf(rssi);

        for (ADStructure structure : structures) {
            if (structure instanceof IBeacon) {
                IBeacon iBeacon = (IBeacon) structure;
                deviceUuid = iBeacon.getUUID().toString();

                if (deviceUuid.startsWith(proximity)) {

                    uuid = deviceUuid;

                    cdc = PacketUtil.hexStringToInteger(uuid.substring(9, 11));
                    mic = PacketUtil.hexStringToInteger(uuid.substring(11, 13));
                    voc = PacketUtil.hexStringToInteger(uuid.substring(14, 16)) + PacketUtil.hexStringToInteger(uuid.substring(16, 18));
                    co2 = PacketUtil.hexStringToInteger(uuid.substring(19, 21)) + PacketUtil.hexStringToInteger(uuid.substring(21, 23));
                    temp = PacketUtil.hexStringToInteger(uuid.substring(24, 26));

                    if (uuid.substring(26, 28).equals("ff")) {
                        att = "-" + PacketUtil.hexStringToInteger(uuid.substring(28, 30)) + PacketUtil.hexStringToInteger(uuid.substring(30, 32));
                    } else if (uuid.substring(26, 28).equals("0")) {
                        att = "+" + PacketUtil.hexStringToInteger(uuid.substring(28, 30)) + PacketUtil.hexStringToInteger(uuid.substring(30, 32));
                    }

                    humInt = PacketUtil.hexStringToInteger(uuid.substring(32, 34));
                    humDec = PacketUtil.hexStringToInteger(uuid.substring(34, 36));

                    LogUtil.e(TAG, "Device Uuid ::::: " + deviceUuid);

                    setData();

                }

            }

        }
        return deviceAddress;
    }


}
