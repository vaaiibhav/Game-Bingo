package com.example.mainscreen;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btn_Coin5000;
    private ImageView btn_Coin1000;
    private ImageView btn_Coin500;
    private ImageView btn_Coin100;
    private ImageView btn_Coin50;
    private ImageView btn_Coin10;
    private ImageView iv_spinwheel;
    private int currentBet;
    private int anybet;
    RotateAnimation ranim;
    private static final float ROTATE_FROM = 0.0f;
    private static float ROTATE_TO;// 3.141592654f * 32.0f;
    private static int score = 0;
    final int[] userCoins = new int[1];
    private String UpEmail;
    private int totalbet;
    private Handler mha;
    private Handler mhand;
    private TextView tv_ddummyNo;
    /**
     * 4
     */
    private TextView tv_winnerNo;
    /**
     * tv_score
     */
    private TextView tv_score;
    /**
     * tv_score
     */
    private TextView tv_timer;
    /**
     * tv_score
     */
    private TextView tv_winner;
    /**
     * Take
     */
//    private Button btn_takebet;
    /**
     * Bet Ok
     */

    private Button btn_betOK;
    private TextView tv_bet1;
    private TextView tv_bet2;
    private TextView tv_bet3;
    private TextView tv_bet4;
    private TextView tv_bet5;
    private TextView tv_bet6;
    private TextView tv_bet7;
    private TextView tv_bet8;
    private TextView tv_bet9;
    private TextView tv_bet0;
    private Button btn_settings;
    /**
     * 1
     */
    private ImageView btn_bet1;
    /**
     * 2
     */
    private ImageView btn_bet2;
    /**
     * 3
     */
    private ImageView btn_bet3;
    /**
     * 4
     */
    private ImageView btn_bet4;
    /**
     * 5
     */
    private ImageView btn_bet5;
    /**
     * 6
     */
    private ImageView btn_bet6;
    /**
     * 7
     */
    private ImageView btn_bet7;
    /**
     * 8
     */
    private ImageView btn_bet8;
    /**
     * 9
     */
    private ImageView btn_bet9;
    /**
     * 0
     */
    private ImageView btn_bet0;

    private TextView tv_totalBet;
    int betBox[];
    int flagBetOK = 0;
    MediaPlayer mp;
    private int betAmnt;
    ConstantKeys Ck;
    Handler hl = new Handler();
    SharedPreferences sp;
    SharedPreferences.Editor spedit;
    String GameID;
    private Socket socket;
    private int winMultiplier;
    String userEmail;
    Handler h2 = new Handler();
    private int winnerNumber;
    private Handler mHandler;
    private int nobet;
    //    private Button btn_takebet2;
    private int[] myArray;
    private TextView mTvLastbet1;
    private TextView mTvLastbet2;
    private TextView mTvLastbet3;
    private TextView mTvLastbet4;
    private TextView mTvLastbet5;
    private TextView mTvLastbet6;
    private TextView mTvLastbet7;
    private TextView mTvLastbet8;
    private TextView mTvLastbet9;
    private TextView mTvLastbet0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                blink();

            }
        },5000);

        myArray = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        initView();
        mhand = new Handler();
        mha = new Handler();
        UpEmail =  userSession.getInstance().getUsrname();
//        Toast.makeText(this, UpEmail, Toast.LENGTH_SHORT).show();
        betBox = new int[10];
        Arrays.fill(betBox, 0);
        updareBoxes(betBox);
        //  score = userSession.getInstance().getUsrcoins();
        // score = 500;
        play_music();
        Ck = new ConstantKeys();
        sp = getSharedPreferences(Ck.LOGINSHAREDPREFKEYS, 0);
        tv_score.setText("" + score);
        serverConnect();

        getUsercoins();
        winMultiplier = 9;
        mHandler = new Handler();
        anybet = 0;
        totalbet=0;

    }

    private void getServerCountdown(final JSONObject timer) {

        final String[] count = new String[1];
        final int[] countdown_int = new int[1];
        final int[] countdown_int_disp = new int[1];
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    count[0] = timer.getString("countdown");
                    countdown_int[0] = Integer.parseInt(count[0]);
                    countdown_int_disp[0] = countdown_int[0] - 10;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    public void run() {
                        // Actions to do after 10 seconds
                        if (countdown_int_disp[0] < 0) {
                            tv_timer.setText("Wait");

                        } else {
                            tv_timer.setText("00:" + (countdown_int_disp[0]));

                        }

                    }
                });
                        /*Handler mha = new Handler();
                        mha.post(new Runnable() {
                            public void run() {

                                tv_ddummyNo.setText("");
                            }
                        });
                        */
                if (countdown_int[0] == 10) {
                    myArray = Arrays.copyOf(betBox, betBox.length);
                    nobet = 1;
                    if (anybet ==1){
//                        Toast.makeText(GameActivity.this, "Bet Placed", Toast.LENGTH_SHORT).show();
                        sendGameData();}

                }
                if (countdown_int[0] == 5) {
//                            justSpin();
//                            winnCalculator();
                    getwin();
                    anybet = 0;

//                    Toast.makeText(GameActivity.this, "Bet Reset", Toast.LENGTH_SHORT).show();
                }
                if (countdown_int[0] == 68) {
                    nobet = 0;
                    lastwinn();
                    showlastgamebets();
                    totalbet =0;


//                            Toast.makeText(GameActivity.this,"Gmaeid:"+ GameID, Toast.LENGTH_SHORT).show();
                }
                socket.emit("getwins", 0);
            }

        });

    }


    private void getwin() {
        socket.emit("getwinno", "1574");
        socket.on("winno", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println(args);
                winnerNumber = Integer.parseInt(String.valueOf(args[0]));
                System.out.println("winnerNumber: " + winnerNumber);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        spinwheel(winnerNumber);
                    }
                });
            }
        });
    }

    private void justSpin() {
        ranim = new RotateAnimation(30, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ranim.setRepeatCount(Animation.INFINITE);
        ranim.setRepeatMode(500);

        ranim.setDuration((long) 1000);
//        ranim.setRepeatCount(5);
        iv_spinwheel.startAnimation(ranim);
    }

    private void serverConnect() {
        try {
            socket = IO.socket("http://192.168.0.120:11619/");
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {


            }

        }).on("event", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                finish();
            }

        }).on("timer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject timer = (JSONObject) args[0];
                getServerCountdown(timer);

            }
        }).on("newgameid", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i("THISUIS",""+args[0]);
                JSONObject gameargs = (JSONObject) args[0];
//                lastwinn();
                try {
                    GameID = gameargs.getString("newgame");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // GameID= (int) args[0];;
            }
        });


    }

    private void lastwinn() {
        socket.emit("getlastenwin", "123123");
        socket.on("lastwinsare", new Emitter.Listener() {

            @Override
            public void call(final Object... args) {
//                Log.d ("Lastwinsare", args[0]);
                System.out.println(args[0]);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_ddummyNo.setText((CharSequence) args[0]);
                    }
                });
            }
        });
        getUsercoins();
//
//        final RequestQueue rq = Volley.newRequestQueue(this);
//        StringRequest srq = new StringRequest(Request.Method.POST, Ck.win_URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                tv_ddummyNo.setText(response);
//                rq.stop();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }){ @Override
//        protected Map<String, String> getParams() throws AuthFailureError {
//            Map<String, String> map = new HashMap<>();
//
//
//            return map;
//
//        }
//        };
//        rq.add(srq);
//        rq.start();


    }


    private void initView() {
        btn_Coin5000 =findViewById(R.id.btn_Coin5000);
        btn_Coin5000.setOnClickListener(this);
        btn_Coin1000 = findViewById(R.id.btn_Coin1000);
        btn_Coin1000.setOnClickListener(this);
        btn_Coin500 =  findViewById(R.id.btn_Coin500);
        btn_Coin500.setOnClickListener(this);
        btn_Coin100 =  findViewById(R.id.btn_Coin100);
        btn_Coin100.setOnClickListener(this);
        btn_Coin50 =  findViewById(R.id.btn_Coin50);
        btn_Coin50.setOnClickListener(this);
        btn_Coin10 = findViewById(R.id.btn_Coin10);
        btn_Coin10.setOnClickListener(this);
        iv_spinwheel = (ImageView) findViewById(R.id.iv_spinwheel);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_winner = (TextView) findViewById(R.id.tv_winner);
//        tv_ddummyNo = (TextView) findViewById(R.id.tv_ddummyNo);
//        btn_takebet2 = (Button) findViewById(R.id.btn_takebet2);
//        btn_takebet2.setOnClickListener(this);
//        btn_takebet.setOnClickListener(this);
//        btn_betOK.setOnClickListener(this);
        tv_bet1 = (TextView) findViewById(R.id.tv_bet1);
        tv_bet2 = (TextView) findViewById(R.id.tv_bet2);
        tv_bet3 = (TextView) findViewById(R.id.tv_bet3);
        tv_bet4 = (TextView) findViewById(R.id.tv_bet4);
        tv_bet5 = (TextView) findViewById(R.id.tv_bet5);
        tv_bet6 = (TextView) findViewById(R.id.tv_bet6);
        tv_bet7 = (TextView) findViewById(R.id.tv_bet7);
        tv_bet8 = (TextView) findViewById(R.id.tv_bet8);
        tv_bet9 = (TextView) findViewById(R.id.tv_bet9);
        tv_bet0 = (TextView) findViewById(R.id.tv_bet0);
//        tv_totalBet= (TextView)findViewById(R.id.tv_totalbet);
//        btn_settings = (Button) findViewById(R.id.btn_settings);
//        btn_settings.setOnClickListener(this);
        btn_bet1 =  findViewById(R.id.btn_bet1);
        btn_bet1.setOnClickListener(this);
        btn_bet2 =  findViewById(R.id.btn_bet2);
        btn_bet2.setOnClickListener(this);
        btn_bet3 =  findViewById(R.id.btn_bet3);
        btn_bet3.setOnClickListener(this);
        btn_bet4 =  findViewById(R.id.btn_bet4);
        btn_bet4.setOnClickListener(this);
        btn_bet5 =  findViewById(R.id.btn_bet5);
        btn_bet5.setOnClickListener(this);
        btn_bet6 =  findViewById(R.id.btn_bet6);
        btn_bet6.setOnClickListener(this);
        btn_bet7 =  findViewById(R.id.btn_bet7);
        btn_bet7.setOnClickListener(this);
        btn_bet8 =  findViewById(R.id.btn_bet8);
        btn_bet8.setOnClickListener(this);
        btn_bet9 =  findViewById(R.id.btn_bet9);
        btn_bet9.setOnClickListener(this);
        btn_bet0 =  findViewById(R.id.btn_bet0);
        btn_bet0.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Coin5000:
//                BetOn( currentBet, 5000);
                betAmnt = 5000;
                break;
            case R.id.btn_Coin1000:
//                BetOn( currentBet, 1000);
                betAmnt = 1000;

                break;
            case R.id.btn_Coin500:
//                BetOn( currentBet, 500);
                betAmnt = 500;

                break;
            case R.id.btn_Coin100:
//                BetOn( currentBet, 100);
                betAmnt = 100;

                break;
            case R.id.btn_Coin50:
//                BetOn( currentBet, 50);
                betAmnt = 50;

                break;
            case R.id.btn_Coin10:
//                BetOn( currentBet, 10);
                betAmnt = 10;

                break;
//            case R.id.btn_takebet:
//
//                ////PayTM OPTIONvaibhav
////                userSession.getInstance().setUsrcoins(score);
////                Intent buyCoinIntent = new Intent(this, PaytmActivity.class);
////                startActivity(buyCoinIntent);
//
//                break;
//            case R.id.btn_takebet2:
//                showlastgamebets();

//                break;
//            case R.id.btn_betOK:
//
//                resetBets();
//                break;
//            case R.id.btn_settings:
//                break;
            case R.id.btn_bet1:
                currentBet = 1;
                BetOn(currentBet, betAmnt);
                break;
            case R.id.btn_bet2:
                currentBet = 2;
                BetOn(currentBet, betAmnt);
                break;
            case R.id.btn_bet3:
                currentBet = 3;
                BetOn(currentBet, betAmnt);
                break;
            case R.id.btn_bet4:
                currentBet = 4;
                BetOn(currentBet, betAmnt);
                break;
            case R.id.btn_bet5:
                currentBet = 5;
                BetOn(currentBet, betAmnt);
                break;
            case R.id.btn_bet6:
                currentBet = 6;
                BetOn(currentBet, betAmnt);
                break;
            case R.id.btn_bet7:
                currentBet = 7;
                BetOn(currentBet, betAmnt);
                break;
            case R.id.btn_bet8:
                currentBet = 8;
                BetOn(currentBet, betAmnt);
                break;
            case R.id.btn_bet9:
                currentBet = 9;
                BetOn(currentBet, betAmnt);
                break;
            case R.id.btn_bet0:

                currentBet = 0;
                BetOn(currentBet, betAmnt);
                break;
        }
    }

    private void showlastgamebets() {
//        StringBuilder builder = new StringBuilder();
//        for (int i : myArray) {
//            builder.append("" + i + "-");
//        }
//        Toast.makeText(this, builder, Toast.LENGTH_SHORT).show();
        tv_bet0.setText(""+myArray[0]);
        tv_bet1.setText(""+myArray[1]);
        tv_bet2.setText(""+myArray[2]);
        tv_bet3.setText(""+myArray[3]);
        tv_bet4.setText(""+myArray[4]);
        tv_bet5.setText(""+myArray[5]);
        tv_bet6.setText(""+myArray[6]);
        tv_bet7.setText(""+myArray[7]);
        tv_bet8.setText(""+myArray[8]);
        tv_bet9.setText(""+myArray[9]);


    }

    private void getUsercoins() {
        final int[] userCoins = new int[1];
        JSONObject getcoins = new JSONObject();
        try {
            getcoins.put("uname", UpEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("getcoins", getcoins);
        socket.on("ucoins", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                userCoins[0] = (int) args[0];
//            totalbet=userCoins[0];

           /*  try {
                 userCoins[0] = data.getInt("ucoins");
             } catch (JSONException e) {
                 e.printStackTrace();
             }*/
                ShowUserCoins(userCoins[0]);
            }

        });

    }

    private void ShowUserCoins(final int userCoin) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_score.setText("" + userCoin);
                score = userCoin;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void BetOn(int betOn, int c) {

        if (nobet == 1) {
            Toast.makeText(this, "Cannot Place Bet, Please Wait", Toast.LENGTH_SHORT).show();
        }
        else {
//            mp = MediaPlayer.create(getApplicationContext(), R.raw.coin_add);
            switch (betOn) {

                case 1:

                    if (c < score) {
                        anybet =1;

                        totalbet = totalbet + c;
                        score = score - c;
                        betBox[1] = betBox[1] + c;
                        tv_score.setText("" + score);
                        //mp.start();
                    } else {
//                        Toast.makeText(this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:

                    if (c < score) {
                        anybet =1;

                        totalbet = totalbet + c;

                        score = score - c;
                        tv_score.setText("" + score);
                        betBox[2] = betBox[2] + c;
                        //mp.start();
                    } else {
//                        Toast.makeText(this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:

                    if (c < score) {
                        anybet =1;

                        totalbet = totalbet + c;
                        score = score - c;

                        tv_score.setText("" + score);
                        betBox[3] = betBox[3] + c;
                        //mp.start();
                    } else {
//                        Toast.makeText(this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
                    }


                    break;
                case 4:
                    if (c < score) {
                        anybet =1;

                        totalbet = totalbet + c;

                        score = score - c;
                        tv_score.setText("" + score);
                        betBox[4] = betBox[4] + c;
                        //mp.start();
                    } else {
//                        Toast.makeText(this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 5:
                    if (c < score) {
                        anybet =1;

                        totalbet = totalbet + c;

                        score = score - c;
                        tv_score.setText("" + score);
                        betBox[5] = betBox[5] + c;
                        //mp.start();
                    } else {
//                        Toast.makeText(this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 6:
                    if (c < score) {
                        anybet =1;

                        totalbet = totalbet + c;

                        score = score - c;
                        tv_score.setText("" + score);
                        betBox[6] = betBox[6] + c;
                        //mp.start();
                    } else {
//                        Toast.makeText(this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 7:
                    if (c < score) {
                        anybet =1;

                        totalbet = totalbet + c;

                        score = score - c;
                        tv_score.setText("" + score);
                        betBox[7] = betBox[7] + c;
                        //mp.start();
                    } else {
//                        Toast.makeText(this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 8:
                    if (c < score) {
                        anybet =1;

                        totalbet = totalbet + c;

                        score = score - c;


                        tv_score.setText("" + score);
                        betBox[8] = betBox[8] + c;
                        //mp.start();
                    } else {
//                        Toast.makeText(this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 9:
                    if (c < score) {
                        anybet =1;

                        totalbet = totalbet + c;

                        score = score - c;
                        tv_score.setText("" + score);
                        betBox[9] = betBox[9] + c;
                        //mp.start();
                    } else {
//                        Toast.makeText(this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 0:
                    if (c < score) {
                        anybet =1;

                        totalbet = totalbet + c;
                        score = score - c;
                        tv_score.setText("" + score);
                        betBox[0] = betBox[0] + c;
//                        //mp.start();
                    } else {
//                        Toast.makeText(this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 21:
                    Arrays.fill(betBox, 0);
                    break;

            }
//            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    mp.stop();
//                    mp.release();
//                }
//            });
//            tv_totalBet.setText(""+totalbet);
            mhand.post(new Runnable() {
                @Override
                public void run() {


                }
            });
        }
        updareBoxes(betBox);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_NUMPAD_0:
                BetOn(0, betAmnt);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_1:
                BetOn(1, betAmnt);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_2:
                BetOn(2, betAmnt);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_3:
                BetOn(3, betAmnt);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_4:
                BetOn(4, betAmnt);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_5:
                BetOn(5, betAmnt);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_6:
                BetOn(6, betAmnt);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_7:
                BetOn(7, betAmnt);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_8:
                BetOn(8, betAmnt);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_9:
                BetOn(9, betAmnt);
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void updareBoxes(int[] betBox) {
        tv_bet0.setText(betBox[0] + "");
        tv_bet1.setText(betBox[1] + "");
        tv_bet2.setText(betBox[2] + "");
        tv_bet3.setText(betBox[3] + "");
        tv_bet4.setText(betBox[4] + "");
        tv_bet5.setText(betBox[5] + "");
        tv_bet6.setText(betBox[6] + "");
        tv_bet7.setText(betBox[7] + "");
        tv_bet8.setText(betBox[8] + "");
        tv_bet9.setText(betBox[9] + "");


    }


    private void sendGameData() {

        JSONObject sendgame = new JSONObject();
        try {
            sendgame.put("gmeid", GameID);
            sendgame.put("uname", UpEmail);
            sendgame.put("bet1", String.valueOf(betBox[1]));
            sendgame.put("bet2", String.valueOf(betBox[2]));
            sendgame.put("bet3", String.valueOf(betBox[3]));
            sendgame.put("bet4", String.valueOf(betBox[4]));
            sendgame.put("bet5", String.valueOf(betBox[5]));
            sendgame.put("bet6", String.valueOf(betBox[6]));
            sendgame.put("bet7", String.valueOf(betBox[7]));
            sendgame.put("bet8", String.valueOf(betBox[8]));
            sendgame.put("bet9", String.valueOf(betBox[9]));
            sendgame.put("bet0", String.valueOf(betBox[0]));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("savegame", sendgame);

    }
    private void winnCalculator() {
        RequestQueue rqgame = Volley.newRequestQueue(this);
        StringRequest srq = new StringRequest(Request.Method.POST, Ck.game_score_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                winnerNumber = Integer.parseInt(response);
                System.out.println("winnerNumber: " + winnerNumber);

                spinwheel(winnerNumber);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                //map.put(,uName);
                map.put("gmeid", String.valueOf(GameID));


                return map;

            }
        };
        rqgame.add(srq);

    }

    private void resetBets() {
        Arrays.fill(betBox, 0);
        updareBoxes(betBox);
        getUsercoins();
//        totalbet= 0;


    }

    private void updateServerCoins() {
        JSONObject upcoins = new JSONObject();
        try {
            upcoins.put("uname", UpEmail);
            //upcoins.put("uname",userEmail);
            upcoins.put("ucoins", score);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("updatecoins", upcoins);

    }


    private void winscore(int winnerNumber) {
        final int wintotal = betBox[winnerNumber] * winMultiplier;
        mp = MediaPlayer.create(getApplicationContext(), R.raw.coins_win);

        score = score + wintotal;
        tv_winner.setText("" + wintotal);
        if (wintotal>0){
            //mp.start();
        }

        mhand.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_score.setText("" + score);
                mp.stop();
                mp.release();
            }
        }, 1500);


    }

    private void spinwheel(final int i) {


        switch (i) {

            case 0:
                ROTATE_TO = (float) 144;
//                winscore(0);
                break;
            case 1:
                ROTATE_TO = (float) 108;

//                winscore(1);
                break;
            case 2:
                ROTATE_TO = (float) 72;
//                winscore(2);
                break;
            case 3:
                ROTATE_TO = (float) 36;
//                winscore(3);
                break;
            case 4:
                ROTATE_TO = (float) 360;
//                winscore(4);
                break;

            case 5:
                ROTATE_TO = (float) 325;
//                winscore(5);
                break;
            case 6:
                ROTATE_TO = (float) 289;
//                winscore(6);
                break;
            case 7:
                ROTATE_TO = (float) 253;
//                winscore(7);
                break;
            case 8:
                ROTATE_TO = (float) 217;
//                winscore(8);
                break;
            case 9:
                ROTATE_TO = (float) 181;
//                winscore(9);
                break;
            default:
                ROTATE_TO = (float) 360;
                break;


        }

        ranim = new RotateAnimation(ROTATE_FROM, ROTATE_TO, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        ranim.setDuration((long) 400);
        ranim.setRepeatCount(7);
        ranim.setFillAfter(true);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.wheel_mixdown);
        ranim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                //mp.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mp.stop();
                mp.release();
                winscore(i);
//                mp = MediaPlayer.create(getApplicationContext(), R.raw.winner_two);
//                //mp.start();
                tv_winnerNo.setText("" + i);
                tv_score.setText("" + score);


//                tv_winmount.setText(""+betBox[i]*winMultiplier);
                updateServerCoins();
                resetBets();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                iv_spinwheel.startAnimation(ranim);
            }
        });

//        resetBets();
    }

    private void play_music() {

    }

    @Override
    protected void onPause() {
//       startActivity(new Intent(this,LoginActivity.class));

        super.onPause();

    }

    @Override
    protected void onStart() {
        score = userSession.getInstance().getUsrcoins();
        userEmail = userSession.getInstance().getUsrname();

        super.onStart();

    }

    @Override
    protected void onStop() {

        super.onStop();
        finish();
    }

    public void onDestroy() {


        socket.disconnect();
        super.onDestroy();
    }

    private void blink(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 1000;    //in milissegunds
                try{Thread.sleep(timeToBlink);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(tv_timer.getVisibility() == View.VISIBLE){
                            tv_timer.setVisibility(View.INVISIBLE);
                        }else{
                            tv_timer.setVisibility(View.VISIBLE);
                        }
                        blink();
                    }
                });
            }
        }).start();
    }

}