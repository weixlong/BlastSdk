package com.hzjq.core.util;

import android.device.DeviceManager;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;

public class OtgUtils {



    /**
     * 模块上电
     *
     * @param enable
     * @return
     */
    public static boolean setDT40CGPIOEnabled(boolean enable) {
       String POGO_NODE_YOUKAI ="/sys/devices/soc/78db000.usb/otg_enable";
        String projectName = new DeviceManager().getSettingProperty("pwv.project");
        Log.d("ubx","projectName:" + projectName);
        FileOutputStream node_1 = null;
        FileOutputStream node_2 = null;
        byte[] close = new byte[]{0x30};
        try {
            Log.d("ubx","enable:" + enable);
                byte[] open_two = new byte[]{0x31};
                node_1 = new FileOutputStream(POGO_NODE_YOUKAI);
                node_1.write(enable ? open_two : close);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (node_1 != null) {
                    node_1.close();
                }
                if (node_2 != null) {
                    node_2.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


//    public static void set53CGPIOEnabled(boolean enable) {
//        boolean is53CGPIO = false;
//            FileOutputStream f = null;
//            FileOutputStream f1 = null;
//            try {
//                if (is53CGPIO) {
//                    Log.i("urovo", "enable:" + enable);
//                    f = new FileOutputStream("/sys/devices/soc/soc:sectrl/ugp_ctrl/gp_pogo_5v_ctrl/enable");
//                    f.write(enable ? "1".getBytes() : "0".getBytes());
//                    f1 = new FileOutputStream("/sys/devices/soc/soc:sectrl/ugp_ctrl/gp_otg_en_ctrl/enable");
//                    f1.write(enable ? "1".getBytes() : "0".getBytes());
//                } else {
//                    Log.i("ubx", "set53GPIOcEnabled: " + enable);
//                    f = new FileOutputStream("/sys/devices/soc/c170000.serial/pogo_uart");
//                    f.write(enable ? "1".getBytes() : "0".getBytes());
//                    f1 = new FileOutputStream("/sys/devices/virtual/Usb_switch/usbswitch/function_otg_en");
//                    f1.write(enable ? "2".getBytes() : "0".getBytes());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (f != null) {
//                    try {
//                        f.close();
//                        f1.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//    }
//
//    public static void set52OTGEnabled(boolean enable) {
//        FileOutputStream f = null;
//        try {
//            f = new FileOutputStream("/sys/devices/soc/78db000.usb/dpdm_pulldown_enable");
//            f.write(enable ? "otgenable".getBytes() : "otgdisable".getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (f != null) {
//                try {
//                    f.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    public static void set53GPIOEnabled(boolean enable) {
//        FileOutputStream f = null;
//        FileOutputStream f1 = null;
//        try {
//            Log.i("ubx", "set53GPIOcEnabled: " + enable);
//            f = new FileOutputStream("/sys/devices/soc/c170000.serial/pogo_uart");
//            f.write(enable ? "1".getBytes() : "0".getBytes());
//            f1 = new FileOutputStream("/sys/devices/virtual/Usb_switch/usbswitch/function_otg_en");
//            f1.write(enable ? "2".getBytes() : "0".getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (f != null) {
//                try {
//                    f.close();
//                    f1.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

}
