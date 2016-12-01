package com.example.mark.fileexploer.activity;

import android.app.Activity;
import android.os.Bundle;
import com.example.mark.fileexploer.view.PaintView;

public class ToolActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PaintView view = new PaintView(this);
        setContentView(view);
        addContentView(view.btn_reset,view.params);

    }
}
