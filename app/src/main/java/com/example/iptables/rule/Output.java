package com.example.iptables.rule;

import com.example.iptables.support.Rooter;

/**
 * @author EdisonChang
 */
public class Output extends ActionBase {

    @Override
    protected String doInBackgroundImp() throws Exception {
        return Rooter.CommandOutput("iptables --line-numbers -L OUTPUT");
    }

    @Override
    protected String getAddRule() {
        return "iptables -A OUTPUT";
    }

    @Override
    protected String getDelRule() {
        return "iptables -D OUTPUT ";
    }
}
