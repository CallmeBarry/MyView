package com.example.barry.myview;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gordonwong.materialsheetfab.MaterialSheetFab;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.ColorPicker;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.fab)
    Fab mFab;
    @BindView(R.id.overlay)
    View mOverlay;
    @BindView(R.id.paintcolor)
    TextView mPaintcolor;
    @BindView(R.id.paintsize)
    TextView mPaintsize;
    @BindView(R.id.clear)
    TextView mClear;
    @BindView(R.id.save)
    TextView mSave;
    @BindView(R.id.fab_sheet)
    View mFabSheet;
    @BindView(R.id.ConstraintLayout)
    ConstraintLayout mConstraintLayout;
    private MaterialSheetFab<Fab> mMaterialSheetFab;

    private int weight = 5;
    private int color = 5;
    private MyView mMyView;
    private int mScreenWidth;
    private int mScreenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        int sheetColor = getResources().getColor(R.color.fab_sheet_color);
        int fabColor = getResources().getColor(R.color.fab_color);

        mMaterialSheetFab = new MaterialSheetFab<>(mFab, mFabSheet, mOverlay,
                sheetColor, fabColor);

        Display defaultDisplay = getWindowManager().getDefaultDisplay();

        mScreenWidth = defaultDisplay.getWidth();
        mScreenHeight = defaultDisplay.getHeight();
        mMyView = new MyView(this, mScreenWidth, mScreenHeight);
        mConstraintLayout.addView(mMyView, 0);
        mFabSheet.bringToFront();

    }


    @Override
    public void onBackPressed() {
        if (mMaterialSheetFab.isSheetVisible()) {
            mMaterialSheetFab.hideSheet();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.paintcolor, R.id.paintsize, R.id.clear, R.id.save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.paintcolor:

                ColorPicker picker = new ColorPicker(this);
                picker.setInitColor(0xDD00DD);
                picker.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
                    @Override
                    public void onColorPicked(int pickedColor) {
                        mMyView.changPanitColor(pickedColor);
                    }
                });
                picker.show();

                break;
            case R.id.paintsize:

                View inflate = LayoutInflater.from(this).inflate(R.layout.settingpaintsize, null);

                final View mview = inflate.findViewById(R.id.view);

                final ViewGroup.LayoutParams lp = mview.getLayoutParams();

                SeekBar mSeekBar = (SeekBar) inflate.findViewById(R.id.seekBar);
                mSeekBar.setProgress(weight);
                lp.height = weight;
                mview.setLayoutParams(lp);

                mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        weight = i;
                        lp.height = i;
                        mview.setLayoutParams(lp);
                        Log.i(TAG, "onProgressChanged: " + i);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                new AlertDialog.Builder(MainActivity.this)
                        .setView(inflate)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                mMyView.changePaintWeight(weight);

                            }
                        })
                        .show().getWindow().setLayout((int) (mScreenWidth * 0.8), (int) (mScreenHeight * 0.5));

                break;
            case R.id.clear:
                mMyView.clearCanvas();

                break;
            case R.id.save:
                mMyView.saveToSDCard();
                break;
        }

        mMaterialSheetFab.hideSheet();
    }





}
