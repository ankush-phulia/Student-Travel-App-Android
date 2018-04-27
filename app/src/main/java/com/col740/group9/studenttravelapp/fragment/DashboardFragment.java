package com.col740.group9.studenttravelapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.col740.group9.studenttravelapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnDashboardFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DashboardFragment extends Fragment
        implements DashboardJourneyFragment.OnDashboardJourneyFragmentInteractionListener,
        DashboardTripFragment.OnDashboardTripFragmentInteractionListener{

    private OnDashboardFragmentInteractionListener mListener;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private View DashboardFragmentView;
    private FragmentActivity myContext;
    private Bundle bundle;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        bundle = getArguments();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DashboardFragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        myContext = getActivity();

//        FloatingActionButton fab_add = (FloatingActionButton) DashboardFragmentView.findViewById(R.id.fab_add_journey_or_trip);
//        fab_add.setOnClickListener(new View.OnClickListener() { // click listener for start button
//            @Override
//            public void onClick(View view) {
//                // TODO start add new journey or trip class
//            }
//        });

        return DashboardFragmentView;
    }

    @Override
    public void onResume(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) DashboardFragmentView.findViewById(R.id.dashboard_viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        super.onResume();

    }

    @Override
    public void onAttach(Context context) {
        myContext=(FragmentActivity) context;
        super.onAttach(context);
        if (context instanceof OnDashboardFragmentInteractionListener) {
            mListener = (OnDashboardFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDashboardFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDashboardJourneyFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDashboardTripFragmentInteraction(Uri uri) {

    }

    public interface OnDashboardFragmentInteractionListener {
        void onDashboardFragmentInteraction(Uri uri);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = null;
            switch (position) {
                case 0: fragment = new DashboardJourneyFragment();
                    break;
                case 1: fragment = new DashboardTripFragment();
                    break;
            }

            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Journey";
                case 1:
                    return "Trip";
            }
            return null;
        }
    }

}
