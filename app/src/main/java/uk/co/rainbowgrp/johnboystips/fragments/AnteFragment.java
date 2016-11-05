package uk.co.rainbowgrp.johnboystips.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import uk.co.rainbowgrp.johnboystips.R;

/**
 * Created by Martin on 8/30/2016.
 */
public class AnteFragment extends Fragment {

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


    public AnteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_ante, container, false);
        parent = rootView.findViewById(R.id.parent);
        loader = rootView.findViewById(R.id.loader_view);

        first = (ImageView) rootView.findViewById(R.id.custom_loading_view_one);
        two = (ImageView) rootView.findViewById(R.id.custom_loading_view_two);
        three = (ImageView) rootView.findViewById(R.id.custom_loading_view_three);
        four = (ImageView) rootView.findViewById(R.id.custom_loading_view_four);
        five = (ImageView) rootView.findViewById(R.id.custom_loading_view_five);

        mHandler = new Handler();

        showLoader();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        new getLatestNews().execute("http://json.johnboystips.co.uk/blog/getLastestNews/index.php");
                        new getAnteData().execute("http://johnboystips.co.uk/antepost/partial/index.php");

                    }
                }, 500);
            }
        });

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

    public void setData() {

        for (int i = 0; i < doc.select("div.kode-time-zoon > time").size(); i++) {

            String date = doc.select("div.kode-time-zoon > time").get(i).text();
            String day = transformString(date);
            String month = doc.select("div.kode-time-zoon > time > span").get(i).text();
            String header = doc.select("div.kode-time-zoon > h5 > a").get(i).text();
            String author = doc.select("div.kode-blog-info >div > ul > li > a[href='#']").get(i).text();


            String text = doc.select("div.kode-blog-info > div").get(i + (i + 1)).text();
            String img = doc.select("div.kode-blog-info > div > img").get(i).absUrl("src");

            LinearLayout parentView = (LinearLayout) rootView.findViewById(R.id.parent);
            parentView.addView(addView(day, month, header, author, text, img));

        }

    }

    public String transformString(String string) {

        return "" + string.charAt(0) + string.charAt(1);

    }

    public View addView(String date, String month, String header, String author, String content, String img) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //params.setMargins(dpToPx(18), 0, dpToPx(18), 0);

        LinearLayout parent = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.fragment_ante_inflater, null);
        parent.setLayoutParams(params);

        // Get header contents

        LinearLayout child = (LinearLayout) parent.getChildAt(0);

        TextView textViewHeader = (TextView) child.getChildAt(1);

        child = (LinearLayout) child.getChildAt(0);

        child = (LinearLayout) child.getChildAt(0);

        TextView textViewDate = (TextView) child.getChildAt(0);
        TextView textViewMonth = (TextView) child.getChildAt(1);

        textViewDate.setText(date);
        textViewMonth.setText(month);
        textViewHeader.setText(header);

        // Get content contents :D

        child = (LinearLayout) parent.getChildAt(1);

        ImageView imageView = (ImageView) child.getChildAt(2);
        TextView textViewContent = (TextView) child.getChildAt(3);

        child = (LinearLayout) child.getChildAt(0);
        child = (LinearLayout) child.getChildAt(0);

        TextView textViewAuthor = (TextView) child.getChildAt(1);

        if (!"".equals(img)) {

            Picasso.with(getActivity()).load(img).into(imageView);
            imageView.setVisibility(View.VISIBLE);

        }

        textViewAuthor.setText(author);
        textViewContent.setText(content);

        return parent;

    }

    public int dpToPx(int value) {

        Resources r = getActivity().getResources();

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());

    }

    public void setLatestNews(String result) throws JSONException {

        JSONObject myObject = new JSONObject(result);
        String scrollingText = myObject.getString("info_msg");

        TextView latestNews = (TextView) rootView.findViewById(R.id.scrollingText);

        latestNews.setText(scrollingText);

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

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class getLatestNews extends AsyncTask<String, String, String> {

        public getLatestNews() {


        }

        @Override
        protected String doInBackground(final String... params) {

            boolean hasConnection = isNetworkAvailable();

            if (hasConnection) {

                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(params[0]);
                // replace with your url

                try {

                    HttpResponse response = client.execute(request);
                    String result = EntityUtils.toString(response.getEntity());

                    Log.d("Http Post Response:", result);
                    return result;

                } catch (ClientProtocolException e) {


                } catch (IOException e) {


                }

            } else {

                return null;

            }

            return null;

        }

        protected void onPostExecute(String result) {

            if (result == null) {

                //NoConnection();

                //Functions.showToast("No connection");

            } else {

                try {

                    setLatestNews(result);

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }

        }

    }

    private class getAnteData extends AsyncTask<String, String, String> {

        public getAnteData() {


        }

        @Override
        protected String doInBackground(final String... params) {

            boolean hasConnection = isNetworkAvailable();

            if (hasConnection) {

                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(params[0]);
                // replace with your url

                try {

                    HttpResponse response = client.execute(request);
                    String result = EntityUtils.toString(response.getEntity());

                    Log.d("Http Post Response:", result);
                    return result;

                } catch (ClientProtocolException e) {


                } catch (IOException e) {


                }

            } else {

                return null;

            }

            return null;

        }

        protected void onPostExecute(String result) {

            if (result == null) {

                //NoConnection();

                //Functions.showToast("No connection");

            } else {

                doc = Jsoup.parse(result);
                setData();
                hideLoader();

            }

        }

    }

}
