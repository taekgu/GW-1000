package com.sinest.gw_1000.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_manager;


public class Activity_rfidcardpassord extends Activity {

    ImageView rfid_password_1; ImageView rfid_password_2; ImageView rfid_password_3;
    ImageView rfid_password_4; ImageView rfid_password_5; ImageView rfid_password_6;
    ImageView rfid_password_7; ImageView rfid_password_8; ImageView rfid_password_9; ImageView rfid_password_0;
    ImageView rfid_password_d; ImageView rfid_password_c; ImageView rfid_password_e; ImageView rfid_password_b;

    boolean[] r_password_kflag = {true,true,true,true,true,true,true,true,true,true};
    boolean[] r_password_flag = {true,true,true,true};
    boolean check_c = true;
    boolean check_m_c = false;

    TextView rfid_p;
    Intent intent_rfid;
    Intent intent_pscheck;

    char[] s_buf = {' ',' ',' ',' '};
    String ps = "";
    int int_buf = 0;
    int int_c = 0;
    String mater_ps = "5866";
    String password = "0000";

    Intent check;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_rfid_card_passord);

        Application_manager.setFullScreen(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        password = Application_manager.get_m_password();

        rfid_password_1 = (ImageView) findViewById(R.id.rfid_password_1);
        rfid_password_2 = (ImageView) findViewById(R.id.rfid_password_2);
        rfid_password_3 = (ImageView) findViewById(R.id.rfid_password_3);
        rfid_password_4 = (ImageView) findViewById(R.id.rfid_password_4);
        rfid_password_5 = (ImageView) findViewById(R.id.rfid_password_5);
        rfid_password_6 = (ImageView) findViewById(R.id.rfid_password_6);
        rfid_password_7 = (ImageView) findViewById(R.id.rfid_password_7);
        rfid_password_8 = (ImageView) findViewById(R.id.rfid_password_8);
        rfid_password_9 = (ImageView) findViewById(R.id.rfid_password_9);
        rfid_password_0 = (ImageView) findViewById(R.id.rfid_password_0);
        rfid_password_d = (ImageView) findViewById(R.id.rfid_password_d);
        rfid_password_c = (ImageView) findViewById(R.id.rfid_password_c);
        rfid_password_e = (ImageView) findViewById(R.id.rfid_password_e);
        rfid_password_b = (ImageView) findViewById(R.id.rfid_password_b);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        rfid_p = (TextView) findViewById(R.id.rfid_p);
        rfid_p.setTypeface(tf);


        intent_rfid = new Intent(this, Activity_rfid.class);
        intent_rfid.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                Application_manager.set_m_start_sleep(0);
                switch (v.getId()) {
                    case R.id.rfid_password_1:
                        //
                        addps('1');
                        ps = ""+s_buf[0]+s_buf[1]+s_buf[2]+s_buf[3];
                        Log.v("test","ps : "+ps);
                        rfid_p.setText(ps);
                        break;
                    case R.id.rfid_password_2:
                        //
                        addps('2');
                        ps = ""+s_buf[0]+s_buf[1]+s_buf[2]+s_buf[3];
                        rfid_p.setText(ps);
                        break;
                    case R.id.rfid_password_3:
                        //
                        addps('3');
                        ps = ""+s_buf[0]+s_buf[1]+s_buf[2]+s_buf[3];
                        rfid_p.setText(ps);
                        break;
                    case R.id.rfid_password_4:
                        //
                        addps('4');
                        ps = ""+s_buf[0]+s_buf[1]+s_buf[2]+s_buf[3];
                        rfid_p.setText(ps);
                        break;
                    case R.id.rfid_password_5:
                        //
                        addps('5');
                        ps = ""+s_buf[0]+s_buf[1]+s_buf[2]+s_buf[3];
                        rfid_p.setText(ps);
                        break;
                    case R.id.rfid_password_6:
                        //
                        addps('6');
                        ps = ""+s_buf[0]+s_buf[1]+s_buf[2]+s_buf[3];
                        rfid_p.setText(ps);
                        break;
                    case R.id.rfid_password_7:
                        //
                        addps('7');
                        ps = ""+s_buf[0]+s_buf[1]+s_buf[2]+s_buf[3];
                        rfid_p.setText(ps);
                        break;
                    case R.id.rfid_password_8:
                        //
                        addps('8');
                        ps = ""+s_buf[0]+s_buf[1]+s_buf[2]+s_buf[3];
                        rfid_p.setText(ps);
                        break;
                    case R.id.rfid_password_9:
                        //
                        addps('9');
                        ps = ""+s_buf[0]+s_buf[1]+s_buf[2]+s_buf[3];
                        rfid_p.setText(ps);
                        break;
                    case R.id.rfid_password_0:
                        //
                        addps('0');
                        ps = ""+s_buf[0]+s_buf[1]+s_buf[2]+s_buf[3];
                        rfid_p.setText(ps);
                        break;
                    //-----------------------------------------------
                    case R.id.rfid_password_d:
                        //
                        delps();
                        ps = ""+s_buf[0]+s_buf[1]+s_buf[2]+s_buf[3];
                        rfid_p.setText(ps);
                        break;
                    case R.id.rfid_password_c:
                        //
                        check_c = false;
                        rfid_p.setText("Previous password");
                        rfid_p.setTextSize(25);
                        setzeros();
                        break;
                    case R.id.rfid_password_b:
                        //
                        Application_manager.rfid_pass_f = true;
                        finish();
                        break;
                    case R.id.rfid_password_e:
                        //
                        if(check_c == true)
                        {
                            if(ps.equals(mater_ps)){
                                password = "0000";
                            }
                            else if(ps.equals(password)){
                                Application_manager.rfid_pass_f = true;
                                Application_manager.rfid_pass_f2 = true;
                                finish();
                            }
                            else{
                                rfid_p.setText("!"+s_buf[0]+s_buf[1]+s_buf[2]+s_buf[3]);
                                setzeros();
                            }
                        }else if(check_c == false)
                        {
                            // change 버튼이 눌렸을시
                            if(check_m_c == true)
                            {
                                password = ps;
                                Application_manager.set_m_password(password);
                                Log.v("test",""+password);
                                rfid_p.setText("Password Change");
                                rfid_p.setTextSize(25);
                                setzeros();
                                check_m_c = false;
                                check_c = true;
                            }else
                            {
                                if(ps.equals(password))
                                {
                                    rfid_p.setText("New password");
                                    rfid_p.setTextSize(25);
                                    setzeros();
                                    check_m_c = true;
                                }else{
                                    rfid_p.setText("Invalid password");
                                    rfid_p.setTextSize(25);
                                    setzeros();
                                }
                            }
                        }

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

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return false;
    }

    void addps(char num){
        if(int_c == 0)
        {
            s_buf[0] = num;
            int_c++;
        }else if(int_c == 1)
        {
            s_buf[1] = num;
            int_c++;
        }else if(int_c == 2)
        {
            s_buf[2] = num;
            int_c++;
        }else if(int_c == 3)
        {
            s_buf[3] = num;
            int_c++;
        }else if(int_c == 4){
            s_buf[0] = s_buf[1];
            s_buf[1] = s_buf[2];
            s_buf[2] = s_buf[3];
            s_buf[3] = num;
        }
        rfid_p.setTextSize(50);
    }

    void delps(){
        if(int_c == 0)
        {
            s_buf[0] = ' ';
        }else if(int_c == 1)
        {
            s_buf[0] = ' ';
            int_c--;
        }else if(int_c == 2)
        {
            s_buf[1] = ' ';
            int_c--;
        }else if(int_c == 3)
        {
            s_buf[2] = ' ';
            int_c--;
        }else if(int_c == 4){
            s_buf[3] = ' ';
            int_c--;
        }
    }

    void setzeros()
    {
        int_buf = 0;
        int_c = 0;
        s_buf[0] = ' ';
        s_buf[1] = ' ';
        s_buf[2] = ' ';
        s_buf[3] = ' ';
    }
}
