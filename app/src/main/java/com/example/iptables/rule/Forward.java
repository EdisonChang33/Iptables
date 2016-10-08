package com.example.iptables.rule;

import com.example.iptables.support.Rooter;

/**
 * @author EdisonChang
 */
public class Forward extends ActionBase{

    @Override
    protected String doInBackgroundImp() throws Exception {
        return Rooter.CommandOutput("iptables --line-numbers -L FORWARD");
    }

    @Override
    protected String getAddRule() {
        return "iptables -A FORWARD";
    }

    @Override
    protected String getDelRule() {
        return "iptables -D FORWARD ";
    }

}
