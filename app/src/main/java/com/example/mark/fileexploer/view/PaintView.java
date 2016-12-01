package com.example.mark.fileexploer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mark.fileexploer.R;

/**
 * Created by mark on 16-11-27.
 *
 * 自定义 绘画视图
 */

public class PaintView extends View {
    public PaintView(Context context) {
        super(context);
        super.setBackgroundColor(Color.WHITE);
        super.setOnTouchListener(new PaintViewOnTouchListener());
        btn_reset = new Button(context);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        btn_reset.setText("reset");
        btn_reset.setLayoutParams(params);
        btn_reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                path.reset();
                postInvalidate();
            }
        });
    }

    private Path path = new Path();
    public Button btn_reset;
    public ViewGroup.LayoutParams params;

    // 重写父类 的绘制方法
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(12);
        canvas.drawPath(path,paint);
    }

    private class PaintViewOnTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    // 创建路径 起始点
                    path.moveTo(x,y);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    // 将移动过程中的 点 加入到路径中
                    path.lineTo(x,y);
                    break;
                    default:
                        return false;
            }
            //强行绘制
            postInvalidate();
            return false;
        }
    }
}
