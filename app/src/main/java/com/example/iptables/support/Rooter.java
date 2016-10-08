package com.example.iptables.support;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeoutException;

/**
 * @author EdisonChang
 */
public class Rooter {

    private static final int BUFFER_SIZE = 4096;

    public static void CommandNoOutput(String Command) throws RootDeniedException, IOException, TimeoutException, InterruptedException{
        CommandCapture command = new CommandCapture(0, Command);

        RootTools.getShell(true).add(command);
    }

    public static String CommandOutput(String cmd) throws Exception {
        Process p = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(p.getOutputStream());
        InputStream is = p.getInputStream();
        String result = null;
        os.writeBytes(cmd + "\n");
        int readed = 0;
        byte[] buff = new byte[BUFFER_SIZE];
        int count = 0;
        while (is.available() <= 0 && count < 4) {
            try {
                count++;
                Thread.sleep(2000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        while (is.available() > 0) {
            readed = is.read(buff);
            if (readed <= 0) break;
            result = new String(buff, 0, readed);
        }
        os.writeBytes("exit\n");
        os.flush();
        return result;
    }
}
