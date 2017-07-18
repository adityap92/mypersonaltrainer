package io.mypersonaltrainer.mypersonaltrainer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mypersonaltrainer.mypersonaltrainer.data.DBContract;
import io.mypersonaltrainer.mypersonaltrainer.utils.ExerciseHolder;


public class MainActivity extends AppCompatActivity {

    static GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    public final String TAG = MainActivity.class.getSimpleName();
    public Context mContext;
    LoginFragment loginFragment;
    ExerciseHolder exerciseHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        exerciseHolder = new ExerciseHolder(loadJSONFromAsset());

        if(savedInstanceState==null){
            loginFragment = new LoginFragment();
            openFragment(loginFragment);
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e(TAG, "Google API client Failed to Connect");
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){

            case android.R.id.home:
                onBackPressed();
                return true;
            //action when sign out is selected from menu
            case R.id.menu_sign_out:
                    signOut();
                    return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openFragment(Fragment frag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, frag)
                .commit();
    }

    public void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                openFragment(loginFragment);
            }
        });
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("exercises.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public static class LoginFragment extends Fragment{

        @BindView(R.id.sign_in_button)
        SignInButton bLogin;
        Context mContext;
        public final String TAG = LoginFragment.class.getSimpleName();

        public LoginFragment(){}

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            ButterKnife.bind(this, rootView);

            mContext = getContext();

            bLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn();
                }
            });

            return rootView;
        }

        public void signIn() {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

        private void handleSignInResult(GoogleSignInResult result) {
            Log.d(TAG, "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Sign In Successful
                GoogleSignInAccount acct = result.getSignInAccount();
                isUserExist(acct.getDisplayName());
            } else {
                // Signed out, show unauthenticated UI.
                Snackbar snackbar = Snackbar.make(getView(), getString(R.string.not_logged_in),
                        Snackbar.LENGTH_LONG);
                int snackbarTextId = android.support.design.R.id.snackbar_text;
                View view = snackbar.getView();
                TextView textView = (TextView) view.findViewById(snackbarTextId);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setTextColor(getResources().getColor(R.color.white, null));
                }
                snackbar.show();
            }
        }

        private void startBioFrag(Bundle bundle){
            BioFragment bioFrag = new BioFragment();
            bioFrag.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, bioFrag)
                    .commit();
        }

        private void isUserExist(String name){

            String[] projection = {
                    DBContract.UsersEntry.COLUMN_USER_NAME
            };

            String selection = DBContract.UsersEntry.COLUMN_USER_NAME + " = ?";
            String[] selectionArgs = {name};

            Cursor cursor = mContext.getContentResolver().query(DBContract.UsersEntry.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null,null);

            if(cursor.getCount()>0){
                Snackbar snackbar = Snackbar.make(getView(), getString(R.string.welcome_back) + " " + name +"!",
                        Snackbar.LENGTH_LONG);
                int snackbarTextId = android.support.design.R.id.snackbar_text;
                View view = snackbar.getView();
                TextView textView = (TextView) view.findViewById(snackbarTextId);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setTextColor(getResources().getColor(R.color.white, null));
                }
                snackbar.show();


                PlannerFragment plannerFragment = new PlannerFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, plannerFragment)
                        .commit();
            }
            else{
                Bundle bundle = new Bundle();
                bundle.putString("full_name", name);
                startBioFrag(bundle);
            }

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            OptionalPendingResult<GoogleSignInResult> opr = Auth
                    .GoogleSignInApi.silentSignIn(mGoogleApiClient);

            if(opr.isDone()){
                Log.d(TAG, "Sign in was cached");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            }else{
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }
    }


}
