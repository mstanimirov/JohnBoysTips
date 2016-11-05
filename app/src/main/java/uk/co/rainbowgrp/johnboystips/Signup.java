package uk.co.rainbowgrp.johnboystips;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.rainbowgrp.johnboystips.functions.CustomDialog;
import uk.co.rainbowgrp.johnboystips.functions.Functions;

import static uk.co.rainbowgrp.johnboystips.functions.Regex.isEmail;

/**
 * Created by Martin on 9/8/2016.
 */
public class Signup extends AppCompatActivity {

    private final List<NameValuePair> postdata = new ArrayList<NameValuePair>();
    private uk.co.rainbowgrp.johnboystips.functions.Functions Functions = new Functions(this);
    private EditText email;
    private EditText pass;
    private EditText re_pass;
    private TextView email_error;
    private TextView pass_error;
    private TextView re_pass_error;
    View.OnFocusChangeListener focusChanged = new View.OnFocusChangeListener() {

        public void onFocusChange(View v, boolean hasFocus) {

            if (v instanceof EditText) {

                int edittextlenght = ((EditText) v).getText().length();

                if (hasFocus) {

                    if (edittextlenght == 0) {

                        ((EditText) v).setHint(null);
                        //((EditText) v).setBackgroundResource(R.drawable.input_required);

                    }

                    ((EditText) v).addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable s) {

                            String c = s.toString();

                            int Hashcode = s.hashCode();

                            if (email.getText().hashCode() == Hashcode) {

                                if (c.length() > 0) {

                                    email_error.setText("");

                                }

                            }

                            if (pass.getText().hashCode() == Hashcode) {

                                if (pass.getText().length() > 0) {

                                    pass_error.setText("");

                                }

                            }

                            if (re_pass.getText().hashCode() == Hashcode) {

                                if (re_pass.getText().length() > 0) {

                                    re_pass_error.setText("");

                                }

                            }

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                    });

                } else {

                    /*switch (v.getId()) {
                         case R.id.email:

                            if (!isEmail(email.getText().toString()) && email.getText().toString().length() > 0) {

                                email_error.setText(getResources().getString(R.string.email_error));

                            }

                            email.setHint(getResources().getString(R.string.email_hint));
                            break;

                    }*/

                }

            }
        }

    };
    private TextView terms_error;
    private Button log_in;
    private Button sign_up;
    private CheckBox checkBox;
    private CustomDialog.Builder loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        getWindow().setBackgroundDrawableResource(R.color.colorPrimary);

        initialize();

        sign_up.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {

                boolean isValid = validateUserData();

                if(isValid){

                    postdata.set(0, new BasicNameValuePair("email", email.getText().toString()));
                    postdata.set(1, new BasicNameValuePair("password", pass.getText().toString()));

                    loader.show();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    new signUp().execute("http://json.johnboystips.co.uk/auth/register");

                                }
                            }, 500);
                        }
                    });

                    //Intent i = new Intent(Login.this, Signup.class);
                    //startActivity(i);
                    //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    //finish();

                }


            }

        });

    }

    private void initialize(){

        sign_up = (Button) findViewById(R.id.sign_up);

        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);
        re_pass = (EditText) findViewById(R.id.re_pass);

        email_error = (TextView) findViewById(R.id.email_error);
        pass_error = (TextView) findViewById(R.id.pass_error);
        re_pass_error = (TextView) findViewById(R.id.re_pass_error);

        checkBox = (CheckBox) findViewById(R.id.terms);
        terms_error = (TextView) findViewById(R.id.terms_error);

        email.setOnFocusChangeListener(focusChanged);
        pass.setOnFocusChangeListener(focusChanged);
        re_pass.setOnFocusChangeListener(focusChanged);

        loader = new CustomDialog.Builder(Signup.this);
        loader.setTitle("Sending...");
        loader.addLoader("");

        postdata.add(0, new BasicNameValuePair("email", ""));
        postdata.add(1, new BasicNameValuePair("password", ""));

    }

    private boolean validateUserData(){

        if(email.getText().length() == 0){

            email_error.setText("Please enter your e-mail!");
            return false;

        } else if(!isEmail(email.getText().toString())){

            email_error.setText("Please enter valid e-mail!");
            return false;

        } else if(pass.getText().length() == 0) {

            pass_error.setText("Please enter your password!");
            return false;

        } else if(pass.getText().length() < 6){

            pass_error.setText("Please enter at least 6 symbols!!");
            return false;

        } else if(re_pass.getText().length() == 0) {

            re_pass_error.setText("Please retype your password!");
            return false;

        } else if (!pass.getText().toString().equals(re_pass.getText().toString())) {

            re_pass_error.setText("Passwords do not match!");
            return false;

        } else if (!checkBox.isChecked()) {

            terms_error.setText("You must agree our terms & conditions!");
            return false;

        }

        return true;

    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        final CustomDialog.Builder dialog = new CustomDialog.Builder(this);
        dialog.setTitle("Terms & Conditions");
        dialog.addWebView("http://johnboystips.co.uk/terms/partial");
        dialog.addButton(this, "I Agree", new View.OnClickListener() {

            public void onClick(View v) {

                dialog.dismiss();

            }

        });

        switch (view.getId()) {
            case R.id.terms:

                if (checked) {

                    dialog.show();
                    ((CheckBox) view).setTextColor(getResources().getColor(R.color.white));
                    terms_error.setText("");

                } else {

                    ((CheckBox) view).setTextColor(getResources().getColor(R.color.colorHint));
                    terms_error.setText("You must agree our terms & conditions!");

                }

                break;
        }

    }

    public void showDialog(String result) throws JSONException {

        final CustomDialog.Builder response = new CustomDialog.Builder(Signup.this);

        String title;
        String message;
        boolean hide = false;

        if ("403".equals(result)) {

            title = "Error!";
            message = "This Email is already being used!";

        } else {

            JSONObject obj = new JSONObject(result);
            int success = obj.getInt("success");

            if (success == 1) {

                title = "Success!";
                message = "Success!";

            } else {

                title = "Error!";
                message = "Something went wrong!";

            }

            hide = true;

        }

        response.setTitle(title);
        response.setMessage(message);
        response.addButton(Signup.this, "Ok", new View.OnClickListener() {

            public void onClick(View v) {

                response.dismiss();

            }
        });

        response.show();

        loader.dismiss();

    }

    public void showToast(String str) {

        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(Signup.this, Login.class);
        startActivity(i);
        //overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
        finish();

    }

    private class signUp extends AsyncTask<String, String, String> {

        public signUp() {


        }

        @Override
        protected String doInBackground(final String... params) {

            boolean hasConnection = Functions.isNetworkAvailable();

            if (hasConnection) {

                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost(params[0]);
                // replace with your url
                request.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);
                request.setHeader("Accept", "application/json");

                try {

                    request.setEntity(new UrlEncodedFormEntity(postdata));
                    HttpResponse response = client.execute(request);

                    int statusCode = response.getStatusLine().getStatusCode();

                    String result;

                    if (statusCode == 403) {

                        result = "403";

                    } else {

                        result = EntityUtils.toString(response.getEntity());

                    }

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

                    showDialog(result);

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }

        }

    }

}
