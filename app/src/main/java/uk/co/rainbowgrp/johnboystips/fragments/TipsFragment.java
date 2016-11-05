package uk.co.rainbowgrp.johnboystips.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.jsoup.nodes.Document;

import uk.co.rainbowgrp.johnboystips.R;

/**
 * Created by Martin on 8/30/2016.
 */
public class TipsFragment extends Fragment {

    private View rootView;
    private View loader;
    private View parent;
    private Document doc;

    private Handler mHandler;

    ImageView first;
    ImageView two;
    ImageView three;
    ImageView four;
    ImageView five;

    int loadPosition = 0;

    public TipsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_tips, container, false);
        parent = rootView.findViewById(R.id.parent);
        loader = rootView.findViewById(R.id.loader_view);

        first = (ImageView) rootView.findViewById(R.id.custom_loading_view_one);
        two = (ImageView) rootView.findViewById(R.id.custom_loading_view_two);
        three = (ImageView) rootView.findViewById(R.id.custom_loading_view_three);
        four = (ImageView) rootView.findViewById(R.id.custom_loading_view_four);
        five = (ImageView) rootView.findViewById(R.id.custom_loading_view_five);

        mHandler = new Handler();

        showLoader();

        return rootView;
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            displayLoadingPosition(loadPosition);

            loadPosition++;

            mHandler.postDelayed(mStatusChecker, 500);
        }
    };

    private void displayLoadingPosition(int loadPosition) {
        int emphasizedViewPos = loadPosition % 5;

        first.setImageResource(R.drawable.horse_gray);
        two.setImageResource(R.drawable.horse_gray);
        three.setImageResource(R.drawable.horse_gray);
        four.setImageResource(R.drawable.horse_gray);
        five.setImageResource(R.drawable.horse_gray);

        switch (emphasizedViewPos) {
            case 0:
                first.setImageResource(R.drawable.horse);
                break;

            case 1:
                two.setImageResource(R.drawable.horse);
                break;

            case 2:
                three.setImageResource(R.drawable.horse);
                break;

            case 3:
                four.setImageResource(R.drawable.horse);
                break;

            case 4:
                five.setImageResource(R.drawable.horse);
                break;
        }
    }

    public void showLoader() {

        mStatusChecker.run();
        parent.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

    }

    public void hideLoader() {

        loader.setVisibility(View.GONE);
        parent.setVisibility(View.VISIBLE);

    }

}