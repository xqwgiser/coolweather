package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xqw.coolweather.R;

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
    private TextView temp2Text;
    private TextView currentDateText;
    private TextView clText;
    private TextView lyText;
    private TextView ssdText;
    private Button switchCity;
    private Button refreshWeather;
    private ImageView imageView;
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
        temp2Text=(TextView)findViewById(R.id.temp2);
        currentDateText=(TextView)findViewById(R.id.current_date);
        lyText=(TextView)findViewById(R.id.ly_text);
        clText=(TextView)findViewById(R.id.cl_text);
        ssdText=(TextView)findViewById(R.id.ssd_text);
        imageView=(ImageView)findViewById(R.id.weather_image);
        String countyCode=getIntent().getStringExtra("county_code");
        if(!TextUtils.isEmpty(countyCode)){
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        }else {
            showWeather();
        }
        switchCity=(Button)findViewById(R.id.switch_city);
        refreshWeather=(Button)findViewById(R.id.refresh_weather);
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
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
        temp2Text.setText(prefs.getString("temp2",""));
        weatherDespText.setText(prefs.getString("weather_desp",""));
        String weatherinfo=prefs.getString("weather_desp","");
        if(weatherinfo.contains("晴"))
            weatherstate=Qing;
        else if (weatherinfo.contains("阴"))
            weatherstate=Yin;
        else if (weatherinfo.contains("多云"))
            weatherstate=Duoyun;
        else if (weatherinfo.contains("小雨"))
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
        publishText.setText(prefs.getString("publish_time",""));
        currentDateText.setText(prefs.getString("current_date",""));
        lyText.setText("旅游："+prefs.getString("ly_text","")+"  ");
        clText.setText("晨练："+prefs.getString("cl_text","")+"  ");
        ssdText.setText("舒适度："+prefs.getString("ssd_text","")+"  ");
        switch (weatherstate){
            case Qing:
                imageView.setImageResource(R.drawable.org3_ww0);
                break;
            case Yin:
                imageView.setImageResource(R.drawable.org3_ww2);
                break;
            case Duoyun:
                imageView.setImageResource(R.drawable.org3_ww1);
                break;
            case Xiaoyu:
                imageView.setImageResource(R.drawable.org3_ww7);
                break;
            case Zhongyu:
                imageView.setImageResource(R.drawable.org3_ww8);
                break;
            case Dayu:
                imageView.setImageResource(R.drawable.org3_ww19);
                break;
            case Baoyu:
                imageView.setImageResource(R.drawable.org3_ww10);
                break;
            case Zhenyu:
                imageView.setImageResource(R.drawable.org3_ww3);
                break;
            case Leizhenyu:
                imageView.setImageResource(R.drawable.org3_ww4);
                break;
            case Yujiaxue:
                imageView.setImageResource(R.drawable.org3_ww6);
                break;
            case Xiaoxue:
                imageView.setImageResource(R.drawable.org3_ww14);
                break;
            case Zhongxue:
                imageView.setImageResource(R.drawable.org3_ww15);
                break;
            case Daxue:
                imageView.setImageResource(R.drawable.org3_ww16);
                break;
            case Baoxue:
                imageView.setImageResource(R.drawable.org3_ww17);
                break;
        }
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
        Intent intent=new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
}
