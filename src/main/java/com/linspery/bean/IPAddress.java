package com.linspery.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Linspery on 15/12/1.
 */
public class IPAddress {
    public static List<String> IPlist =new ArrayList<String>();

    public static List<String> getIPlist() {
        return IPlist;
    }

    public static void setIPlist(List<String> IPlist) {
        IPAddress.IPlist = IPlist;
    }
}
