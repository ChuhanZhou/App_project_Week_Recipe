package com.example.week_recipe.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.week_recipe.R;

public class PopupSetStringFragment extends Fragment {

    private View view;
    private TextView setStringTitleTextView;
    private EditText stringEditText;
    private Button confirmButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popup_set_string, container, false);
    }

    public PopupSetStringFragment()
    {
    }

    public PopupSetStringFragment(View view,String stringTitle)
    {
        this.view = view;
        bind();
        setStringTitleTextView.setText(stringTitle);
    }

    public PopupSetStringFragment(View view,int id)
    {
        this.view = view;
        bind();
        setStringTitleTextView.setText(view.getContext().getText(id));
    }

    private void bind()
    {
        setStringTitleTextView = view.findViewById(R.id.fragment_popupSetString_setStringTitleTextView);
        stringEditText = view.findViewById(R.id.fragment_popupSetString_stringEditText);
        confirmButton = view.findViewById(R.id.fragment_popupSetString_confirmButton);
    }

    public EditText getStringEditText() {
        return stringEditText;
    }

    public Button getConfirmButton() {
        return confirmButton;
    }
}