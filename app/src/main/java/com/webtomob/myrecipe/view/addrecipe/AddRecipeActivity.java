package com.webtomob.myrecipe.view.addrecipe;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.webtomob.myrecipe.R;
import com.webtomob.myrecipe.database.AppDatabase;
import com.webtomob.myrecipe.database.AppExecutor;
import com.webtomob.myrecipe.model.Recipe;
import com.webtomob.myrecipe.utils.Utils;
import com.webtomob.myrecipe.viewmodel.AddRecipeViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AddRecipeActivity extends AppCompatActivity {
    private AddRecipeViewModel recipeViewModel;
    private Button mSaveButton;
    private EditText mName, mIngredient, mDuration, mSteps;
    private ImageView mImageView;
    private AppDatabase mDatabase;
    private Spinner mCatSpinner;

    private static final int CAPTURE_PHOTO = 1;
    private static final int PERMISSIONS_REQUEST_STORAGE = 2;
    private static final int PERMISSIONS_REQUEST_CAMERA = 3;
    private Uri imageUriFromCamera;
    private String image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_receipe_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.add_recipe_title));
        setSupportActionBar(toolbar);
        mDatabase = AppDatabase.getInstance(getApplicationContext());
        recipeViewModel = ViewModelProviders.of(this).get(AddRecipeViewModel.class);

        recipeViewModel.getRecipeData().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                recipe.getCookingTime();
            }
        });

        setUpUI();

    }

    private void setUpUI() {
        mSaveButton = findViewById(R.id.saveButton);
        mName = findViewById(R.id.nameEditText);
        mIngredient = findViewById(R.id.ingredientEditText);
        mDuration = findViewById(R.id.durationEditText);
        mSteps = findViewById(R.id.stepsEditText);
        mCatSpinner = findViewById(R.id.catSpinner);
        //mCatSpinner.setOnItemSelectedListener(catSpinnerListener);
        loadSpinnerData();
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationFields()) {
                    saveReceipe();
                    recipeViewModel.onClick(view);
                }
            }
        });

        mImageView = findViewById(R.id.imageView);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storagePermission();
            }
        });

        handlingScrollingOfEditText();

    }

    private boolean validationFields() {
        if (mName.getText().toString().isEmpty() || mName.getText().toString().length() == 0) {
            mName.setError(getString(R.string.name_error));
            mName.requestFocus();
            return false;
        } else if (mIngredient.getText().toString().isEmpty() || mIngredient.getText().toString().length() == 0) {
            mIngredient.setError(getString(R.string.ingredient_error));
            mIngredient.requestFocus();
            return false;
        } else if (mDuration.getText().toString().isEmpty() || mDuration.getText().toString().length() == 0) {
            mDuration.setError(getString(R.string.cooking_time_error));
            mDuration.requestFocus();
            return false;
        } else if (mSteps.getText().toString().isEmpty() || mSteps.getText().toString().length() == 0) {
            mSteps.setError(getString(R.string.steps_error));
            mSteps.requestFocus();
            return false;
        }

        return true;
    }


    /**
     * OnTouch listener to handle the inner scrolling of edittext
     */
    private void handlingScrollingOfEditText() {
        mIngredient.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (view.getId() == R.id.ingredientEditText) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        mSteps.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (view.getId() == R.id.stepsEditText) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
    }

    /**
     * Saving the tickets in database
     */
    private void saveReceipe() {
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Recipe recipe = new Recipe("", mName.getText().toString(), "", mSteps.getText().toString(),
                        mIngredient.getText().toString(), mDuration.getText().toString());
                mDatabase.receipeDao().insertRecipeItem(recipe);
            }
        });
    }

    private void loadSpinnerData() {
        final ArrayList<String> categories = new ArrayList<>();
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mDatabase.categoryDao().getAllCategory().size(); i++) {
                    categories.add(mDatabase.categoryDao().getAllCategory().get(i).getCateName());
                }
                // Stuff that updates the UI
                if (categories != null) {
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplication(), R.layout.custom_spinner_layout, categories);

                    dataAdapter.setDropDownViewResource(R.layout.custom_spinner_layout);
                    mCatSpinner.setAdapter(dataAdapter);
                    dataAdapter.notifyDataSetChanged();
                }

                mCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        String label = adapterView.getItemAtPosition(position).toString();
                        Log.e(" this spinner ", " kjhkjh " + label);
                        Toast.makeText(adapterView.getContext(), "You selected: " + label, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Log.e(" this spinner ", " kjhkjh ");
                        Toast.makeText(adapterView.getContext(), "You selected: ", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void cameraPermission() {
        int hasAccounts;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasAccounts = checkSelfPermission(Manifest.permission.CAMERA);

            if (hasAccounts != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            PERMISSIONS_REQUEST_CAMERA);

                    return;
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        PERMISSIONS_REQUEST_CAMERA);
                return;
            }
            startCameraActivity();

        } else {
            startCameraActivity();
        }
    }

    private void storagePermission() {
        int hasAccounts;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasAccounts = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasAccounts != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSIONS_REQUEST_STORAGE);

                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_STORAGE);
                return;
            }
            cameraPermission();
        } else {
            cameraPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraPermission();

                }
                break;
            case PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCameraActivity();
                }
                break;
        }
    }

    private void startCameraActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            imageUriFromCamera = Uri.fromFile(Utils.getOutputMediaFile(this));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera);
        } else {
            File file = new File(Uri.fromFile(Utils.getOutputMediaFile(this)).getPath());
            imageUriFromCamera = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_PHOTO);
            startCameraActivity();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) switch (requestCode) {
            case CAPTURE_PHOTO:
                Bitmap croppedImgBitmap;
                if (imageUriFromCamera != null) {
                    try {
                        croppedImgBitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(this).getContentResolver(), imageUriFromCamera);
                        mImageView.setImageBitmap(croppedImgBitmap);
                        image = Utils.bitMapToString(croppedImgBitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

        }
    }

    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.discard_changes))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(getString(R.string.not_saved));
        alert.show();
    }


    @Override
    public void onBackPressed() {
        alertDialog();
    }

}
