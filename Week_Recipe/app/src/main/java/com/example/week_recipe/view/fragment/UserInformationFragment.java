package com.example.week_recipe.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.model.domain.user.UserData;
import com.example.week_recipe.utility.MyPicture;
import com.example.week_recipe.viewModel.RecipeWithDateViewModel;
import com.example.week_recipe.viewModel.UserInformationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

@RequiresApi(api = Build.VERSION_CODES.O)
public class UserInformationFragment extends Fragment {

    private UserInformationViewModel viewModel;
    private LiveData<UserData> userData;
    private ImageView userImage;
    private TextView emailTextView;
    private TextView userNameTextView;
    private FloatingActionButton editImageButton;
    private final static int IMAGE_FROM_PHOTO = 1001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_information, container, false);
        bind(view);
        setListener();
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
                    userImage.setImageURI(uri);
                    viewModel.updateUserImage(MyPicture.drawableToBitmap(userImage.getDrawable()));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void bind(View view) {
        viewModel = new ViewModelProvider(this).get(UserInformationViewModel.class);
        userData = viewModel.getUserData();
        emailTextView = view.findViewById(R.id.fragment_userInformation_emailTextView);
        userNameTextView = view.findViewById(R.id.fragment_userInformation_userNameTextView);
        userImage = view.findViewById(R.id.fragment_userInformation_userImage);
        editImageButton = view.findViewById(R.id.fragment_userInformation_chooseImageButton);
        updateUserInfo(userData.getValue());
    }

    private void updateUserInfo(UserData userData)
    {
        if (userData!=null)
        {
            emailTextView.setText(userData.getEmail());
            userNameTextView.setText(userData.getUserName());
            if(userData.hasUserImage())
            {
                userImage.setImageBitmap(userData.getUserImage());

            }
            else
            {
                userImage.setImageResource(R.drawable.ic_launcher_foreground);
            }
            userImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void setListener()
    {
        userData.observe(this, new Observer<UserData>() {
            @Override
            public void onChanged(UserData userData) {
                updateUserInfo(userData);
            }
        });
        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromLocal();
            }
        });
    }

    private void selectImageFromLocal() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_FROM_PHOTO);
    }
}