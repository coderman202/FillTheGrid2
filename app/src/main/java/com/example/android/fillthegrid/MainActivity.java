package com.example.android.fillthegrid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fillthegrid.adapters.LevelChooserSpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    public static final String CHOSEN_LEVEL = "Chosen Level";
    @BindView(R.id.start_game) TextView startGameView;
    @BindView(R.id.choose_level_spinner)
    Spinner levelChooser;
    @BindArray(R.array.levels_array)
    String[] levelsArray;

    List<String> levelList = new ArrayList<>();

    LevelChooserSpinnerAdapter adapter;

    int levelChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initLevelChooserSpinner();

        startGameView.setOnClickListener(this);

    }

    /**
     * Set up the drop down list for the user to choose the level.
     */
    private void initLevelChooserSpinner() {
        levelList = Arrays.asList(levelsArray);

        adapter = new LevelChooserSpinnerAdapter(this, levelList);
        levelChooser.setAdapter(adapter);

        levelChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                levelChosen = i + 5;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                levelChosen = 5;
            }
        });
    }

    @Override
    public void onClick(View view){
        int id = view.getId();

        switch(id){
            case R.id.start_game:
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra(CHOSEN_LEVEL, levelChosen);
                Toast.makeText(this, levelList.get(levelChosen - 5), Toast.LENGTH_LONG).show();
                startActivity(intent);
        }
    }
}
