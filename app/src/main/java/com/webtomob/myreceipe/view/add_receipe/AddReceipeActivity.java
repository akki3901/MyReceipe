package com.webtomob.myreceipe.view.add_receipe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.webtomob.myreceipe.R;
import com.webtomob.myreceipe.database.AppDatabase;
import com.webtomob.myreceipe.database.AppExecutor;
import com.webtomob.myreceipe.model.Receipe;
import com.webtomob.myreceipe.viewmodel.AddReceipeViewModel;


public class AddReceipeActivity extends AppCompatActivity {
    private AddReceipeViewModel receipeViewModel;
    private Button mSaveButton;
    private EditText mName, mIngredient, mDuration, mSteps;
    private ImageView mImageView;
    private AppDatabase mDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_receipe_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.add_receipe_title));
        setSupportActionBar(toolbar);
        mDatabase = AppDatabase.getInstance(getApplicationContext());
        receipeViewModel = ViewModelProviders.of(this).get(AddReceipeViewModel.class);

        receipeViewModel.getReceipeData().observe(this, new Observer<Receipe>() {
            @Override
            public void onChanged(Receipe receipe) {
                receipe.getCookingTime();
                Log.v(" this is dummy", receipe.getCookingTime());
            }
        });

        setUpUI();

    }

    private void setUpUI(){
        mSaveButton = findViewById(R.id.saveButton);
        mName = findViewById(R.id.nameEditText);
        mIngredient = findViewById(R.id.ingredientEditText);
        mDuration = findViewById(R.id.durationEditText);
        mSteps = findViewById(R.id.stepsEditText);


        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validationFields()) {
                    saveReceipe();
                    receipeViewModel.onClick(view);
                }
            }
        });

        handlingScrollingOfEditText();

    }

    private boolean validationFields(){
        if(mName.getText().toString().isEmpty() || mName.getText().toString().length()==0){
            mName.setError(getString(R.string.name_error));
            mName.requestFocus();
            return false;
        }else if(mIngredient.getText().toString().isEmpty() || mIngredient.getText().toString().length()==0){
            mIngredient.setError(getString(R.string.ingredient_error));
            mIngredient.requestFocus();
            return false;
        }else if(mDuration.getText().toString().isEmpty() || mDuration.getText().toString().length()==0){
            mDuration.setError(getString(R.string.cooking_time_error));
            mDuration.requestFocus();
            return false;
        }else if(mSteps.getText().toString().isEmpty() || mSteps.getText().toString().length()==0){
            mSteps.setError(getString(R.string.steps_error));
            mSteps.requestFocus();
            return false;
        }

        return true;
    }



    /**
     * OnTouch listener to handle the inner scrolling of edittext
     */
    private void handlingScrollingOfEditText(){
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
    private void saveReceipe(){
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Receipe receipe = new Receipe("", mName.getText().toString(), "", mSteps.getText().toString(),
                        mIngredient.getText().toString(), mDuration.getText().toString());
                mDatabase.receipeDao().insertReceipeItem(receipe);
                /*Ticket ticket = new Ticket(1, Utils.currentDate(), response, SessionManager.getInstance(fragment.mContext).getUserEmail());
                if (mDatabase.ticketDao().getAllTickets().size() == 0) {
                    mDatabase.ticketDao().insertTicket(ticket);
                } else {
                    mDatabase.ticketDao().updateTicket(ticket);
                }*/
            }
        });
    }

    @Override
    public void onBackPressed() {
        alertDialog();
    }


    private void alertDialog(){
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
}


/*
public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();

    private MutableLiveData<LoginUser> userMutableLiveData;

    public MutableLiveData<LoginUser> getUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;

    }

    public void onClick(View view) {

        LoginUser loginUser = new LoginUser(EmailAddress.getValue(), Password.getValue());

        userMutableLiveData.setValue(loginUser);

    }

}*/





    /*private LoginViewModel loginViewModel;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        binding.setLifecycleOwner(this);

        binding.setLoginViewModel(loginViewModel);

        loginViewModel.getUser().observe(this, new Observer<LoginUser>() {
            @Override
            public void onChanged(@Nullable LoginUser loginUser) {

                if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getStrEmailAddress())) {
                    binding.txtEmailAddress.setError("Enter an E-Mail Address");
                    binding.txtEmailAddress.requestFocus();
                }
                else if (!loginUser.isEmailValid()) {
                    binding.txtEmailAddress.setError("Enter a Valid E-mail Address");
                    binding.txtEmailAddress.requestFocus();
                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getStrPassword())) {
                    binding.txtPassword.setError("Enter a Password");
                    binding.txtPassword.requestFocus();
                }
                else if (!loginUser.isPasswordLengthGreaterThan5()) {
                    binding.txtPassword.setError("Enter at least 6 Digit password");
                    binding.txtPassword.requestFocus();
                }
                else {
                    binding.lblEmailAnswer.setText(loginUser.getStrEmailAddress());
                    binding.lblPasswordAnswer.setText(loginUser.getStrPassword());
                }

            }
        });

    }*/


/*
<data>
<variable
            name="LoginViewModel"
                    type="com.example.umangburman.databindingwithlivedata.ViewModel.LoginViewModel" />
</data>*/
