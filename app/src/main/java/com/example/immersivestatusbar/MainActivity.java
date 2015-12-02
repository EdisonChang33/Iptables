package com.example.immersivestatusbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.helper.SystemBarTint;

/**
 * @author EdisonChang
 */
public class MainActivity extends BaseActivity {

    private SystemBarTint mtintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtintManager = new SystemBarTint(this);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImmersiveActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        setStatusBarColor(Color.parseColor("#ff0099cc"));
    }

    protected void setStatusBarColor(int colorBurn) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = setImmerseLayout();
            window.setStatusBarColor(colorBurn);
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mtintManager != null) {
                mtintManager.setStatusBarTintEnabled(true);
                mtintManager.setStatusBarTintColor(colorBurn);
            }
        }
    }
}
