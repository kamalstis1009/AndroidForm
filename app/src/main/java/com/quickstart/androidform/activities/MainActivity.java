package com.quickstart.androidform.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.quickstart.androidform.R;
import com.quickstart.androidform.models.User;
import com.quickstart.androidform.repositories.room.AppDatabase;
import com.quickstart.androidform.viewmodels.UserViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ACTION_PICK_REQUEST_CODE = 1;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 2;
    private ImageView imageView;
    private EditText name, birth;
    private RadioGroup radioGroup;
    private LinearLayout checkboxLayout;

    private String imageName;
    private String imagePath;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.user_image);
        name = (EditText) findViewById(R.id.user_name);
        birth = (EditText) findViewById(R.id.user_birth);
        radioGroup = (RadioGroup) findViewById(R.id.user_gender_group);
        //RadioButton male = (RadioButton) findViewById(R.id.user_male);
        //RadioButton female = (RadioButton) findViewById(R.id.user_female);
        checkboxLayout = (LinearLayout) findViewById(R.id.checkbox_group);
        //CheckBox student = (CheckBox) findViewById(R.id.user_bangla);
        //CheckBox employee = (CheckBox) findViewById(R.id.user_english);

        imageView.setOnClickListener(new ActionEvent());
        birth.setOnClickListener(new ActionEvent());
        ((Button) findViewById(R.id.user_submit)).setOnClickListener(new ActionEvent());


        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        getAllUser();
    }

    private class ActionEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.user_image) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    getImage();
                }
            }
            if (v.getId() == R.id.user_birth) {
                getDate();
            }
            if (v.getId() == R.id.user_submit) {
                String id = UUID.randomUUID().toString();
                String n = name.getText().toString();
                String b = birth.getText().toString();
                String g = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();

                User user = new User(id, encodeFromImage(), n, b, g, getCheckboxValue());
                if (!n.isEmpty() && !b.isEmpty() && !g.isEmpty()) {
                    //saveData(user);
                    saveIntoDatabase(user);
                }
                if (imagePath != null) {
                    ((ImageView) findViewById(R.id.load_image)).setImageBitmap(loadImage());
                }
            }
        }
    }

    //====================================================| DatePicker
    private void getDate() {
        DatePicker datePicker = new DatePicker(this);
        int day = datePicker.getDayOfMonth();
        int mon = datePicker.getMonth();
        int year = datePicker.getYear();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birth.setText(dayOfMonth +"/"+ (month+1) +"/"+ year);
            }
        }, year, mon, day);
        dialog.show();
    }

    //====================================================| Checkbox
    private String getCheckboxValue() {
        StringBuilder value = new StringBuilder();
        for(int i=0; i<checkboxLayout.getChildCount(); i++) {
            CheckBox cb = (CheckBox) checkboxLayout.getChildAt(i);
            if (cb.isClickable()) {
                value.append(cb.getText().toString()).append(",");
            }
        }
        return value.toString();
    }

    //====================================================| For Image
    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ACTION_PICK_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==ACTION_PICK_REQUEST_CODE && resultCode==RESULT_OK && data != null) {
            Uri uri = data.getData();
            imageView.setImageURI(uri);
            imageName = "img_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".png";
            imagePath = saveToInternalStorage(((BitmapDrawable)imageView.getDrawable()).getBitmap());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==READ_EXTERNAL_STORAGE_REQUEST_CODE && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            getImage();
        }
    }

    private String encodeFromImage() {
        String encode = null;
        if (imageView.getVisibility()==View.VISIBLE) {
            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            //String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"Title",null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                encode = Base64.getEncoder().encodeToString(stream.toByteArray());
            } else {
                encode = android.util.Base64.encodeToString(stream.toByteArray(), android.util.Base64.DEFAULT);
            }

            //decode
            //byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
            //String decodedString = new String(decodedBytes);

            //https://github.com/elye/demo_android_base64_image/tree/master/app/src/main/java/com/elyeproj/base64imageload
            //val bitmap = BitmapFactory.decodeResource(resources, resourceId)
            //return bitmap.encodeBitmapIntoBase64(compressFormat, quality)
        }
        return encode;
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        File directory = new File(getFilesDir() + "/UsersPhoto/");
        directory.mkdir(); //Create imageDir
        File file = new File(directory, imageName);
        try {
            OutputStream output = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, output); // Compress into png format image from 0% - 100%
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    private Bitmap loadImage(){
        Bitmap bitmap = null;
        try {
            File file = new File(imagePath, imageName);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //====================================================| Save Button
    private void saveData(User user) {
        Log.d(TAG, user.getUserLanguage() + new Gson().toJson(user));
        Snackbar.make(findViewById(android.R.id.content), ""+new Gson().toJson(user), Snackbar.LENGTH_INDEFINITE).show();
    }


    //====================================================| Room
    private void saveIntoDatabase(User user) {
        long result = mUserViewModel.insert(user);
        Toast.makeText(MainActivity.this, ""+result, Toast.LENGTH_SHORT).show();
    }

    private void getAllUser() {
        mUserViewModel.getAll().observe(MainActivity.this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null) {
                    Log.d(TAG, new Gson().toJson(users));
                }
            }
        });

    }


}
