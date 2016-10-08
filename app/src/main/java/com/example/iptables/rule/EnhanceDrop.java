package com.example.iptables.rule;

import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.Log;

import com.example.iptables.support.Rooter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author EdisonChang
 */
public class EnhanceDrop {

    private static final String TAG = "EnhanceDrop";

    private static String exec(String cmd, String args[], int timeOut) {
        try {
            String arg = "";
            if (args != null) {
                for (String s : args) {
                    arg += " ";
                    arg += s;
                }
            }
            return Rooter.CommandOutput(cmd + arg);
        } catch (Throwable e) {
            throw new AndroidRuntimeException("Root service has not run");
        }
    }

    public static void execIpTables() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String iptables;
                try {
                    Log.i(TAG, "iptables -L");

                    String args[];
                    args = new String[]{"-L"};
                    iptables = exec("iptables", args, 10000);

                    int rules = 0;
                    int uid = android.os.Process.myUid();
                    rules = getDropRules(uid, iptables);
                    for (int j = 0; j < rules; j++) {
                        String argsOut[], argsIn[];
                        argsOut = new String[]{"-D", "OUTPUT", "-m", "owner", "--uid-owner", String.valueOf(uid), "-j", "DROP"};
                        argsIn = new String[]{"-D", "INPUT", "-m", "owner", "--uid-owner", String.valueOf(uid), "-j", "DROP"};
                        exec("iptables", argsOut, 5000);
                        exec("iptables", argsIn, 5000);
                    }

                    String arg2s[];
                    arg2s = new String[]{"--line-numbers", "-L", "OUTPUT"};
                    iptables = exec("iptables", arg2s, 10000);

                    List<Integer> lineNum = getDropRuleLines(uid, iptables);
                    for (int j = 0; j < lineNum.size(); j++) {
                        String argsOut[];
                        argsOut = new String[]{"-D", "OUTPUT", String.valueOf(lineNum.get(j))};
                        exec("iptables", argsOut, 5000);
                    }

                    String arg3s[];
                    arg3s = new String[]{"--line-numbers", "-L", "INPUT"};
                    iptables = exec("iptables", arg3s, 10000);

                    List<Integer> lineNum2 = getDropRuleLines(uid, iptables);
                    for (int j = 0; j < lineNum2.size(); j++) {
                        String argsOut[];
                        argsOut = new String[]{"-D", "INPUT", String.valueOf(lineNum2.get(j))};
                        exec("iptables", argsOut, 5000);
                    }

                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static int getDropRules(int uid, String iptables) {

        if (TextUtils.isEmpty(iptables)) {
            return 0;
        }

        String[] ipTableArray = iptables.split("\n");
        String uidFormat = String.format("u0_a" + uid % 1000);
        int count = 0;
        for (String ipTable : ipTableArray) {
            if (ipTable.startsWith("DROP") && ipTable.contains(uidFormat)) {
                ++count;
            }
        }
        Log.i(TAG, "getDropRules uid = " + uid + " count = " + count);
        return count;
    }

    private static List<Integer> getDropRuleLines(int uid, String iptables) {

        List<Integer> lineNum = new ArrayList<>();
        if (TextUtils.isEmpty(iptables)) {
            return lineNum;
        }

        String[] ipTableArray = iptables.split("\n");
        String uidFormat = String.format("u0_a" + uid % 1000);
        for (int i = 0; i < ipTableArray.length; i++) {
            if (ipTableArray[i].contains("DROP") && ipTableArray[i].contains(uidFormat)) {
                String[] segment = ipTableArray[i].split(" ");
                try {
                    lineNum.add(Integer.valueOf(segment[0]));
                } catch (NumberFormatException e) {
                    lineNum.add(i);
                }
            }
        }
        Log.i(TAG, "getDropRules uid = " + uid + " count = " + lineNum.size());
        return lineNum;
    }
}
