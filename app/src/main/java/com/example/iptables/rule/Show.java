package com.example.iptables.rule;

import android.util.Log;

import com.example.iptables.support.Rooter;

/**
 * @author EdisonChang
 */
public class Show extends Base {

    @Override
    protected String doInBackgroundImp() throws Exception {
        Log.i(TAG, "iptables --line-numbers -L");
        return Rooter.CommandOutput("iptables --line-numbers -L");
    }
}
