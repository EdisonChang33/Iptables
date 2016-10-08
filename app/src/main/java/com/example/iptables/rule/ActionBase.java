package com.example.iptables.rule;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.iptables.R;
import com.example.iptables.support.Rooter;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author EdisonChang
 */
public abstract class ActionBase extends Base {


    protected abstract String getAddRule();

    protected abstract String getDelRule();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.add:
                add();
                return true;
            case R.id.del:
                del();
                return true;
            case R.id.refresh:
                refresh();
                return true;
            default:
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void refresh() {
        textView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        if (ipTablesTask.getStatus() == AsyncTask.Status.RUNNING) {
            ipTablesTask.cancel(true);
        }

        ipTablesTask = new IpTablesTask();
        ipTablesTask.execute();
    }


    private void del() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_del, null);

        final EditText rule = (EditText) layout.findViewById(R.id.editText);

        builder.setView(layout).
                setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String num = null;
                        try {
                            num = rule.getText().toString();

                            Rooter.CommandNoOutput(getDelRule() + num);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getBaseContext(), getDelRule() + num, Toast.LENGTH_SHORT).show();

                        refresh();
                    }
                })
                .setNegativeButton(R.string.abort, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.create();
        builder.show();

    }

    private void add() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_add, null);

        final List<String> interfaces = new ArrayList<>();
        interfaces.add("");

        try {
            for (Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces(); list.hasMoreElements(); ) {
                NetworkInterface i = list.nextElement();
                if (i.isUp()) {
                    interfaces.add(i.getDisplayName());
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        final Spinner spinner = (Spinner) layout.findViewById(R.id.spinner);
        final Spinner spinner2 = (Spinner) layout.findViewById(R.id.spinner2);
        final Spinner spinner3 = (Spinner) layout.findViewById(R.id.spinner3);

        final EditText source = (EditText) layout.findViewById(R.id.editText);
        final EditText dest = (EditText) layout.findViewById(R.id.editText2);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.proto, android.R.layout.simple_spinner_item);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.action, android.R.layout.simple_spinner_item);
        ArrayAdapter adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, interfaces);

        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);


        builder.setView(layout).
                setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String sip = source.getText().toString();
                        String dip = dest.getText().toString();

                        String proto = spinner.getSelectedItem().toString();
                        String action = spinner2.getSelectedItem().toString();
                        String intf = spinner3.getSelectedItem().toString();

                        String tmp = getAddRule();
                        if (!TextUtils.isEmpty(sip))
                            tmp = tmp + " -s " + sip;
                        if (!TextUtils.isEmpty(dip))
                            tmp = tmp + " -d " + dip;
                        if (!TextUtils.isEmpty(intf))
                            tmp = tmp + " -i " + intf;
                        if (!TextUtils.isEmpty(proto))
                            tmp = tmp + " -p " + proto;
                        if (!TextUtils.isEmpty(action))
                            tmp = tmp + " -j " + action;

                        if (TextUtils.isEmpty(action) || tmp.equals(getAddRule()))
                            Toast.makeText(getBaseContext(), "rule invalid..", Toast.LENGTH_SHORT).show();
                        else {
                            try {
                                Rooter.CommandNoOutput(tmp);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getBaseContext(), tmp, Toast.LENGTH_SHORT).show();
                        }

                        refresh();
                    }
                })
                .setNegativeButton(R.string.abort, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.create();
        builder.show();
    }
}
