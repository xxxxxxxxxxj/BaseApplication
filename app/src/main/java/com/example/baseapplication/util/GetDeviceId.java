package com.example.baseapplication.util;

/**
 * @author:姜谷蓄
 * @Date:2019/10/8
 * @Description:
 */

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.UUID;

/**
 * @author liangjun on 2018/1/21.
 */

public class GetDeviceId {

    /**
     * 读取固定的文件中的内容,这里就是读取sd卡中保存的设备唯一标识符
     *
     * @param context
     * @return
     */
    public static String readDeviceID(Context context) {
        try {
            File file = CommonUtil.createFile(context, 4,"",null);
            StringBuffer buffer = new StringBuffer();
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            Reader in = new BufferedReader(isr);
            int i;
            while ((i = in.read()) > -1) {
                buffer.append((char) i);
            }
            in.close();
            return buffer.toString();
        } catch (IOException e) {
            Log.e("TAG", "readDeviceID失败 e = " + e.toString());
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 保存 内容到 SD卡中,  这里保存的就是 设备唯一标识符
     * * @param context
     */
    public static void saveDeviceID(Context context) {
        try {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            File file = CommonUtil.createFile(context, 4,"",null);
            StringBuffer buffer = new StringBuffer();
            FileOutputStream fos = new FileOutputStream(file);
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            out.write(uuid);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG", "saveDeviceID失败 e = " + e.toString());
        }
    }
}
