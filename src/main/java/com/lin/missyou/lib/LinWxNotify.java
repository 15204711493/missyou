package com.lin.missyou.lib;

import com.lin.missyou.exception.http.ServiereErrorException;

import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LinWxNotify {


    public static String readNotify(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        String line;

        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");

            }
        } catch (IOException e) {
            throw new ServiereErrorException(9999);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String xml = stringBuilder.toString();
        return xml;

    }


    public static String fail() {
        return "false";

    }

    public static String success() {
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }
}
