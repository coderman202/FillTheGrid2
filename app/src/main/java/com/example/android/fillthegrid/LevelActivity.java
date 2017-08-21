package com.example.android.fillthegrid;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.fillthegrid.adapters.GameStageAdapter;
import com.example.android.fillthegrid.data.FillTheGridContract;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LevelActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter gameStagesSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @BindView(R.id.level_container)
    ViewPager gameStagesViewPager;

    @BindView(R.id.dot_layout)
    TabLayout dotLayout;

    @BindView(R.id.level_toolbar)
    Toolbar toolbar;

    public static final String CHOSEN_LEVEL = "Chosen Level";
    public static int levelChosen;

    @BindArray(R.array.grid_dimensions_array)
    int[] sizesArray;

    // Array of columns to be passed when a query to the Game Stage Table is made
    public static final String[] GAME_STAGE_ENTRY_COLUMNS = {
            FillTheGridContract.GameStageEntry.PK_GAME_STAGE_ID,
            FillTheGridContract.GameStageEntry.FK_DIFFICULTY_LEVEL_ID,
            FillTheGridContract.GameStageEntry.SIZE,
            FillTheGridContract.GameStageEntry.SCORE,
            FillTheGridContract.GameStageEntry.TARGET_SCORE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        ButterKnife.bind(this);

        levelChosen = getIntent().getIntExtra(CHOSEN_LEVEL, 0);

        setSupportActionBar(toolbar);

        dotLayout.setupWithViewPager(gameStagesViewPager);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        gameStagesSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        gameStagesViewPager.setAdapter(gameStagesSectionsPagerAdapter);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_level, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * The fragment for displaying the grid of 20 levels for each size.
     */
    public static class LevelFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static final int LOADER_ID = 1;

        // Array of columns to be passed when a query to the Game Stage Table is made
        public static final String[] GAME_STAGE_ENTRY_COLUMNS = {
                FillTheGridContract.GameStageEntry.PK_GAME_STAGE_ID,
                FillTheGridContract.GameStageEntry.FK_DIFFICULTY_LEVEL_ID,
                FillTheGridContract.GameStageEntry.SIZE,
                FillTheGridContract.GameStageEntry.SCORE,
                FillTheGridContract.GameStageEntry.TARGET_SCORE
        };


        @BindView(R.id.level_grid_layout)
        RecyclerView levelGridView;

        GridLayoutManager layoutManager;

        GameStageAdapter adapter;

        LoaderManager loaderManager;

        public LevelFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static LevelFragment newInstance(int sectionNumber) {
            LevelFragment fragment = new LevelFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_level, container, false);

            ButterKnife.bind(this, rootView);

            int pageNumber = getArguments().getInt(ARG_SECTION_NUMBER);

            loaderManager = getLoaderManager();
            layoutManager = new GridLayoutManager(this.getContext(), 4);
            levelGridView.setLayoutManager(layoutManager);
            adapter = new GameStageAdapter(getContext(), pageNumber);
            levelGridView.setAdapter(adapter);
            Log.e("dfgdfgdf", "oncreateview");
            loaderManager.restartLoader(LOADER_ID, null, this);

            return rootView;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            Log.e("dfgdfgdf", "actcreate");
            loaderManager.restartLoader(LOADER_ID, null, this);
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String sortOrder = FillTheGridContract.GameStageEntry.PK_GAME_STAGE_ID + " ASC";

            Uri gameStageUri = FillTheGridContract.GameStageEntry.CONTENT_URI;

            Log.e("Uri", gameStageUri.toString());

            int size = getArguments().getInt(ARG_SECTION_NUMBER) + 4;
            size = size * size;

            String whereClause = FillTheGridContract.GameStageEntry.FK_DIFFICULTY_LEVEL_ID + " = " +
                    (levelChosen - 4) + " AND " + FillTheGridContract.GameStageEntry.SIZE + " = " + size + ";";

            Log.e("level", "" + (levelChosen - 4));
            Log.e("where", whereClause);

            return new CursorLoader(getContext(), gameStageUri, GAME_STAGE_ENTRY_COLUMNS, whereClause, null, sortOrder);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            adapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            adapter.swapCursor(null);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a LevelFragment (defined as a static inner class below).
            return LevelFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 1 page for each grid size.
            return sizesArray.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return null;
        }
    }
}
