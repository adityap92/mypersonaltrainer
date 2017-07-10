package io.mypersonaltrainer.mypersonaltrainer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        LoginFragment loginFrag = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container, loginFrag, "LoginFragment")
                .addToBackStack(null)
                .commit();

    }

    public static class LoginFragment extends Fragment{

        TextView tv_hello_world;

        public LoginFragment(){}

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_login, container, false);

            tv_hello_world = (TextView) rootView.findViewById(R.id.tv_hello_world);

            tv_hello_world.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_hello_world.setText("asdf");
                }
            });

            return rootView;
        }
    }
}
