package com.example.week_recipe.view.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.week_recipe.R;
import com.example.week_recipe.utility.CameraInterface;
import com.example.week_recipe.utility.MyPicture;
import com.example.week_recipe.utility.MyQRCode;
import com.example.week_recipe.view.myView.CameraSurfaceView;

import java.io.IOException;

public class QRCodeScanFragment extends Fragment{
    private final static int HAS_NEW_DECODE_MESSAGE = 1001;
    private View view;
    private CameraSurfaceView surfaceView;
    //private ImageView imageView;
    private TextView decodeTextView;
    private String decodeMessage;
    private Bitmap decodeBitmap;
    private Boolean needDecode;
    private DecodeQRCodeListener listener;
    private boolean isOpen;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HAS_NEW_DECODE_MESSAGE) {
                decodeTextView.setText(decodeMessage);
                //imageView.setImageBitmap(decodeBitmap);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_q_r_code_scan, container, false);
        init();
        bind();
        return view;
    }

    @Override
    public void onPause() {
        needDecode = false;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isOpen)
        {
            openCamera(listener);
        }
    }

    private void init()
    {
        needDecode = false;
    }

    public void bind()
    {
        surfaceView = view.findViewById(R.id.fragment_QRCodeScan_surfaceView);
        decodeTextView = view.findViewById(R.id.fragment_QRCodeScan_decodeTextView);
        //imageView = view.findViewById(R.id.fragment_QRCodeScan_imageView);
    }

    private void updateDecodeMessage(String newMessage,Bitmap bitmap)
    {
        decodeMessage = newMessage;
        decodeBitmap = bitmap;
        if (listener!=null)
        {
            listener.onDecodeNewInfo(newMessage);
        }
        Message message = new Message();
        message.what = HAS_NEW_DECODE_MESSAGE;
        handler.sendMessage(message);
    }

    public void openCamera(DecodeQRCodeListener listener)
    {
        decodeMessage = null;
        decodeBitmap = null;
        decodeTextView.setText(null);
        this.listener = listener;

        if (checkCamera())
        {
            if (needDecode)
            {
                stopCamera();
            }

            surfaceView.openCamera();
            needDecode = true;
            new Thread(()->{
                while (needDecode)
                {
                    Bitmap preview = surfaceView.getPreview();

                    if (preview!=null)
                    {
                        String message = MyQRCode.decodeQRCode(preview);
                        if (message==null)
                        {
                            preview = MyPicture.getBinarizationBitmapBaseOnOriginalBitmap(preview,true);
                            message = MyQRCode.decodeQRCode(preview);
                        }
                        if(message!=null)
                        {
                            updateDecodeMessage(message,preview);
                        }

                    }
                    else
                    {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            isOpen = true;
        }
    }

    public Bitmap getDecodeBitmap() {
        return decodeBitmap;
    }

    public String getDecodeMessage() {
        return decodeMessage;
    }

    public void setListener(DecodeQRCodeListener listener) {
        this.listener = listener;
    }

    public void stopCamera()
    {
        surfaceView.stopCamera();
        needDecode = false;
        decodeMessage = null;
        decodeBitmap = null;
        decodeTextView.setText(null);
        isOpen = false;
    }

    private boolean checkCamera()
    {
        if (surfaceView.checkCamera()) {
            Toast.makeText(getContext(), "搜索到摄像头", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(getContext(), "未检测到摄像头", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public interface DecodeQRCodeListener
    {
        void onDecodeNewInfo(String info);
    }
}