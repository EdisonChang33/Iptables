package com.example.iptables;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.iptables.rule.Base;
import com.example.iptables.rule.EnhanceDrop;
import com.example.iptables.rule.Forward;
import com.example.iptables.rule.Input;
import com.example.iptables.rule.Output;
import com.example.iptables.rule.Show;
import com.example.iptables.support.Rooter;
import com.stericson.RootTools.RootTools;

import java.util.ArrayList;
import java.util.List;

/**
 * @author EdisonChang
 */
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!RootTools.isAccessGiven()) {
            Toast.makeText(getBaseContext(), "Root is required", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initCmdList();
    }

    private void initCmdList() {

        List<String> cmdArray = new ArrayList<>();
        cmdArray.add(getString(R.string.show));
        cmdArray.add(getString(R.string.in));
        cmdArray.add(getString(R.string.out));
        cmdArray.add(getString(R.string.forward));
        cmdArray.add(getString(R.string.clear));
        cmdArray.add(getString(R.string.enhance_drop));

        ListAdapter listenAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cmdArray);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listenAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String action = String.valueOf(parent.getItemAtPosition(position));
                Log.i(TAG, "setOnItemClickListener " + action);

                if (!TextUtils.isEmpty(action)) {
                    if (action.equals(getString(R.string.show))) {
                        Base.start(MainActivity.this, Show.class);
                    } else if (action.equals(getString(R.string.in))) {
                        Base.start(MainActivity.this, Input.class);
                    } else if (action.equals(getString(R.string.out))) {
                        Base.start(MainActivity.this, Output.class);
                    } else if (action.equals(getString(R.string.forward))) {
                        Base.start(MainActivity.this, Forward.class);
                    } else if (action.equals(getString(R.string.clear))) {
                        clearIpTables();
                    } else if (action.equals(getString(R.string.enhance_drop))) {
                        enhanceDrop();
                    }
                }
            }
        });
    }

    private void enhanceDrop() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("ensure to check and drop the harmful rules?").setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EnhanceDrop.execIpTables();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.create();
        builder.show();
    }

    private void clearIpTables() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("ensure to clear the Iptables?").setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    Rooter.CommandNoOutput("iptables -F");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getBaseContext(), "clear", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.create();
        builder.show();
    }
}
