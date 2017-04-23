package com.example.barry.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaScannerConnection;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by barry on 17-4-20.
 */

public class MyView extends View {

    private Context context;
    private Paint mPaint;
    private Path mPath;


    private int mColor;
    private int mWeight;

    private int mScreenWidth;
    private int mScreenHeight;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    public MyView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;
        this.mScreenWidth = screenWidth;
        this.mScreenHeight = screenHeight;
        initCanvas();
    }

    private void initCanvas() {
        initPaint();

        mBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(Color.argb(255, 255, 255, 255));
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.TRANSPARENT);
    }

    private void initPaint() {
        mPaint = new Paint();
        mColor = Color.BLACK;
        mWeight = 5;
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mWeight);
        mPaint.setStyle(Paint.Style.STROKE);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        if (mPath != null)
            canvas.drawPath(mPath, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath = new Path();
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mCanvas.drawPath(mPath, mPaint);
                mPath = null;// 重新置空
                invalidate();

        }


        return true;
    }

    public void changPanitColor(int color) {
        this.mColor = color;
        mPaint.setColor(mColor);
    }

    public void changePaintWeight(int weight) {
        this.mWeight = weight;
        mPaint.setStrokeWidth(mWeight);
    }

    public void clearCanvas() {
        mBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(Color.argb(255, 255, 255, 255));
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.TRANSPARENT);
        invalidate();
    }


    public void saveToSDCard() {
        //获得系统当前时间，并以该时间作为文件名
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate) + "paint.JPEG";
        File file = new File("sdcard/" + str);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mBitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                fos);

        MediaScannerConnection.scanFile(context, new String[]{"sdcard/"+ str}
                , null, null);
        Log.e("TAG", "图片已保存");
        Toast.makeText(context, "图片已保存", Toast.LENGTH_SHORT).show();
    }
}
