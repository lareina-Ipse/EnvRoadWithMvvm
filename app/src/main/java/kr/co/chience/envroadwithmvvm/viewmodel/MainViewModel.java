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
import android.util.Log;

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
import kr.co.chience.envroadwithmvvm.util.ScanUtil;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    private MutableLiveData<List<Data>> datas = new MutableLiveData<>();

    BluetoothAdapter mBluetoothAdapter;
    BluetoothLeScanner mBluetoothLeScanner;
    BluetoothDevice mBluetoothDevice;
    String proximity = "aabbccdd";
    String uuid, cdc, mic, voc, co2, temp, att, humInt, humDec;

    public MutableLiveData<List<Data>> getData() {
        if (uuid != null && !uuid.isEmpty()) {
            setData();
        }
        return datas;
    }

    public void setData() {
        List<Data> data = new ArrayList<>();
        data.add(new Data(cdc, mic, voc, co2, temp, att, humInt, humDec));
        datas.setValue(data);
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
                uuid = ScanUtil.bluetoothScan(scanRecord.getBytes(), proximity);
                if (uuid != null && !uuid.isEmpty()) {
                    cdc = PacketUtil.cdc(uuid);
                    mic = PacketUtil.mic(uuid);
                    voc = PacketUtil.voc(uuid);
                    co2 = PacketUtil.co2(uuid);
                    temp = PacketUtil.temp(uuid);
                    att = PacketUtil.att(uuid);
                    humInt = PacketUtil.humInt(uuid);
                    humDec = PacketUtil.humDec(uuid);
                    setData();
                }

            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, "Error ScanCallback ::::::: " + e);
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

}
