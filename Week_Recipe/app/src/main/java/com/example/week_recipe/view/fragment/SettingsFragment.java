package com.example.week_recipe.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.week_recipe.R;
import com.example.week_recipe.utility.MyString;
import com.example.week_recipe.view.activity.HomePageActivity;
import com.example.week_recipe.view.activity.LoginActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SettingsFragment extends Fragment {

    private View view;
    private Button logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        bind();
        setListener();
        return view;
    }

    private void bind()
    {
        logoutButton = view.findViewById(R.id.fragment_settings_logoutButton);
    }

    private void setListener()
    {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signOut() {
        Context context = view.getContext();
        AuthUI.getInstance().signOut(context);
        MyString.deleteStringInInternalStorage("AutoLogin");
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}