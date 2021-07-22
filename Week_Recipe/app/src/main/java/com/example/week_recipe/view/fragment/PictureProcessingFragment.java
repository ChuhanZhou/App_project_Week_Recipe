package com.example.week_recipe.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.week_recipe.R;
import com.example.week_recipe.utility.MyPicture;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class PictureProcessingFragment extends Fragment {
    private final static int IMAGE_FROM_PHOTO = 1001;
    private final static int HAS_NEW_IMAGE = 1002;

    private View view;
    private ImageView imageView;
    private TextView titleTextView;

    private ArrayList<Bitmap> showList;
    private ArrayList<String> titleList;
    private int showIndex;
    private FloatingActionButton chooseImageButton;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HAS_NEW_IMAGE) {
                imageView.setImageBitmap(showList.get(showList.size()-1));
                titleTextView.setText(titleList.get(showList.size()-1));
                showIndex = showList.size()-1;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_picture_processing, container, false);
        init();
        bind(view);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode)
        {
            case IMAGE_FROM_PHOTO:
                if (data!=null)
                {
                    Uri uri = data.getData();
                    imageView.setImageURI(uri);

                    Bitmap read = MyPicture.drawableToBitmap(imageView.getDrawable());

                    showList.clear();
                    titleList.clear();

                    showIndex = 0;
                    showList.add(read);
                    titleList.add("原图");
                    imageView.setImageBitmap(showList.get(0));
                    titleTextView.setText(titleList.get(0));

                    new Thread(()->{
                        //Bitmap compression = MyPicture.bitmapCompressionOnSize(read,1f);
                        //showList.add(compression);
                        //titleList.add("原图->压缩");
                        //updateNewImage();
//
                        //Bitmap brightnessBackground = MyPicture.getBrightnessBackground(compression,5);
                        //showList.add(brightnessBackground);
                        //titleList.add("原图->压缩->亮度背景11x11");
                        //updateNewImage();
//
                        //Bitmap brightnessAdjustment = MyPicture.brightnessAdjustment(compression,brightnessBackground);
                        //showList.add(brightnessAdjustment);
                        //titleList.add("原图->压缩->亮度背景11x11->亮度调整");
                        //updateNewImage();
//
                        //showList.add(MyPicture.getBinarizationBitmapByPixel(brightnessAdjustment));
                        //titleList.add("原图->压缩->亮度背景11x11->亮度调整->二值化");
                        //updateNewImage();

                        showList.add(MyPicture.getBinarizationBitmapBaseOnOriginalBitmap(read));
                        titleList.add("原图->灰度化->压缩->计算亮度背景（样本范围11x11）->亮度调整->二值化");
                        updateNewImage();

                        showList.add(MyPicture.getBinarizationBitmapByPixel(MyPicture.bitmapCompressionOnSize(read,2.5f)));
                        titleList.add("原图->压缩->二值化");
                        updateNewImage();
                    }).start();
                }
                break;
        }
    }

    private void updateNewImage()
    {
        Message message = new Message();
        message.what = HAS_NEW_IMAGE;
        handler.sendMessage(message);
    }

    private void init()
    {
        showList = new ArrayList<>();
        titleList = new ArrayList<>();
        showIndex = -1;
    }

    private void bind(View view)
    {
        imageView = view.findViewById(R.id.fragment_pictureProcessing_imageView);
        titleTextView = view.findViewById(R.id.fragment_pictureProcessing_titleTextView);
        chooseImageButton = view.findViewById(R.id.fragment_pictureProcessing_chooseImageButton);
        setListener();
    }

    private void setListener()
    {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showList.size()>0)
                {
                    if (showIndex>=-1&&showIndex<showList.size()-1)
                    {
                        showIndex++;
                    }
                    else if (showIndex==showList.size()-1)
                    {
                        showIndex = 0;
                    }
                    imageView.setImageBitmap(showList.get(showIndex));
                    titleTextView.setText(titleList.get(showIndex));
                }
            }
        });
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromLocal();
            }
        });
    }

    private void selectImageFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19)
        {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        }
        else
        {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent,IMAGE_FROM_PHOTO);
    }
}