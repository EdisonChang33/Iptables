package com.example.immersivestatusbar;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * @author EdisonChang
 */
public class ImmersiveActivity extends BaseActivity {

    private View viewStatusBarTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immer);
        immerseLayout();
    }

    protected void immerseLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setImmerseLayout();

            viewStatusBarTop = findViewById(R.id.status_bar_top);
            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) viewStatusBarTop.getLayoutParams();
            params2.height = statusBarHeight;
            viewStatusBarTop.setLayoutParams(params2);
            viewStatusBarTop.setVisibility(View.VISIBLE);
        }
    }
}
