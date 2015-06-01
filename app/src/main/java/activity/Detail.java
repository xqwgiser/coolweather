package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xqw.coolweather.R;

import model.Weatherinfo;

/**
 * Created by xqw on 2015/5/26.
 */
public class Detail extends Activity implements View.OnClickListener{
    private LinearLayout weatherdetaillayout;
    private LinearLayout bg;
    private TextView city_name;
    private ImageView weatherimg;
    private TextView detailtemp;
    private TextView detailweather;
    private TextView clText;
    private TextView lyText;
    private TextView ssdText;
    private TextView cy;
    private Button back;
    private Button refresh;
    private Weatherinfo weatherinfo;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detail);
        weatherdetaillayout=(LinearLayout)findViewById(R.id.weather_detail_layout);
        bg=(LinearLayout)findViewById(R.id.bg);
        city_name=(TextView)findViewById(R.id.city_name);
        weatherimg=(ImageView)findViewById(R.id.weatherimage);
        detailtemp=(TextView)findViewById(R.id.detail_temp);
        detailweather=(TextView)findViewById(R.id.detail_weather);
        clText=(TextView)findViewById(R.id.cl);
        lyText=(TextView)findViewById(R.id.ly);
        ssdText=(TextView)findViewById(R.id.ssd);
        cy=(TextView)findViewById(R.id.cy);
        back=(Button)findViewById(R.id.back);
        refresh=(Button)findViewById(R.id.refresh);
        back.setOnClickListener(this);
        refresh.setOnClickListener(this);
        weatherinfo=(Weatherinfo)getIntent().getParcelableExtra("weather_data");
        if(weatherinfo==null){
            weatherinfo=(Weatherinfo)getIntent().getParcelableExtra("tomarrow_data");
        }
        city_name.setText(weatherinfo.getCityname());
        detailtemp.setText(weatherinfo.getTemp());
        detailweather.setText(weatherinfo.getWeather());
        clText.setText(weatherinfo.getCl());
        lyText.setText(weatherinfo.getLy());
        ssdText.setText(weatherinfo.getSsd());
        cy.setText(weatherinfo.getCy());
        String weather_info=weatherinfo.getWeather();
        if (weather_info.contains("小雨"))
            weatherstate=Xiaoyu;
        else if (weather_info.contains("中雨"))
            weatherstate=Zhongyu;
        else if (weather_info.contains("大雨"))
            weatherstate=Dayu;
        else if (weather_info.contains("暴雨"))
            weatherstate=Baoyu;
        else if (weather_info.contains("阵雨"))
            weatherstate=Zhenyu;
        else if (weather_info.contains("雷阵雨"))
            weatherstate=Leizhenyu;
        else if (weather_info.contains("雨夹雪"))
            weatherstate=Yujiaxue;
        else if (weather_info.contains("小雪"))
            weatherstate=Xiaoxue;
        else if (weather_info.contains("中雪"))
            weatherstate=Zhongxue;
        else if (weather_info.contains("大雪"))
            weatherstate=Daxue;
        else if (weather_info.contains("暴雪"))
            weatherstate=Baoxue;
        else if(weather_info.contains("晴")) {
            weatherstate = Qing;
        }
        else if (weather_info.contains("阴"))
            weatherstate=Yin;
        else if (weather_info.contains("多云"))
            weatherstate=Duoyun;
        switch (weatherstate){
            case Qing:
                weatherimg.setImageResource(R.drawable.org3_ww0);
                bg.setBackgroundResource(R.drawable.qing1);
                break;
            case Yin:
                weatherimg.setImageResource(R.drawable.org3_ww2);
                bg.setBackgroundResource(R.drawable.yin1);
                break;
            case Duoyun:
                weatherimg.setImageResource(R.drawable.org3_ww1);
                bg.setBackgroundResource(R.drawable.yin1);
                break;
            case Xiaoyu:
                weatherimg.setImageResource(R.drawable.org3_ww7);
                bg.setBackgroundResource(R.drawable.yu1);
                break;
            case Zhongyu:
                weatherimg.setImageResource(R.drawable.org3_ww8);
                bg.setBackgroundResource(R.drawable.yu1);
                break;
            case Dayu:
                weatherimg.setImageResource(R.drawable.org3_ww19);
                bg.setBackgroundResource(R.drawable.yu1);
                break;
            case Baoyu:
                weatherimg.setImageResource(R.drawable.org3_ww10);
                bg.setBackgroundResource(R.drawable.yu1);
                break;
            case Zhenyu:
                weatherimg.setImageResource(R.drawable.org3_ww3);
                bg.setBackgroundResource(R.drawable.yu1);
                break;
            case Leizhenyu:
                weatherimg.setImageResource(R.drawable.org3_ww4);
                bg.setBackgroundResource(R.drawable.yu1);
                break;
            case Yujiaxue:
                weatherimg.setImageResource(R.drawable.org3_ww6);
                bg.setBackgroundResource(R.drawable.xue1);
                break;
            case Xiaoxue:
                weatherimg.setImageResource(R.drawable.org3_ww14);
                bg.setBackgroundResource(R.drawable.xue1);
                break;
            case Zhongxue:
                weatherimg.setImageResource(R.drawable.org3_ww15);
                bg.setBackgroundResource(R.drawable.xue1);
                break;
            case Daxue:
                weatherimg.setImageResource(R.drawable.org3_ww16);
                bg.setBackgroundResource(R.drawable.xue1);
                break;
            case Baoxue:
                weatherimg.setImageResource(R.drawable.org3_ww17);
                bg.setBackgroundResource(R.drawable.xue1);
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent intent=new Intent(this,WeatherActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    @Override
    public void onBackPressed(){
        Intent intent=new Intent(this,WeatherActivity.class);
        startActivity(intent);
        finish();
    }
}
