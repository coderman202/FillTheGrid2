package com.example.android.fillthegrid;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.fillthegrid.utils.GlobalUtils;

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
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @BindView(R.id.dot_layout)
    TabLayout dotLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.level_toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.level_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.level_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
    public static class LevelFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";


        @BindView(R.id.level_grid_layout)
        GridLayout levelGridLayout;

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

            levelGridLayout.setColumnCount(4);

            Integer[] colorArray = GlobalUtils.getColorArray(getContext());

            // Styles for the grid items
            ContextThemeWrapper boxLayoutStyle = new ContextThemeWrapper(getContext(), R.style.BoxFrameStyle);
            ContextThemeWrapper boxTextStyle = new ContextThemeWrapper(getContext(), R.style.BoxTextStyle);
            ContextThemeWrapper boxMarginStyle = new ContextThemeWrapper(getContext(), R.style.MarginFrameStyle);

            for (int i = 0; i < 20; i++) {
                LinearLayout boxLayout = new LinearLayout(boxLayoutStyle);
                LinearLayout boxMargin = new LinearLayout(boxMarginStyle);
                TextView tv = new TextView(boxTextStyle);
                GradientDrawable border = (GradientDrawable) getContext().getDrawable(R.drawable.level_item_border);
                border.setStroke(5, colorArray[pageNumber - 1]);
                boxLayout.setBackground(border);
                String str = i + "";
                tv.setBackgroundResource(R.drawable.stage_complete);
                boxLayout.addView(tv);
                boxMargin.addView(boxLayout);
                levelGridLayout.addView(boxMargin);
            }

            return rootView;
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
            // Show 6 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return null;
        }
    }
}
