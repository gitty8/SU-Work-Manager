package com.version.stutijindal.approject;

/*
Created by Stuti Jindal on 04/10/17

Fragment Features:
-Firebase Database and Auth
-Current User Image
-Upload Image from Camera/Gallery
-Current User Information
-Update Current User Information and Profile Picture and Save to Firebase
-Delete Current User from Firebase Database
-Logout Current User
-Keyboard adjust
-Snackbar
-Toast
-Alert Dialog
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    String name, email;
    Uri photoUrl;
    EditText tname, temail, tpassword;
    ImageView image;
    Button logout, save, delete;
    FloatingActionButton camera;
    Intent intent;
    int name_flag = 0, email_flag = 0, photo_flag = 0;
    FirebaseUser user;
    String new_name, new_email;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    Bitmap thumbnail;
    private boolean imageGreaterThan5MB;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        image = (ImageView) view.findViewById(R.id.image);
        tname = (EditText) view.findViewById(R.id.name);
        temail = (EditText) view.findViewById(R.id.email);
        logout = (Button) view.findViewById(R.id.logout);
        save = (Button) view.findViewById(R.id.save);
        delete = (Button) view.findViewById(R.id.delete);
        camera = (FloatingActionButton) view.findViewById(R.id.camera);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d("in image click", "");
                selectImage();
                photo_flag = 1;
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            name = user.getDisplayName();
            Log.d("name", "" + name);
            email = user.getEmail();
            Log.d("email", "" + email);
            photoUrl = user.getPhotoUrl();
            Log.d("photoURL", "" + photoUrl);
            if (photoUrl != null)
                Picasso.with(getActivity()).load(photoUrl).into(image);
            tname.setText(name);
            temail.setText(email);
        }

        tname.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                new_name = tname.getText().toString();
                Log.d("NewName", "Value is " + new_name);
                if (new_name.trim().length() != 0) {
                    name_flag = 1;
                    save.setEnabled(true);

                } else {
                    name_flag = 0;
                    save.setEnabled(false);


                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        temail.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                new_email = temail.getText().toString();
                Log.v("", "Value is " + new_email);
                if (new_email.trim().length() != 0) {
                    email_flag = 1;
                    save.setEnabled(true);

                } else {
                    email_flag = 0;
                    save.setEnabled(false);

                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }

        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(Html.fromHtml("<font color='#0D1D37'>Are you sure you want to logout?</font>"))
                        .setIcon(R.drawable.warn)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getActivity(), "You have logged out!",
                                        Toast.LENGTH_LONG).show();
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                auth.signOut();
                                intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(Html.fromHtml("<font color='#0D1D37'>Are you sure you want to delete your account permanently?</font>"))
                        .setIcon(R.drawable.warn)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getActivity(), "User Account Has Been Deleted!",
                                        Toast.LENGTH_LONG).show();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("Delete", "User account deleted.");
                                                }
                                            }
                                        });
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                auth.signOut();
                                intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        
                    }
                }).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (photo_flag == 1) {
                                            if (!imageGreaterThan5MB) {
                                                Uri uri = getImageUri(getContext(), thumbnail);
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setPhotoUri(uri)
                                                        .build();

                                                user.updateProfile(profileUpdates)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                Snackbar snackbar = Snackbar.make(tname, "Profile Picture updated!", Snackbar.LENGTH_LONG);
                                                                snackbar.show();
                                                                if (task.isSuccessful()) {
                                                                    Log.d("UserProfilePic", "Profile picture updated!");
                                                                }
                                                            }
                                                        });
                                            } else {
                                                Picasso.with(getActivity()).load(R.drawable.com_facebook_profile_picture_blank_square).into(image);
                                                Snackbar snackbar = Snackbar.make(tname, "Picture size can not be greater than 5 MB.", Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                            }
                                        }

                                        if (name_flag == 1) {
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(new_name)
                                                    .build();

                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            Snackbar snackbar = Snackbar.make(tname, "Name updated!", Snackbar.LENGTH_LONG);
                                                            snackbar.show();
                                                            if (task.isSuccessful()) {
                                                                Log.d("UserName", "Your name has been updated!");
                                                            }
                                                        }
                                                    });
                                        }

                                        if (email_flag == 1) {
                                            user.updateEmail(new_email)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Snackbar snackbar = Snackbar.make(temail, "Email address updated!", Snackbar.LENGTH_LONG);
                                                                snackbar.show();
                                                                Log.d("User EmailID", " Email address updated.");
                                                            }
                                                        }
                                                    });
                                            user.sendEmailVerification()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getActivity(), "An email verification has been sent to your account!",
                                                                        Toast.LENGTH_LONG).show();
                                                                Log.d("Email Verification", "Email sent.");
                                                            }
                                                        }
                                                    });
                                        }

                                        name_flag = 0;
                                        email_flag = 0;
                                        photo_flag = 0;
                                    }

                                }
        );


        return view;
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(Html.fromHtml("<font color='#0D1D37'>Add a Photo</font>"));
        builder.setIcon(R.drawable.com_facebook_profile_picture_blank_portrait);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        long fileSizeInBytes = thumbnail.getAllocationByteCount();
        long fileSizeInKB = fileSizeInBytes / 1024;
        long fileSizeInMB = fileSizeInKB / 1024;

        if (fileSizeInMB > 5) {
            imageGreaterThan5MB = true;
            Picasso.with(getActivity()).load(R.drawable.com_facebook_profile_picture_blank_square).into(image);
            Snackbar snackbar = Snackbar.make(tname, "Picture size can not be greater than 5 MB.", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            imageGreaterThan5MB = false;
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            image.setImageBitmap(thumbnail);
            }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        thumbnail = null;
        if (data != null) {
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long fileSizeInBytes = thumbnail.getAllocationByteCount();
        long fileSizeInKB = fileSizeInBytes / 1024;
        long fileSizeInMB = fileSizeInKB / 1024;

        if (fileSizeInMB > 5) {
            imageGreaterThan5MB = true;
            Picasso.with(getActivity()).load(R.drawable.com_facebook_profile_picture_blank_square).into(image);
            Snackbar snackbar = Snackbar.make(tname, "Picture size can not be greater than 5 MB.", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            imageGreaterThan5MB = false;
            image.setImageBitmap(thumbnail);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        File file = new File(path);
        long fileSizeInBytes = file.length();
        long fileSizeInKB = fileSizeInBytes / 1024;
        long fileSizeInMB = fileSizeInKB / 1024;

        if (fileSizeInMB > 5) {
            imageGreaterThan5MB = true;
        } else imageGreaterThan5MB = false;
        return Uri.parse(path);
    }


}
