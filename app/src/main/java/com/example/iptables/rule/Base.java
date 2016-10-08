package com.example.iptables.rule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.iptables.R;

/**
 * @author EdisonChang
 */
public abstract class Base extends Activity {

    protected static final String TAG = "IpTables";

    protected TextView textView;
    protected ProgressBar progressBar;
    protected IpTablesTask ipTablesTask;

    public static void start(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    protected abstract String doInBackgroundImp() throws Exception;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iptables);

        textView = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        ipTablesTask = new IpTablesTask();
        ipTablesTask.execute();
    }

    @Override
    protected void onDestroy() {
        ipTablesTask.cancel(true);
        super.onDestroy();
    }

    protected class IpTablesTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... i) {
            try {
                return doInBackgroundImp();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "cmd output nothing...";
        }

        @Override
        protected void onPostExecute(String result) {
            if (!TextUtils.isEmpty(result)) {
                Log.i(TAG, result);
                textView.setText(result);
            } else {
                textView.setText("cmd output nothing...");
            }

            progressBar.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }
}
