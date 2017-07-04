package io.mypersonaltrainer.mypersonaltrainer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tv_hello_world;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv_hello_world = (TextView) findViewById(R.id.tv_hello_world);

        tv_hello_world.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_hello_world.setText("asdf");
            }
        });

    }
}
