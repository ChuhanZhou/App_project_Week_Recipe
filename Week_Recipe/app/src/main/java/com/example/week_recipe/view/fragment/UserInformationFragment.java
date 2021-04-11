package com.example.week_recipe.view.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.utility.MyPicture;
import com.google.gson.Gson;
@RequiresApi(api = Build.VERSION_CODES.O)
public class UserInformationFragment extends Fragment {

    private ImageView userImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_information, container, false);
        bind(view);
        return view;
    }

    private void bind(View view) {
        SystemModel systemModel = SystemModelManager.getSystemModelManager();
        TextView emailTextView = view.findViewById(R.id.fragment_user_information_emailTextView);
        emailTextView.setText(systemModel.getUserData().getEmail());
        TextView userNameTextView = view.findViewById(R.id.fragment_user_information_userNameTextView);
        userNameTextView.setText(systemModel.getUserData().getUserName());
        userImage = view.findViewById(R.id.fragment_user_information_userImage);

        //Drawable drawable = userImage.getBackground();
        //Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        //String a = MyPicture.convertIconToString(bitmap);
        //MyPicture.save(a);
    }

}