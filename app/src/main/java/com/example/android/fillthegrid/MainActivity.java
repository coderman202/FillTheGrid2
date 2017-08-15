package com.example.android.fillthegrid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fillthegrid.utils.GameSetupUtils;

import org.malcdevelop.cyclicview.CyclicFragmentAdapter;
import org.malcdevelop.cyclicview.CyclicView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{



    @BindView(R.id.start_game) TextView startGameView;

    @BindArray(R.array.levels_array)
    String[] levelsArray;

    public static List<String> levelList = new ArrayList<>();


    public static final String CHOSEN_LEVEL = "Chosen Level";
    int levelChosen;

    @BindView(R.id.arrow_left)
    ImageView arrowLeft;
    @BindView(R.id.arrow_right)
    ImageView arrowRight;
    @BindView(R.id.level_selector)
    CyclicView levelSelectorView;
    CyclicPageAdapter cyclicPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initCyclicViewPager();


        startGameView.setOnClickListener(this);
    }

    private void initCyclicViewPager() {
        levelList = Arrays.asList(levelsArray);

        cyclicPageAdapter = new CyclicPageAdapter(this, getSupportFragmentManager()) {
            @Override
            protected Fragment createFragment(int i) {
                return LevelSelectorFragment.newInstance(i);
            }

            @Override
            public int getItemsCount() {
                return levelList.size();
            }
        };

        levelSelectorView.setAdapter(cyclicPageAdapter);
    }

    @Override
    public void onClick(View view){
        int id = view.getId();

        switch(id){
            case R.id.start_game:
                Intent intent = new Intent(this, LevelActivity.class);
                levelChosen = levelSelectorView.getCurrentPosition() + 5;
                intent.putExtra(CHOSEN_LEVEL, levelChosen);
                Toast.makeText(this, levelList.get(levelChosen - 5), Toast.LENGTH_LONG).show();
                startActivity(intent);
                break;
            case R.id.arrow_left:
                levelSelectorView.setCurrentPosition(levelSelectorView.getCurrentPosition() + levelList.size());
                break;
            case R.id.arrow_right:
                levelSelectorView.setCurrentPosition(levelSelectorView.getCurrentPosition() + 1);
                break;
        }
    }

    /**
     * A fragment containing the chosen level and it's icon.
     */
    public static class LevelSelectorFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        @BindView(R.id.level_text)
        TextView levelView;
        @BindView(R.id.level_icon)
        ImageView iconView;

        public LevelSelectorFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static LevelSelectorFragment newInstance(int sectionNumber) {
            LevelSelectorFragment fragment = new LevelSelectorFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                 final Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.level_selector_fragment, container, false);

            ButterKnife.bind(this, rootView);

            int position = getArguments().getInt(ARG_SECTION_NUMBER);

            levelView.setText(levelList.get(position));
            iconView.setImageResource(GameSetupUtils.getLevelDrawableResID(position));

            return rootView;
        }
    }

    /**
     * A {@link org.malcdevelop.cyclicview.CyclicFragmentAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private abstract class CyclicPageAdapter extends CyclicFragmentAdapter {

        CyclicPageAdapter(Context context, FragmentManager fm) {
            super(context, fm);
        }
    }
}
