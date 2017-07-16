package com.artifex.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * Created by DELL on 6/28/2017.
 */

public class ConstantValues {
    public static final String baseDomainUrl = "http://ec2-13-59-14-57.us-east-2.compute.amazonaws.com:8080/livetution";
    public static final String fetchDataUrl = "http://ec2-13-59-14-57.us-east-2.compute.amazonaws.com:8080";
    public static final String fileStoragePath = "/storage/emulated/0/Download/";

    public static boolean loginSessionFlag = false;
    public static boolean offlineDemoFlag = false;

    public static String getLastWord(String uri)  {
        String[] segments = uri.split("/");
        String idStr2 = segments[segments.length-1];
         String encodedFilename = idStr2;
        try {
            encodedFilename = URLEncoder.encode(encodedFilename, "UTF-8").replace("+", "%20%");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedFilename ;
    }
}
