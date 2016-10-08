package com.example.iptables.rule;

import com.example.iptables.support.Rooter;

/**
 * @author EdisonChang
 */
public class Input extends ActionBase {

    @Override
    protected String doInBackgroundImp() throws Exception {
        return Rooter.CommandOutput("iptables --line-numbers -L INPUT");
    }

    @Override
    protected String getAddRule() {
        return "iptables -A INPUT";
    }

    @Override
    protected String getDelRule() {
        return "iptables -D INPUT ";
    }
}
