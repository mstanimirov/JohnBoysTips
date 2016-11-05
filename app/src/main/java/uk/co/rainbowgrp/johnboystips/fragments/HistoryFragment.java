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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import uk.co.rainbowgrp.johnboystips.R;
import uk.co.rainbowgrp.johnboystips.functions.CustomDialog;

/**
 * Created by Martin on 8/30/2016.
 */
public class HistoryFragment extends Fragment {

    private View rootView;
    private View loader;
    private LinearLayout parent;
    private TableRow table_row;
    private Document doc;

    private Handler mHandler;

    ImageView first;
    ImageView two;
    ImageView three;
    ImageView four;
    ImageView five;

    int loadPosition = 0;

    Bet[] bets;

    ArrayList<String> monthsArray = new ArrayList<>();
    ArrayList<String> daysArray = new ArrayList<>();

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_history, container, false);
        parent = (LinearLayout) rootView.findViewById(R.id.parent);
        table_row = (TableRow) rootView.findViewById(R.id.bet_table);
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

                        new getData().execute("http://json.johnboystips.co.uk/bets/achievedBets");
                        new getLatestNews().execute("http://json.johnboystips.co.uk/blog/getLastestNews/index.php");

                    }
                }, 500);
            }
        });

        return rootView;
    }

    private class getData extends AsyncTask<String, String, String> {

        public getData() {


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
                    setData(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

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

    public void setData(String result) throws JSONException {

        JSONObject myObject = new JSONObject(result);

        JSONArray all_bets = myObject.getJSONArray("result");

        Gson gson = new Gson();
        bets = gson.fromJson(String.valueOf(all_bets), Bet[].class);
        System.out.println(gson.toJson(bets[53].bet));

        populateArray(bets[bets.length - 1].betDate, bets[0].betDate);

        hideLoader();

    }

    public void populateArray(String startDate, String endDate){

        monthsArray.clear();

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");

        Calendar startCalendar = new GregorianCalendar();
        Calendar endCalendar = new GregorianCalendar();

        try {
            startCalendar.setTime(sdf.parse(startDate));
            endCalendar.setTime(sdf.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

        Log.d("-----> diffMonth: ", ""+diffMonth);

        for(int i = 0; i <= diffMonth; i++){

            monthsArray.add(i, getMonthName(endCalendar.get(Calendar.MONTH)) + " " + endCalendar.get(Calendar.YEAR) );

            endCalendar.add(Calendar.MONTH, -1);

        }

        Spinner(R.id.choose_month, R.id.month, "Choose Month", monthsArray);
        Spinner(R.id.choose_day, R.id.date, "Choose Day", daysArray);

    }

    public void populateDayArray(String date){

        daysArray.clear();

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");

        Calendar startCalendar = new GregorianCalendar();
        try {
            startCalendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++){

            daysArray.add(i, "" + startCalendar.get(Calendar.YEAR) + ((startCalendar.get(Calendar.MONTH) < 9) ? ( "-0" + (startCalendar.get(Calendar.MONTH) + 1) ) : ( "-" + (startCalendar.get(Calendar.MONTH) + 1) )) + ((i < 9) ? ( "-0" + (i + 1) ) : ( "-" + (i + 1) )) );

        }

        Spinner(R.id.choose_day, R.id.date, "Choose Day", daysArray);

    }

    private void Spinner(int click_field_id, int field_id, String setTitle, ArrayList<String> array) {

        final TextView field = (TextView) rootView.findViewById(field_id);
        final View click_field = rootView.findViewById(click_field_id);
        final TextView text_view = (TextView) rootView.findViewById(R.id.date);
        final TextView text_view2 = (TextView) rootView.findViewById(R.id.month);

        final CustomDialog.Builder dialog = new CustomDialog.Builder(getActivity());
        dialog.setTitle(setTitle);
        dialog.addListView();

        final ListView listView = dialog.getListView();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, array);

        listView.setAdapter(adapter);

        click_field.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                switch(v.getId()){

                    case R.id.choose_month:

                        dialog.show();

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                String item = (String) parent.getItemAtPosition(position);

                                field.setText(item);

                                text_view.setText("Choose Day");

                                populateDayArray(getDate(item));

                                dialog.dismiss();

                            }
                        });

                        break;

                    case R.id.choose_day:

                        if(text_view2.getText().toString().equals("Choose Month")){

                            showToast("Select month first");

                        }else{

                            dialog.show();

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    String item = (String) parent.getItemAtPosition(position);

                                    field.setText(item);

                                    showTips(item);

                                    dialog.dismiss();

                                }
                            });

                        }

                        break;

                }

            }

        });

    }

    public void showTips(String date){

        table_row.removeAllViews();

        for(int i = 0; i < bets.length - 1; i++){

            String check_date = "";

            for(int j = 0; j < 10; j++){

                check_date = check_date + bets[i].betDate.charAt(j);

            }

            if(date.equals(check_date)){

                table_row.addView(addTipView(bets[i].bet));

            }

        }

    }

    public int dpToPx(int value) {

        Resources r = getActivity().getResources();

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());

    }

    public View addTipView(ArrayList<BetDetails> bet) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        params.setMargins(dpToPx(15), dpToPx(15), dpToPx(15), dpToPx(15));

        RelativeLayout parent = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.fragment_history_inflater, null);
        parent.setLayoutParams(params);

        LinearLayout bet_double = (LinearLayout) parent.findViewById(R.id.bet_double);
        LinearLayout bet_treble = (LinearLayout) parent.findViewById(R.id.bet_treble);

        TextView tip_type = (TextView) parent.findViewById(R.id.tip_type);
        tip_type.setText(bet.get(0).betType);

        TextView place = (TextView) parent.findViewById(R.id.place);
        TextView place2 = (TextView) parent.findViewById(R.id.place2);
        TextView place3 = (TextView) parent.findViewById(R.id.place3);

        TextView time = (TextView) parent.findViewById(R.id.time);
        TextView time2 = (TextView) parent.findViewById(R.id.time2);
        TextView time3 = (TextView) parent.findViewById(R.id.time3);

        TextView horse_name = (TextView) parent.findViewById(R.id.horse_name);
        TextView horse_name2 = (TextView) parent.findViewById(R.id.horse_name2);
        TextView horse_name3 = (TextView) parent.findViewById(R.id.horse_name3);

        TextView bet_result = (TextView) parent.findViewById(R.id.bet_result);
        TextView bet_result2 = (TextView) parent.findViewById(R.id.bet_result2);
        TextView bet_result3 = (TextView) parent.findViewById(R.id.bet_result3);

        TextView odd = (TextView) parent.findViewById(R.id.odd);
        TextView odd2 = (TextView) parent.findViewById(R.id.odd2);
        TextView odd3 = (TextView) parent.findViewById(R.id.odd3);

        ImageView win_stamp = (ImageView) parent.findViewById(R.id.win_stamp);

        ImageView slik = (ImageView) parent.findViewById(R.id.img);
        ImageView slik2 = (ImageView) parent.findViewById(R.id.img2);
        ImageView slik3 = (ImageView) parent.findViewById(R.id.img3);

        RelativeLayout odd_circle = (RelativeLayout) parent.findViewById(R.id.odd_circle);
        RelativeLayout odd_circle2 = (RelativeLayout) parent.findViewById(R.id.odd_circle2);
        RelativeLayout odd_circle3 = (RelativeLayout) parent.findViewById(R.id.odd_circle3);

        switch(bet.get(0).betResult){

            case "Non Runner": case "Lose":

                win_stamp.setVisibility(View.GONE);

                break;

            case "Placed": case "Won":

                win_stamp.setVisibility(View.VISIBLE);

                break;

        }

        switch(bet.get(0).betType){

            case "Single":

                bet_double.setVisibility(View.INVISIBLE);
                bet_treble.setVisibility(View.INVISIBLE);

                place2.setText("Place: " + bet.get(0).raceCourse);
                time2.setText("Time: " + getTime(bet.get(0).tipDate));
                horse_name2.setText(" " + bet.get(0).horse);
                bet_result2.setText(" " + bet.get(0).tipType);

                if(bet.get(0).odd.equals("0 / 0")){

                    odd_circle2.setVisibility(View.INVISIBLE);

                }else{

                    odd2.setText(bet.get(0).odd);

                }

                Picasso.with(getActivity()).load("http://johnboystips.co.uk" + bet.get(0).imgUrl).into(slik2);

                break;

            case "Double":

                bet_treble.setVisibility(View.INVISIBLE);

                place.setText("Place: " + bet.get(0).raceCourse);
                time.setText("Time: " + getTime(bet.get(0).tipDate));
                horse_name.setText(" " + bet.get(0).horse);
                bet_result.setText(" " + bet.get(0).tipType);
                if(bet.get(0).odd.equals("0 / 0")){

                    odd_circle.setVisibility(View.INVISIBLE);

                }else{

                    odd.setText(bet.get(0).odd);

                }

                place2.setText("Place: " + bet.get(1).raceCourse);
                time2.setText("Time: " + getTime(bet.get(1).tipDate));
                horse_name2.setText(" " + bet.get(1).horse);
                bet_result2.setText(" " + bet.get(1).tipType);
                if(bet.get(1).odd.equals("0 / 0")){

                    odd_circle2.setVisibility(View.INVISIBLE);

                }else{

                    odd2.setText(bet.get(1).odd);

                }

                Picasso.with(getActivity()).load("http://johnboystips.co.uk" + bet.get(0).imgUrl).into(slik);
                Picasso.with(getActivity()).load("http://johnboystips.co.uk" + bet.get(1).imgUrl).into(slik2);

                break;

            case "Treble":

                place.setText("Place: " + bet.get(0).raceCourse);
                time.setText("Time: " + getTime(bet.get(0).tipDate));
                horse_name.setText(" " + bet.get(0).horse);
                bet_result.setText(" " + bet.get(0).tipType);
                if(bet.get(0).odd.equals("0 / 0")){

                    odd_circle.setVisibility(View.INVISIBLE);

                }else{

                    odd.setText(bet.get(0).odd);

                }

                place2.setText("Place: " + bet.get(1).raceCourse);
                time2.setText("Time: " + getTime(bet.get(1).tipDate));
                horse_name2.setText(" " + bet.get(1).horse);
                bet_result2.setText(" " + bet.get(1).tipType);
                if(bet.get(1).odd.equals("0 / 0")){

                    odd_circle2.setVisibility(View.INVISIBLE);

                }else{

                    odd2.setText(bet.get(1).odd);

                }

                place3.setText("Place: " + bet.get(2).raceCourse);
                time3.setText("Time: " + getTime(bet.get(2).tipDate));
                horse_name3.setText(" " + bet.get(2).horse);
                bet_result3.setText(" " + bet.get(2).tipType);
                if(bet.get(2).odd.equals("0 / 0")){

                    odd_circle3.setVisibility(View.INVISIBLE);

                }else{

                    odd3.setText(bet.get(2).odd);

                }

                Picasso.with(getActivity()).load("http://johnboystips.co.uk" + bet.get(0).imgUrl).into(slik);
                Picasso.with(getActivity()).load("http://johnboystips.co.uk" + bet.get(1).imgUrl).into(slik2);
                Picasso.with(getActivity()).load("http://johnboystips.co.uk" + bet.get(2).imgUrl).into(slik3);

                break;

        }

        return parent;

    }

    public String getTime(String date){

        return "" + date.charAt(date.length() - 5) + date.charAt(date.length() - 4) + date.charAt(date.length() - 3) + date.charAt(date.length() - 2) + date.charAt(date.length() - 1);

    }

    public String getMonthName(int number){

        switch(number){

            case 0: return "January";
            case 1: return "February";
            case 2: return "March";
            case 3: return "April";
            case 4: return "May";
            case 5: return "June";
            case 6: return "July";
            case 7: return "August";
            case 8: return "September";
            case 9: return "October";
            case 10: return "November";
            case 11: return "December";
            default: return "";

        }

    }

    public String getDate(String date){

        String month = "" + date.charAt(0) + date.charAt(1) + date.charAt(2);

        switch(month){

            case "Jan": month = "01"; break;
            case "Feb": month = "02"; break;
            case "Mar": month = "03"; break;
            case "Apr": month = "04"; break;
            case "May": month = "05"; break;
            case "Jun": month = "06"; break;
            case "Jul": month = "07"; break;
            case "Aug": month = "08"; break;
            case "Sep": month = "09"; break;
            case "Oct": month = "10"; break;
            case "Nov": month = "11"; break;
            case "Dec": month = "12"; break;

        }

        String year = "" + date.charAt(date.length() - 4) + date.charAt(date.length() - 3) + date.charAt(date.length() - 2) + date.charAt(date.length() - 1);

        return year + "-" + month + "-01";

    }

    public void setLatestNews(String result) throws JSONException {

        JSONObject myObject = new JSONObject(result);
        String scrollingText = myObject.getString("info_msg");

        TextView latestNews = (TextView) rootView.findViewById(R.id.scrollingText);

        latestNews.setText(scrollingText);

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

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showToast(String str) {

        Toast.makeText(getActivity().getApplicationContext(), str, Toast.LENGTH_SHORT).show();

    }

    class Bet{

        ArrayList<BetDetails> bet;

        String betDate;
        String betResult;
        String betType;

    }

    class BetDetails{

        String betDate;
        String betResult;
        String betType;
        String horse;
        String imgUrl;
        String jockey;
        String odd;
        String raceCourse;
        String tipCombinedId;
        String tipDate;
        String tipResult;
        String tipType;

    }

}
