package com.example.android.fillthegrid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    @BindView(R.id.start_game) TextView startGameView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        startGameView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        int id = view.getId();

        switch(id){
            case R.id.start_game:
                Intent intent = new Intent(this, GameActivity.class);
                startActivity(intent);
        }
    }
}
