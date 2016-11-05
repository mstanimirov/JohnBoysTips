package uk.co.rainbowgrp.johnboystips;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

/**
 * Created by Martin on 9/8/2016.
 */
public class Login extends AppCompatActivity {

    private final List<NameValuePair> postdata = new ArrayList<NameValuePair>(1);
    private final List<NameValuePair> logindata = new ArrayList<NameValuePair>(2);
    private Functions Functions = new Functions(this);
    private CustomDialog.Builder dialog;
    private CustomDialog.Builder loader;
    private CustomDialog.Builder logInLoader;
    private EditText email;
    private EditText pass;
    private TextView email_error;
    private TextView pass_error;
    private TextView forgot_pass;
    private Button log_in;
    private Button sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        getWindow().setBackgroundDrawableResource(R.color.colorPrimary);

        initialize();

        log_in.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {

                //boolean isValid = validateUserData();

                //if(isValid){

                logindata.set(0, new BasicNameValuePair("email", email.getText().toString()));
                logindata.set(1, new BasicNameValuePair("password", pass.getText().toString()));

                logInLoader.show();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                new logIn().execute("http://json.johnboystips.co.uk/auth/login");

                            }
                        }, 500);
                    }
                });
                //}


            }

        });

        sign_up.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login.this, Signup.class);
                startActivity(i);
                //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                finish();


            }

        });

        forgot_pass.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {

                dialog.show();

            }

        });

    }

    private void initialize() {

        log_in = (Button) findViewById(R.id.log_in);
        sign_up = (Button) findViewById(R.id.sign_up);

        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);

        email.setText("m.stanimirov81@gmail.com");
        pass.setText("qqqqqqq");

        email_error = (TextView) findViewById(R.id.email_error);
        pass_error = (TextView) findViewById(R.id.pass_error);

        forgot_pass = (TextView) findViewById(R.id.forgot_pass);

        dialog = new CustomDialog.Builder(Login.this);
        loader = new CustomDialog.Builder(Login.this);
        logInLoader = new CustomDialog.Builder(Login.this);

        postdata.add(0, new BasicNameValuePair("email", ""));

        logindata.add(0, new BasicNameValuePair("email", ""));
        logindata.add(1, new BasicNameValuePair("password", ""));

        dialog.setTitle("Reset Password");
        dialog.setMessage("We can help you reset your password using your email address linked to your account.");
        dialog.addEditText();
        dialog.addButtonGroup(Login.this, "Reset Password", new View.OnClickListener() {

            public void onClick(View v) {

                        /*if(dialog.getEditText().getText().length() == 0){

                            dialog.getEditTextError().setText("Please enter your e-mail!");

                        } else if(!isEmail(dialog.getEditText().getText().toString())){

                            dialog.getEditTextError().setText("Please enter valid e-mail!");

                        }else{*/

                postdata.set(0, new BasicNameValuePair("email", dialog.getEditText().getText().toString()));

                loader.show();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                new resetPassword().execute("http://json.johnboystips.co.uk/auth/forgotpassword");

                            }
                        }, 500);
                    }
                });

                //}

            }
        });
        dialog.addButtonGroup(Login.this, "Cancel", new View.OnClickListener() {

            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        loader.setTitle("Sending...");
        loader.addLoader("");

        logInLoader.setTitle("Logging In...");
        logInLoader.addLoader("");

    }

    public void showDialog(String result) throws JSONException {

        final CustomDialog.Builder response = new CustomDialog.Builder(Login.this);

        String title;
        String message;
        boolean hide = false;

        if ("401".equals(result)) {

            title = "Email Error!";
            message = "Invalid email address!";

        } else if ("404".equals(result)) {

            title = "Wrong Email!";
            message = "Account with that email address not found!";

        } else if ("405".equals(result)) {

            title = "Wrong Email!";
            message = "Account with that email address not found!";

        } else {

            JSONObject obj = new JSONObject(result);
            int success = obj.getInt("success");

            if (success == 1) {

                title = "Success!";
                message = "Your password was changed please check your e-mail!";

            } else {

                title = "Error!";
                message = "Something went wrong!";

            }

            hide = true;

        }

        response.setTitle(title);
        response.setMessage(message);
        response.addButton(Login.this, "Ok", new View.OnClickListener() {

            public void onClick(View v) {

                response.dismiss();

            }
        });

        response.show();

        loader.dismiss();

        if (hide) {

            dialog.dismiss();

        }

    }

    public void changeActivity(String result) throws JSONException {

        final CustomDialog.Builder response = new CustomDialog.Builder(Login.this);

        String title;
        String message;

        if ("401".equals(result)) {

            title = "Please Login!";
            message = "Wrong Email or Password!";

            response.setTitle(title);
            response.setMessage(message);
            response.addButton(Login.this, "Ok", new View.OnClickListener() {

                public void onClick(View v) {

                    response.dismiss();

                }
            });

            response.show();

        } else {

            JSONObject obj = new JSONObject(result);
            int success = obj.getInt("success");

            if (success == 1) {

                Intent i = new Intent(Login.this, MainActivity.class);
                i.putExtra("email", email.getText().toString());
                startActivity(i);
                //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                finish();

            } else {

                title = "Error!";
                message = "Something went wrong!";

                response.setTitle(title);
                response.setMessage(message);
                response.addButton(Login.this, "Ok", new View.OnClickListener() {

                    public void onClick(View v) {

                        response.dismiss();

                    }
                });

                response.show();

            }

        }

        logInLoader.dismiss();

    }

    private class logIn extends AsyncTask<String, String, String> {

        public logIn() {


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

                    request.setEntity(new UrlEncodedFormEntity(logindata));
                    HttpResponse response = client.execute(request);

                    String result;

                    switch(response.getStatusLine().getStatusCode()){

                        case 401: case 404: case 405: result = "401"; break;
                        default : result = EntityUtils.toString(response.getEntity()); Log.d("Http Post Response:", result);; break;

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

                    changeActivity(result);

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }

        }

    }

    private class resetPassword extends AsyncTask<String, String, String> {

        public resetPassword() {


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

                    String result;

                    switch(response.getStatusLine().getStatusCode()){

                        case 401: result = "401"; break;
                        case 404: result = "404"; break;
                        case 405: result = "405"; break;
                        default : result = EntityUtils.toString(response.getEntity()); break;

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

    public void showToast(String str) {

        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

    }

}