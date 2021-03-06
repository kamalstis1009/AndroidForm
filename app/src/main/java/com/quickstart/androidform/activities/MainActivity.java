package com.quickstart.androidform.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quickstart.androidform.BuildConfig;
import com.quickstart.androidform.R;
import com.quickstart.androidform.models.User;
import com.quickstart.androidform.utils.Utility;
import com.quickstart.androidform.viewmodels.UserViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ACTION_PICK_REQUEST_CODE = 1;
    private static final int REQUEST_CODE = 2;
    private static final int REQUEST_CODE_CAMERA = 3;
    private static final int REQUEST_IMAGE_CAPTURE = 4;
    private ImageView imageView;
    private EditText name, birth;
    private Spinner districts;
    private RadioGroup radioGroup;
    private LinearLayout checkboxLayout;

    private String imageName;
    private String imagePath;
    private UserViewModel mUserViewModel;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.user_image);
        name = (EditText) findViewById(R.id.user_name);
        birth = (EditText) findViewById(R.id.user_birth);
        districts = (Spinner) findViewById(R.id.user_district);
        districts.setSelection(1);
        //String[] str = this.getResources().getStringArray(R.array.districts);
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
                showDialog();
            }
            if (v.getId() == R.id.user_birth) {
                Utility.getDate(MainActivity.this, birth);
            }
            if (v.getId() == R.id.user_submit) {
                String id = UUID.randomUUID().toString();
                String n = name.getText().toString();
                String b = birth.getText().toString();
                String d = districts.getSelectedItem().toString();
                String g = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();

                User user = new User(id, Utility.bitmapToBase64(imageView), n, b, d, g, Utility.getCheckboxValue(checkboxLayout));
                if (!n.isEmpty() && !b.isEmpty() && !g.isEmpty()) {
                    //saveData(user);
                    saveIntoDatabase(user);
                }
                if (imagePath != null) {
                    ((ImageView) findViewById(R.id.load_image)).setImageBitmap(Utility.loadImage(imagePath, imageName));
                }
            }
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_photo_option, null, false);
        builder.setView(view);
        builder.setCancelable(true);
        builder.create();
        AlertDialog dialog = builder.show();
        ((ImageButton) view.findViewById(R.id.camera_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                } else {
                    getCamera();
                }
                dialog.dismiss();
            }
        });
        ((ImageButton) view.findViewById(R.id.gallery_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                } else {
                    getImage();
                }
                dialog.dismiss();
            }
        });
    }

    //====================================================| For Image
    private void getCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                    Log.d(TAG, "Image Uri: "+photoURI);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,/* prefix */".jpg",/* suffix */storageDir/* directory */);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

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
            Bitmap bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            if (bm != null) {
                imagePath = Utility.saveToInternalStorage(MainActivity.this, bm, imageName);
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && currentPhotoPath != null) {
            Uri uri = Uri.fromFile(new File(currentPhotoPath));
            imageView.setImageURI(uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_CODE && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            getImage();
        }
        if (requestCode == REQUEST_CODE_CAMERA) {
            getCamera();
        }
    }

    //====================================================| Save Button
    private void saveData(User user) {
        Log.d(TAG, user.getUserLanguage() + new Gson().toJson(user));
        ((TextView) findViewById(R.id.user_details)).setText(user.getUserName()+user.getUserBirth()+user.getUserDistrict()+user.getUserGender()+user.getUserLanguage());
        //Snackbar.make(findViewById(android.R.id.content), ""+new Gson().toJson(user), Snackbar.LENGTH_INDEFINITE).show();
    }


    //====================================================| Room
    private void saveIntoDatabase(User user) {
        long result = mUserViewModel.insert(user);
        //String value = new Gson().toJson(user);
        //((TextView) findViewById(R.id.user_details)).setText(value);
        Toast.makeText(MainActivity.this, "saveIntoDatabase " + result, Toast.LENGTH_SHORT).show();
    }

    private boolean disable;
    private void getAllUser() {
        mUserViewModel.getAll().observe(MainActivity.this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null) {
                    Log.d(TAG, "getAllUser() " + new Gson().toJson(users));
                    for (User u: users) {
                        if (u != null && !disable) {
                            disable = true;
                            Bitmap bitmap = Utility.base64ToBitmap(u.getUserImageUrl());
                            if (bitmap != null) {
                                ((ImageView) findViewById(R.id.load_image)).setImageBitmap(bitmap);
                            }
                            Log.d(TAG, "getAllUser() " + users.get(0).getUserImageUrl());
                        }
                    }
                }
            }
        });

    }


}
