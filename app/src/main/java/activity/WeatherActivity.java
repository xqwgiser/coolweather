package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.xqw.coolweather.R;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

import java.io.InputStream;

import service.AutoUpdateService;
import util.HttpUtil;
import util.Utility;

/**
 * Created by xqw on 2015/5/5.
 */
public class WeatherActivity extends Activity implements View.OnClickListener {
    public static final int Qing=0;
    public static final int Yin=1;
    public static final int Duoyun=2;
    public static final int Xiaoyu=3;
    public static final int Zhongyu=4;
    public static final int Dayu=5;
    public static final int Baoyu=6;
    public static final int Zhenyu=7;
    public static final int Leizhenyu=8;
    public static final int Yujiaxue=9;
    public static final int Xiaoxue=10;
    public static final int Zhongxue=11;
    public static final int Daxue=12;
    public static final int Baoxue=13;
    private int weatherstate;
    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    private TextView publishText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView currentDateText;
    private TextView clText;
    private TextView lyText;
    private TextView ssdText;
    private Button switchCity;
    private Button refreshWeather;
    private ImageView imageView;
    private ImageView bg;
    private ImageView cloud;
    private ImageView cloud1;
    private LinearLayout  anim;
    private ImageView sunshine;
    private ImageView bird;
    private ImageView bird1;
    private RelativeLayout drop;
    private ImageView drop1;
    private ImageView drop2;
    private ImageView drop3;
    private ImageView drop4;
    private ImageView drop5;
    private ImageView drop6;
    private ImageView drop7;
    private ImageView drop8;
    private ImageView drop9;
    private ImageView drop10;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        weatherInfoLayout=(LinearLayout)findViewById(R.id.weather_info_layout);
        cityNameText=(TextView)findViewById(R.id.city_name);
        publishText=(TextView)findViewById(R.id.publish_text);
        weatherDespText=(TextView)findViewById(R.id.weather_desp);
        temp1Text=(TextView)findViewById(R.id.temp1);
        currentDateText=(TextView)findViewById(R.id.current_date);
        lyText=(TextView)findViewById(R.id.ly_text);
        clText=(TextView)findViewById(R.id.cl_text);
        ssdText=(TextView)findViewById(R.id.ssd_text);
        imageView=(ImageView)findViewById(R.id.weather_image);
        bg=(ImageView)findViewById(R.id.bg);
        String countyCode=getIntent().getStringExtra("county_code");
            if (!TextUtils.isEmpty(countyCode)) {
                publishText.setText("同步中...");
                bg.setVisibility(View.INVISIBLE);
                weatherInfoLayout.setVisibility(View.INVISIBLE);
                cityNameText.setVisibility(View.INVISIBLE);
                if(countyCode.length()<8) {
                    queryWeatherCode(countyCode);
                }else {
                    queryWeatherInfo(countyCode);
                }
            } else {
                showWeather();
            }
        switchCity=(Button)findViewById(R.id.switch_city);
        refreshWeather=(Button)findViewById(R.id.refresh_weather);
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
        AdView adView=new AdView(this, AdSize.FIT_SCREEN);
        LinearLayout adlayout=(LinearLayout)findViewById(R.id.adLayout);
        adlayout.addView(adView);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.switch_city:
                Intent intent=new Intent(this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity",true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather:
                publishText.setText("正在同步...");
                SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode=preferences.getString("weather_code","");
                if(!TextUtils.isEmpty(weatherCode));{
                queryWeatherInfo(weatherCode);
            }
            break;
            default:
                break;
        }
    }
    private void queryWeatherCode(String countyCode){
        String address="http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
        queryFromServer(address,"countyCode");
    }
    private void queryWeatherInfo(String weatherCode){
        String address="http://m.weather.com.cn/atad/"+weatherCode+".html";
        queryFromServer(address,"weatherCode");
    }
    private void queryFromServer(final String address,final String type){
        HttpUtil.sendHttpRequest(address,new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if("countyCode".equals(type)){
                    if(!TextUtils.isEmpty(response)){
                        String[]array=response.split("\\|");
                        if(array!=null&&array.length==2){
                            String weatherCode=array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                }else if("weatherCode".equals(type)){
                    Utility.handleWeatherResponse(WeatherActivity.this,response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });

            }
        });
    }
    private void showWeather(){
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(prefs.getString("city_name",""));
        temp1Text.setText(prefs.getString("temp1",""));
        weatherDespText.setText(prefs.getString("weather_desp", ""));
        String weatherinfo=prefs.getString("weather_desp","");
         if (weatherinfo.contains("小雨"))
            weatherstate=Xiaoyu;
        else if (weatherinfo.contains("中雨"))
            weatherstate=Zhongyu;
        else if (weatherinfo.contains("大雨"))
            weatherstate=Dayu;
        else if (weatherinfo.contains("暴雨"))
            weatherstate=Baoyu;
        else if (weatherinfo.contains("阵雨"))
            weatherstate=Zhenyu;
        else if (weatherinfo.contains("雷阵雨"))
            weatherstate=Leizhenyu;
        else if (weatherinfo.contains("雨夹雪"))
            weatherstate=Yujiaxue;
        else if (weatherinfo.contains("小雪"))
            weatherstate=Xiaoxue;
        else if (weatherinfo.contains("中雪"))
            weatherstate=Zhongxue;
        else if (weatherinfo.contains("大雪"))
            weatherstate=Daxue;
        else if (weatherinfo.contains("暴雪"))
            weatherstate=Baoxue;
        else if(weatherinfo.contains("晴")) {
             weatherstate = Qing;
         }
        else if (weatherinfo.contains("阴"))
            weatherstate=Yin;
        else if (weatherinfo.contains("多云"))
            weatherstate=Duoyun;
        publishText.setText(prefs.getString("publish_time",""));
        currentDateText.setText(prefs.getString("current_date",""));
        lyText.setText("旅游："+prefs.getString("ly_text","")+"  ");
        clText.setText("晨练："+prefs.getString("cl_text","")+"  ");
        ssdText.setText("舒适度："+prefs.getString("ssd_text","")+"  ");
        switch (weatherstate){
            case Qing:
                imageView.setImageResource(R.drawable.org3_ww0);
                bg.setImageResource(R.drawable.qing);
                cloud=(ImageView)findViewById(R.id.cloud);
                cloud1=(ImageView)findViewById(R.id.cloud1);
                cloud.setImageResource(R.drawable.fine_day_cloud1);
                cloud1.setImageResource(R.drawable.fine_day_cloud2);
                anim=(LinearLayout)findViewById(R.id.anim);
                Animation animation= AnimationUtils.loadAnimation(this,R.anim.translate);
                anim.startAnimation(animation);
                Animation animation1=AnimationUtils.loadAnimation(this,R.anim.rotate);
                sunshine=(ImageView)findViewById(R.id.sunshine);
                sunshine.setImageResource(R.drawable.sunshine);
                sunshine.startAnimation(animation1);
                break;
            case Yin:
                imageView.setImageResource(R.drawable.org3_ww2);
                bg.setImageResource(R.drawable.yin);
                bird=(ImageView)findViewById(R.id.bird);
                bird.setImageResource(R.drawable.list);
                AnimationDrawable animationDrawable=(AnimationDrawable)bird.getDrawable();
                animationDrawable.start();
                Animation animation2=AnimationUtils.loadAnimation(this,R.anim.birdfly);
                bird.startAnimation(animation2);
                bird1=(ImageView)findViewById(R.id.bird1);
                bird1.setImageResource(R.drawable.list);
                AnimationDrawable animationDrawable1=(AnimationDrawable)bird.getDrawable();
                animationDrawable1.start();
                Animation birdfly1=AnimationUtils.loadAnimation(this,R.anim.birdfly1);
                bird1.startAnimation(birdfly1);
                break;
            case Duoyun:
                imageView.setImageResource(R.drawable.org3_ww1);
                bg.setImageResource(R.drawable.yin);
                bird=(ImageView)findViewById(R.id.bird);
                bird.setImageResource(R.drawable.list);
                AnimationDrawable animationDrawable2=(AnimationDrawable)bird.getDrawable();
                animationDrawable2.start();
                Animation animation3=AnimationUtils.loadAnimation(this,R.anim.birdfly);
                bird.startAnimation(animation3);
                bird1=(ImageView)findViewById(R.id.bird1);
                bird1.setImageResource(R.drawable.list);
                AnimationDrawable animationDrawable3=(AnimationDrawable)bird.getDrawable();
                animationDrawable3.start();
                Animation birdfly2=AnimationUtils.loadAnimation(this,R.anim.birdfly1);
                bird1.startAnimation(birdfly2);
                bird1.startAnimation(birdfly2);
                break;
            case Xiaoyu:
                imageView.setImageResource(R.drawable.org3_ww7);
                bg.setImageResource(R.drawable.yu);
                drop=(RelativeLayout)findViewById(R.id.drop);
                drop1=(ImageView)findViewById(R.id.drop1);
                drop2=(ImageView)findViewById(R.id.drop2);
                drop3=(ImageView)findViewById(R.id.drop3);
                drop4=(ImageView)findViewById(R.id.drop4);
                drop5=(ImageView)findViewById(R.id.drop5);
                drop6=(ImageView)findViewById(R.id.drop6);
                drop7=(ImageView)findViewById(R.id.drop7);
                drop8=(ImageView)findViewById(R.id.drop8);
                drop9=(ImageView)findViewById(R.id.drop9);
                drop10=(ImageView)findViewById(R.id.drop10);
                drop1.setImageResource(R.drawable.raindrop_xl);
                drop2.setImageResource(R.drawable.raindrop_m);
                drop3.setImageResource(R.drawable.raindrop_s);
                drop4.setImageResource(R.drawable.raindrop_l);
                drop5.setImageResource(R.drawable.raindrop_xl);
                drop6.setImageResource(R.drawable.raindrop_m);
                drop7.setImageResource(R.drawable.raindrop_s);
                drop8.setImageResource(R.drawable.raindrop_l);
                drop9.setImageResource(R.drawable.raindrop_xl);
                drop10.setImageResource(R.drawable.raindrop_xl);
                Animation dropanim=AnimationUtils.loadAnimation(this,R.anim.drop);
                drop.startAnimation(dropanim);
                break;
            case Zhongyu:
                imageView.setImageResource(R.drawable.org3_ww8);
                bg.setImageResource(R.drawable.yu);
                drop=(RelativeLayout)findViewById(R.id.drop);
                drop1=(ImageView)findViewById(R.id.drop1);
                drop2=(ImageView)findViewById(R.id.drop2);
                drop3=(ImageView)findViewById(R.id.drop3);
                drop4=(ImageView)findViewById(R.id.drop4);
                drop5=(ImageView)findViewById(R.id.drop5);
                drop6=(ImageView)findViewById(R.id.drop6);
                drop7=(ImageView)findViewById(R.id.drop7);
                drop8=(ImageView)findViewById(R.id.drop8);
                drop9=(ImageView)findViewById(R.id.drop9);
                drop10=(ImageView)findViewById(R.id.drop10);
                drop1.setImageResource(R.drawable.raindrop_xl);
                drop2.setImageResource(R.drawable.raindrop_m);
                drop3.setImageResource(R.drawable.raindrop_s);
                drop4.setImageResource(R.drawable.raindrop_l);
                drop5.setImageResource(R.drawable.raindrop_xl);
                drop6.setImageResource(R.drawable.raindrop_m);
                drop7.setImageResource(R.drawable.raindrop_s);
                drop8.setImageResource(R.drawable.raindrop_l);
                drop9.setImageResource(R.drawable.raindrop_xl);
                drop10.setImageResource(R.drawable.raindrop_xl);
                Animation dropanim1=AnimationUtils.loadAnimation(this,R.anim.drop);
                drop.startAnimation(dropanim1);
                break;
            case Dayu:
                imageView.setImageResource(R.drawable.org3_ww19);
                bg.setImageResource(R.drawable.yu);
                drop=(RelativeLayout)findViewById(R.id.drop);
                drop1=(ImageView)findViewById(R.id.drop1);
                drop2=(ImageView)findViewById(R.id.drop2);
                drop3=(ImageView)findViewById(R.id.drop3);
                drop4=(ImageView)findViewById(R.id.drop4);
                drop5=(ImageView)findViewById(R.id.drop5);
                drop6=(ImageView)findViewById(R.id.drop6);
                drop7=(ImageView)findViewById(R.id.drop7);
                drop8=(ImageView)findViewById(R.id.drop8);
                drop9=(ImageView)findViewById(R.id.drop9);
                drop10=(ImageView)findViewById(R.id.drop10);
                drop1.setImageResource(R.drawable.raindrop_xl);
                drop2.setImageResource(R.drawable.raindrop_m);
                drop3.setImageResource(R.drawable.raindrop_s);
                drop4.setImageResource(R.drawable.raindrop_l);
                drop5.setImageResource(R.drawable.raindrop_xl);
                drop6.setImageResource(R.drawable.raindrop_m);
                drop7.setImageResource(R.drawable.raindrop_s);
                drop8.setImageResource(R.drawable.raindrop_l);
                drop9.setImageResource(R.drawable.raindrop_xl);
                drop10.setImageResource(R.drawable.raindrop_xl);
                Animation dropanim2=AnimationUtils.loadAnimation(this,R.anim.drop);
                drop.startAnimation(dropanim2);
                break;
            case Baoyu:
                imageView.setImageResource(R.drawable.org3_ww10);
                bg.setImageResource(R.drawable.yu);
                drop=(RelativeLayout)findViewById(R.id.drop);
                drop1=(ImageView)findViewById(R.id.drop1);
                drop2=(ImageView)findViewById(R.id.drop2);
                drop3=(ImageView)findViewById(R.id.drop3);
                drop4=(ImageView)findViewById(R.id.drop4);
                drop5=(ImageView)findViewById(R.id.drop5);
                drop6=(ImageView)findViewById(R.id.drop6);
                drop7=(ImageView)findViewById(R.id.drop7);
                drop8=(ImageView)findViewById(R.id.drop8);
                drop9=(ImageView)findViewById(R.id.drop9);
                drop10=(ImageView)findViewById(R.id.drop10);
                drop1.setImageResource(R.drawable.raindrop_xl);
                drop2.setImageResource(R.drawable.raindrop_m);
                drop3.setImageResource(R.drawable.raindrop_s);
                drop4.setImageResource(R.drawable.raindrop_l);
                drop5.setImageResource(R.drawable.raindrop_xl);
                drop6.setImageResource(R.drawable.raindrop_m);
                drop7.setImageResource(R.drawable.raindrop_s);
                drop8.setImageResource(R.drawable.raindrop_l);
                drop9.setImageResource(R.drawable.raindrop_xl);
                drop10.setImageResource(R.drawable.raindrop_xl);
                Animation dropanim3=AnimationUtils.loadAnimation(this,R.anim.drop);
                drop.startAnimation(dropanim3);
                break;
            case Zhenyu:
                imageView.setImageResource(R.drawable.org3_ww3);
                bg.setImageResource(R.drawable.yu);
                drop=(RelativeLayout)findViewById(R.id.drop);
                drop1=(ImageView)findViewById(R.id.drop1);
                drop2=(ImageView)findViewById(R.id.drop2);
                drop3=(ImageView)findViewById(R.id.drop3);
                drop4=(ImageView)findViewById(R.id.drop4);
                drop5=(ImageView)findViewById(R.id.drop5);
                drop6=(ImageView)findViewById(R.id.drop6);
                drop7=(ImageView)findViewById(R.id.drop7);
                drop8=(ImageView)findViewById(R.id.drop8);
                drop9=(ImageView)findViewById(R.id.drop9);
                drop10=(ImageView)findViewById(R.id.drop10);
                drop1.setImageResource(R.drawable.snowflake_xl);
                drop2.setImageResource(R.drawable.snowflake_m);
                drop3.setImageResource(R.drawable.snowflake_l);
                drop4.setImageResource(R.drawable.snowflake_xxl);
                drop5.setImageResource(R.drawable.snowflake_l);
                drop6.setImageResource(R.drawable.snowflake_m);
                drop7.setImageResource(R.drawable.snowflake_xl);
                drop8.setImageResource(R.drawable.snowflake_xxl);
                drop9.setImageResource(R.drawable.snowflake_l);
                drop10.setImageResource(R.drawable.snowflake_xxl);
                Animation dropanim4=AnimationUtils.loadAnimation(this,R.anim.drop);
                drop.startAnimation(dropanim4);
                break;
            case Leizhenyu:
                imageView.setImageResource(R.drawable.org3_ww4);
                bg.setImageResource(R.drawable.yu);
                drop=(RelativeLayout)findViewById(R.id.drop);
                drop1=(ImageView)findViewById(R.id.drop1);
                drop2=(ImageView)findViewById(R.id.drop2);
                drop3=(ImageView)findViewById(R.id.drop3);
                drop4=(ImageView)findViewById(R.id.drop4);
                drop5=(ImageView)findViewById(R.id.drop5);
                drop6=(ImageView)findViewById(R.id.drop6);
                drop7=(ImageView)findViewById(R.id.drop7);
                drop8=(ImageView)findViewById(R.id.drop8);
                drop9=(ImageView)findViewById(R.id.drop9);
                drop10=(ImageView)findViewById(R.id.drop10);
                drop1.setImageResource(R.drawable.snowflake_xl);
                drop2.setImageResource(R.drawable.snowflake_m);
                drop3.setImageResource(R.drawable.snowflake_l);
                drop4.setImageResource(R.drawable.snowflake_xxl);
                drop5.setImageResource(R.drawable.snowflake_l);
                drop6.setImageResource(R.drawable.snowflake_m);
                drop7.setImageResource(R.drawable.snowflake_xl);
                drop8.setImageResource(R.drawable.snowflake_xxl);
                drop9.setImageResource(R.drawable.snowflake_l);
                drop10.setImageResource(R.drawable.snowflake_xxl);
                Animation dropanim5=AnimationUtils.loadAnimation(this,R.anim.drop);
                drop.startAnimation(dropanim5);
                break;
            case Yujiaxue:
                imageView.setImageResource(R.drawable.org3_ww6);
                bg.setImageResource(R.drawable.xue);
                drop=(RelativeLayout)findViewById(R.id.drop);
                drop1=(ImageView)findViewById(R.id.drop1);
                drop2=(ImageView)findViewById(R.id.drop2);
                drop3=(ImageView)findViewById(R.id.drop3);
                drop4=(ImageView)findViewById(R.id.drop4);
                drop5=(ImageView)findViewById(R.id.drop5);
                drop6=(ImageView)findViewById(R.id.drop6);
                drop7=(ImageView)findViewById(R.id.drop7);
                drop8=(ImageView)findViewById(R.id.drop8);
                drop9=(ImageView)findViewById(R.id.drop9);
                drop10=(ImageView)findViewById(R.id.drop10);
                drop1.setImageResource(R.drawable.snowflake_xl);
                drop2.setImageResource(R.drawable.snowflake_m);
                drop3.setImageResource(R.drawable.snowflake_l);
                drop4.setImageResource(R.drawable.snowflake_xxl);
                drop5.setImageResource(R.drawable.snowflake_l);
                drop6.setImageResource(R.drawable.snowflake_m);
                drop7.setImageResource(R.drawable.snowflake_xl);
                drop8.setImageResource(R.drawable.snowflake_xxl);
                drop9.setImageResource(R.drawable.snowflake_l);
                drop10.setImageResource(R.drawable.snowflake_xxl);
                Animation dropanim6=AnimationUtils.loadAnimation(this,R.anim.drop);
                drop.startAnimation(dropanim6);
                break;
            case Xiaoxue:
                imageView.setImageResource(R.drawable.org3_ww14);
                bg.setImageResource(R.drawable.xue);
                drop=(RelativeLayout)findViewById(R.id.drop);
                drop1=(ImageView)findViewById(R.id.drop1);
                drop2=(ImageView)findViewById(R.id.drop2);
                drop3=(ImageView)findViewById(R.id.drop3);
                drop4=(ImageView)findViewById(R.id.drop4);
                drop5=(ImageView)findViewById(R.id.drop5);
                drop6=(ImageView)findViewById(R.id.drop6);
                drop7=(ImageView)findViewById(R.id.drop7);
                drop8=(ImageView)findViewById(R.id.drop8);
                drop9=(ImageView)findViewById(R.id.drop9);
                drop10=(ImageView)findViewById(R.id.drop10);
                drop1.setImageResource(R.drawable.snowflake_xl);
                drop2.setImageResource(R.drawable.snowflake_m);
                drop3.setImageResource(R.drawable.snowflake_l);
                drop4.setImageResource(R.drawable.snowflake_xxl);
                drop5.setImageResource(R.drawable.snowflake_l);
                drop6.setImageResource(R.drawable.snowflake_m);
                drop7.setImageResource(R.drawable.snowflake_xl);
                drop8.setImageResource(R.drawable.snowflake_xxl);
                drop9.setImageResource(R.drawable.snowflake_l);
                drop10.setImageResource(R.drawable.snowflake_xxl);
                Animation dropanim7=AnimationUtils.loadAnimation(this,R.anim.drop);
                drop.startAnimation(dropanim7);
                break;
            case Zhongxue:
                imageView.setImageResource(R.drawable.org3_ww15);
                bg.setImageResource(R.drawable.xue);
                drop=(RelativeLayout)findViewById(R.id.drop);
                drop1=(ImageView)findViewById(R.id.drop1);
                drop2=(ImageView)findViewById(R.id.drop2);
                drop3=(ImageView)findViewById(R.id.drop3);
                drop4=(ImageView)findViewById(R.id.drop4);
                drop5=(ImageView)findViewById(R.id.drop5);
                drop6=(ImageView)findViewById(R.id.drop6);
                drop7=(ImageView)findViewById(R.id.drop7);
                drop8=(ImageView)findViewById(R.id.drop8);
                drop9=(ImageView)findViewById(R.id.drop9);
                drop10=(ImageView)findViewById(R.id.drop10);
                drop1.setImageResource(R.drawable.snowflake_xl);
                drop2.setImageResource(R.drawable.snowflake_m);
                drop3.setImageResource(R.drawable.snowflake_l);
                drop4.setImageResource(R.drawable.snowflake_xxl);
                drop5.setImageResource(R.drawable.snowflake_l);
                drop6.setImageResource(R.drawable.snowflake_m);
                drop7.setImageResource(R.drawable.snowflake_xl);
                drop8.setImageResource(R.drawable.snowflake_xxl);
                drop9.setImageResource(R.drawable.snowflake_l);
                drop10.setImageResource(R.drawable.snowflake_xxl);
                Animation dropanim8=AnimationUtils.loadAnimation(this,R.anim.drop);
                drop.startAnimation(dropanim8);
                break;
            case Daxue:
                imageView.setImageResource(R.drawable.org3_ww16);
                bg.setImageResource(R.drawable.xue);
                drop=(RelativeLayout)findViewById(R.id.drop);
                drop1=(ImageView)findViewById(R.id.drop1);
                drop2=(ImageView)findViewById(R.id.drop2);
                drop3=(ImageView)findViewById(R.id.drop3);
                drop4=(ImageView)findViewById(R.id.drop4);
                drop5=(ImageView)findViewById(R.id.drop5);
                drop6=(ImageView)findViewById(R.id.drop6);
                drop7=(ImageView)findViewById(R.id.drop7);
                drop8=(ImageView)findViewById(R.id.drop8);
                drop9=(ImageView)findViewById(R.id.drop9);
                drop10=(ImageView)findViewById(R.id.drop10);
                drop1.setImageResource(R.drawable.snowflake_xl);
                drop2.setImageResource(R.drawable.snowflake_m);
                drop3.setImageResource(R.drawable.snowflake_l);
                drop4.setImageResource(R.drawable.snowflake_xxl);
                drop5.setImageResource(R.drawable.snowflake_l);
                drop6.setImageResource(R.drawable.snowflake_m);
                drop7.setImageResource(R.drawable.snowflake_xl);
                drop8.setImageResource(R.drawable.snowflake_xxl);
                drop9.setImageResource(R.drawable.snowflake_l);
                drop10.setImageResource(R.drawable.snowflake_xxl);
                Animation dropanim9=AnimationUtils.loadAnimation(this,R.anim.drop);
                drop.startAnimation(dropanim9);
                break;
            case Baoxue:
                imageView.setImageResource(R.drawable.org3_ww17);
                bg.setImageResource(R.drawable.xue);
                drop=(RelativeLayout)findViewById(R.id.drop);
                drop1=(ImageView)findViewById(R.id.drop1);
                drop2=(ImageView)findViewById(R.id.drop2);
                drop3=(ImageView)findViewById(R.id.drop3);
                drop4=(ImageView)findViewById(R.id.drop4);
                drop5=(ImageView)findViewById(R.id.drop5);
                drop6=(ImageView)findViewById(R.id.drop6);
                drop7=(ImageView)findViewById(R.id.drop7);
                drop8=(ImageView)findViewById(R.id.drop8);
                drop9=(ImageView)findViewById(R.id.drop9);
                drop10=(ImageView)findViewById(R.id.drop10);
                drop1.setImageResource(R.drawable.snowflake_xl);
                drop2.setImageResource(R.drawable.snowflake_m);
                drop3.setImageResource(R.drawable.snowflake_l);
                drop4.setImageResource(R.drawable.snowflake_xxl);
                drop5.setImageResource(R.drawable.snowflake_l);
                drop6.setImageResource(R.drawable.snowflake_m);
                drop7.setImageResource(R.drawable.snowflake_xl);
                drop8.setImageResource(R.drawable.snowflake_xxl);
                drop9.setImageResource(R.drawable.snowflake_l);
                drop10.setImageResource(R.drawable.snowflake_xxl);
                Animation dropanim10=AnimationUtils.loadAnimation(this,R.anim.drop);
                drop.startAnimation(dropanim10);
                break;
        }
        bg.setVisibility(View.VISIBLE);
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
        Intent intent=new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

}
