package com.example.week_recipe.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.week_recipe.R;
import com.example.week_recipe.utility.MyString;
import com.example.week_recipe.view.activity.HomePageActivity;
import com.example.week_recipe.view.activity.LoginActivity;
import com.example.week_recipe.viewModel.AddFoodToMyDailyRecipeListViewModel;
import com.example.week_recipe.viewModel.SettingsViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SettingsFragment extends Fragment {

    private View view;
    private EditText userNameEditText;
    private Button logoutButton;
    private SettingsViewModel viewModel;

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
        userNameEditText = view.findViewById(R.id.fragment_settings_userNameEditText);
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        userNameEditText.setText(viewModel.getUserName());
    }

    private void setListener()
    {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.updateUserName(s.toString());
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