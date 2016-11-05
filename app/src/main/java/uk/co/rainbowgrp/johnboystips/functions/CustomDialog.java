package uk.co.rainbowgrp.johnboystips.functions;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import uk.co.rainbowgrp.johnboystips.R;

/**
 * @author DMS
 */
public class CustomDialog {

    public static class Builder {

        Context mContext;

        LayoutInflater inflater;
        View view;

        PopupWindow popup;

        TextView dialogMessage;
        TextView editTextError;

        DatePicker datePicker;

        ListView listView;

        EditText editText;

        WebView webView;

        private Handler mHandler;

        ImageView first;
        ImageView two;
        ImageView three;
        ImageView four;
        ImageView five;

        int loadPosition = 0;

        int i = 0;
        View.OnTouchListener customPopUpTouchListenr = new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                Log.d("POPUP", "Touch false");
                return false;
            }

        };
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

                                if (getEditText().getText().hashCode() == Hashcode) {

                                    if (c.length() > 0) {

                                        getEditTextError().setText("");

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


                    }

                }
            }

        };

        public Builder(Context context) {

            this.mContext = context;

            inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.dialog, null);

            popup = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
            popup.setContentView(view);
            //popup.setAnimationStyle(R.style.Animation);

            dialogMessage = new TextView(context);
            datePicker = new DatePicker(context);
            listView = new ListView(context);
            editText = new EditText(context);
            editTextError = new TextView(context);
            webView = new WebView(context);

        }

        public void setContentView(View view) {

            popup.setContentView(view);

        }

        public void setTitle(CharSequence title) {

            TextView dialogTitle = (TextView) view.findViewById(R.id.myTitle);
            dialogTitle.setText(title);

        }

        public void setTitle(int title) {

            TextView dialogTitle = (TextView) view.findViewById(R.id.myTitle);
            dialogTitle.setText(title);

        }

        public void setMessage(CharSequence message) {

            dialogMessage.setText(message);
            dialogMessage.setTextAppearance(mContext, R.style.Message);

            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            textParams.gravity = Gravity.CENTER;
            textParams.setMargins(dpToPx(20), dpToPx(25), dpToPx(20), dpToPx(25));

            LinearLayout addTo = (LinearLayout) view.findViewById(R.id.main);

            addTo.addView(dialogMessage, textParams);

        }

        private void addBorder() {

            LinearLayout addBorder = (LinearLayout) view.findViewById(R.id.main);
            LinearLayout.LayoutParams borderParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            TextView border = new TextView(mContext);

            borderParams.width = LayoutParams.MATCH_PARENT;
            borderParams.height = dpToPx(2);

            border.setBackgroundResource(R.color.dialog_border);

            addBorder.addView(border, borderParams);

        }

        public void show() {

            popup.setOutsideTouchable(false);
            popup.setTouchable(true);
            popup.setTouchInterceptor(customPopUpTouchListenr);


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    popup.showAtLocation(view, Gravity.CENTER, 0, 0);
                }
            }, 100L);

        }

        public void dismiss() {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    popup.dismiss();

                }
            }, 100L);
            //Looper.loop();
        }

        public void addEditText(){

            LinearLayout.LayoutParams lvParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams lvParams2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            lvParams.setMargins(dpToPx(20), dpToPx(10), dpToPx(20), dpToPx(0));

            LinearLayout addTo = (LinearLayout) view.findViewById(R.id.main);


            //editText.setBackgroundResource(R.drawable.edittext_bgr);
            editText.setTextColor(Color.BLACK);
            editText.setHint("example@domain.com");
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            editText.setOnFocusChangeListener(focusChanged);

            editTextError.setTextColor(mContext.getResources().getColor(R.color.error));

            addTo.addView(editText, lvParams);

            lvParams2.setMargins(dpToPx(25), dpToPx(10), dpToPx(20), dpToPx(35));

            addTo.addView(editTextError, lvParams2);

        }

        public void addWebView(String url) {

            LinearLayout.LayoutParams lvParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(400));

            lvParams.setMargins(dpToPx(20), dpToPx(10), dpToPx(20), dpToPx(0));

            LinearLayout addTo = (LinearLayout) view.findViewById(R.id.message);

            webView.loadUrl(url);

            addTo.addView(webView, lvParams);

        }

        public EditText getEditText(){

            return editText;

        }

        public TextView getEditTextError(){

            return editTextError;

        }

        public void addButton(Context context, String buttonText, View.OnClickListener listener) {

            if (i == 0) {

                addBorder();

            }

            i++;

            Button myButton = new Button(context);
            myButton.setText(buttonText);
            myButton.setBackgroundResource(R.drawable.button_dialog);
            myButton.setOnClickListener(listener);

            LinearLayout addTo = (LinearLayout) view.findViewById(R.id.main);

            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            addTo.addView(myButton, buttonParams);

        }

        public void addButtonGroup(Context context, String buttonText, View.OnClickListener listener) {

            if (i == 0) {

                addBorder();

            }

            i++;

            Button myButton = new Button(context);
            myButton.setText(buttonText);
            myButton.setBackgroundResource(R.drawable.button_dialog);
            myButton.setOnClickListener(listener);

            LinearLayout addTo = (LinearLayout) view.findViewById(R.id.btngroup);

            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams borderParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            if (buttonText.length() > 4) {

                myButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            }

            if (i == 3) {

                View border = new View(context);

                borderParams.width = dpToPx(2);
                borderParams.height = LayoutParams.MATCH_PARENT;

                border.setBackgroundResource(R.color.dialog_border_left);

                addTo.addView(border, borderParams);

            }

            buttonParams.width = 0;
            buttonParams.weight = 2;
            buttonParams.gravity = Gravity.CENTER;

            addTo.addView(myButton, buttonParams);

            if (i == 1) {

                View border = new View(context);

                borderParams.width = dpToPx(2);
                borderParams.height = LayoutParams.MATCH_PARENT;

                border.setBackgroundResource(R.color.dialog_border_left);
                addTo.addView(border, borderParams);

            }

        }

        public void addDatePicker() {

            LinearLayout.LayoutParams dpParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            LinearLayout addTo = (LinearLayout) view.findViewById(R.id.message);

            boolean tabletSize = mContext.getResources().getBoolean(R.bool.isTablet);

            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                dpParams.setMargins(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20));

            } else if (!tabletSize) {

                dpParams.setMargins(dpToPx(20), dpToPx(-20), dpToPx(20), dpToPx(-20));

            }

            datePicker.setCalendarViewShown(false);

            addTo.addView(datePicker, dpParams);

        }

        public DatePicker getDatePicker() {

            return datePicker;

        }

        public void disableKeyboard() {

            datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        }

        public void addListView() {

            LinearLayout.LayoutParams lvParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            LinearLayout addTo = (LinearLayout) view.findViewById(R.id.main);
            FrameLayout k = (FrameLayout) view.findViewById(R.id.frame);


            listView.setSelector(R.color.dialog_btn_hover);
            addTo.addView(listView, lvParams);

        }

        public ListView getListView() {

            return listView;

        }

        public void addLoader(String message) {

            dialogMessage.setText(message);
            dialogMessage.setTextAppearance(mContext, R.style.Message);

            View progressBar = createLoader();

            LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            progressParams.width = 0;
            progressParams.weight = 2;
            progressParams.setMargins(dpToPx(0), dpToPx(20), dpToPx(10), dpToPx(20));

            textParams.width = 0;
            textParams.weight = 3;
            textParams.gravity = Gravity.CENTER;
            textParams.setMargins(dpToPx(0), dpToPx(20), dpToPx(10), dpToPx(20));

            LinearLayout addTo = (LinearLayout) view.findViewById(R.id.message);

            addTo.addView(progressBar, progressParams);
            addTo.addView(dialogMessage, textParams);

            mStatusChecker.run();

        }

        public LinearLayout createLoader(){

            LinearLayout parent_view = new LinearLayout(mContext);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(25), dpToPx(25));

            params.setMargins(dpToPx(5), dpToPx(5), dpToPx(5), dpToPx(5));

            first = new ImageView(mContext);
            two = new ImageView(mContext);
            three = new ImageView(mContext);
            four = new ImageView(mContext);
            five = new ImageView(mContext);

            parent_view.addView(first, params);
            parent_view.addView(two, params);
            parent_view.addView(three, params);
            parent_view.addView(four, params);
            parent_view.addView(five, params);

            mHandler = new Handler();

            return parent_view;

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

        public int dpToPx(int dp) {
            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
            int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return px;
        }

    }

}
