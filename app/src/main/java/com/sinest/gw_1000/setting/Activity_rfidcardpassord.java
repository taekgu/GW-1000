package com.sinest.gw_1000.setting;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sinest.gw_1000.R;

public class Activity_rfidcardpassord extends Activity {

    Button rfid_password_1; Button rfid_password_2; Button rfid_password_3;
    Button rfid_password_4; Button rfid_password_5; Button rfid_password_6;
    Button rfid_password_7; Button rfid_password_8; Button rfid_password_9; Button rfid_password_0;
    Button rfid_password_d; Button rfid_password_c; Button rfid_password_e; Button rfid_password_b;

    boolean[] r_password_kflag = {true,true,true,true,true,true,true,true,true,true};
    boolean[] r_password_flag = {true,true,true,true};

    TextView rfid_p;
    Intent intent_rfid;

    String s_buf;
    int int_buf;
    int int_c = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_rfid_card_passord);

        rfid_password_1 = (Button) findViewById(R.id.rfid_password_1);
        rfid_password_2 = (Button) findViewById(R.id.rfid_password_2);
        rfid_password_3 = (Button) findViewById(R.id.rfid_password_3);
        rfid_password_4 = (Button) findViewById(R.id.rfid_password_4);
        rfid_password_5 = (Button) findViewById(R.id.rfid_password_5);
        rfid_password_6 = (Button) findViewById(R.id.rfid_password_6);
        rfid_password_7 = (Button) findViewById(R.id.rfid_password_7);
        rfid_password_8 = (Button) findViewById(R.id.rfid_password_8);
        rfid_password_9 = (Button) findViewById(R.id.rfid_password_9);
        rfid_password_0 = (Button) findViewById(R.id.rfid_password_0);
        rfid_password_d = (Button) findViewById(R.id.rfid_password_d);
        rfid_password_c = (Button) findViewById(R.id.rfid_password_c);
        rfid_password_e = (Button) findViewById(R.id.rfid_password_e);
        rfid_password_b = (Button) findViewById(R.id.rfid_password_b);

        rfid_p = (TextView) findViewById(R.id.rfid_p);

        intent_rfid = new Intent(this, Activity_rfid.class);
        intent_rfid.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rfid_password_1:
                        //
                        if(int_c >= 4)
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf);
                            int_buf = (int_buf%1000)*10 + 1;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                        }else
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf)*10 + 1;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                            int_c++;
                        }
                        break;
                    case R.id.rfid_password_2:
                        //
                        if(int_c >= 4)
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf);
                            int_buf = (int_buf%1000)*10 + 2;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                        }else
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf)*10 + 2;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                            int_c++;
                        }
                        break;
                    case R.id.rfid_password_3:
                        //
                        if(int_c >= 4)
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf);
                            int_buf = (int_buf%1000)*10 + 3;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                        }else
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf)*10 + 3;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                            int_c++;
                        }
                        break;
                    case R.id.rfid_password_4:
                        //
                        if(int_c >= 4)
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf);
                            int_buf = (int_buf%1000)*10 + 4;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                        }else
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf)*10 + 4;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                            int_c++;
                        }
                        break;
                    case R.id.rfid_password_5:
                        //
                        if(int_c >= 4)
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf);
                            int_buf = (int_buf%1000)*10 + 5;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                        }else
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf)*10 + 5;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                            int_c++;
                        }
                        break;
                    case R.id.rfid_password_6:
                        //
                        if(int_c >= 4)
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf);
                            int_buf = (int_buf%1000)*10 + 6;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                        }else
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf)*10 + 6;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                            int_c++;
                        }
                        break;
                    case R.id.rfid_password_7:
                        //
                        if(int_c >= 4)
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf);
                            int_buf = (int_buf%1000)*10 + 7;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                        }else
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf)*10 + 7;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                            int_c++;
                        }
                        break;
                    case R.id.rfid_password_8:
                        //
                        if(int_c >= 4)
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf);
                            int_buf = (int_buf%1000)*10 + 8;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                        }else
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf)*10 + 8;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                            int_c++;
                        }
                        break;
                    case R.id.rfid_password_9:
                        //
                        if(int_c >= 4)
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf);
                            int_buf = (int_buf%1000)*10 + 9;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                        }else
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf)*10 + 9;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                            int_c++;
                        }
                        break;
                    case R.id.rfid_password_0:
                        //
                        if(int_c >= 4)
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf);
                            int_buf = (int_buf%1000)*10 + 0;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                        }else
                        {
                            s_buf = (String)rfid_p.getText();
                            int_buf = Integer.parseInt(s_buf)*10 + 0;
                            s_buf = Integer.toString(int_buf);
                            rfid_p.setText(s_buf);
                            int_c++;
                        }
                        break;
                    //-----------------------------------------------
                    case R.id.rfid_password_d:
                        //
                        s_buf = (String)rfid_p.getText();
                        int_buf = Integer.parseInt(s_buf);
                        int_buf = int_buf/10;
                        s_buf = Integer.toString(int_buf);
                        rfid_p.setText(s_buf);
                        break;
                    case R.id.rfid_password_c:
                        //

                        break;
                    case R.id.rfid_password_b:
                        //
                        finish();
                        break;
                    case R.id.rfid_password_e:
                        //
                        startActivity(intent_rfid);
                        finish();
                        break;
                }
                //Toast.makeText(Activity_rfidcardpassord.this, s_buf, Toast.LENGTH_SHORT).show();
            }
        };

        rfid_password_1.setOnClickListener(listener);
        rfid_password_2.setOnClickListener(listener);
        rfid_password_3.setOnClickListener(listener);
        rfid_password_4.setOnClickListener(listener);
        rfid_password_5.setOnClickListener(listener);
        rfid_password_6.setOnClickListener(listener);
        rfid_password_7.setOnClickListener(listener);
        rfid_password_8.setOnClickListener(listener);
        rfid_password_9.setOnClickListener(listener);
        rfid_password_0.setOnClickListener(listener);
        rfid_password_d.setOnClickListener(listener);
        rfid_password_c.setOnClickListener(listener);
        rfid_password_e.setOnClickListener(listener);
        rfid_password_b.setOnClickListener(listener);

    }
}
