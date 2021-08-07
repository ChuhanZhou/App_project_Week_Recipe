package com.example.week_recipe.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.week_recipe.R;


public class PopupQRCodeFragment extends Fragment {

    private View view;
    private ImageView showQRCodeImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_popup_q_r_code, container, false);
    }

    public PopupQRCodeFragment(View view, Bitmap QRCode)
    {
        this.view = view;
        bind();
        showQRCodeImageView.setImageBitmap(QRCode);
    }

    public void bind()
    {
        showQRCodeImageView = view.findViewById(R.id.fragment_popupQRCode_showQRCodeImageView);
    }
}