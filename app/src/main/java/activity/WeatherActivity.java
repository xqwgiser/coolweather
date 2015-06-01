package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.xqw.coolweather.R;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import model.MenuAdapter;
import model.Menuitem;
import model.Weatherinfo;
import service.AutoUpdateService;
import util.HttpUtil;
import util.Utility;

/**
 * Created by xqw on 2015/5/5.
 */
public class WeatherActivity extends ActionBarActivity implements View.OnClickListener {
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
    private TextView publishText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView currentDateText;
    private TextView cityNameText;
//    private TextView clText;
//    private TextView lyText;
//    private TextView ssdText;
   // private Button switchCity;
    //private Button refreshWeather;
   // private ImageView imageView;
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
    private TextView todaytemp;
    private TextView todayweath;
    private ImageView todayimag;
    private TextView tomarrowtemp;
    private TextView tomarrowweath;
    private ImageView tomarrowimag;
    private RelativeLayout today;
    private RelativeLayout tomarrow;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<Menuitem>menuItems=new ArrayList<Menuitem>();
    private ListView menulist;
    private Weatherinfo weatherInfo;
    private Weatherinfo tomarrowinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        menulist=(ListView)findViewById(R.id.menu_item);
        initViews();
        menulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Menuitem menuitem=menuItems.get(position);
                String menuname=menuitem.getContext();
                switch (menuname){
                    case"切换":
                        Intent intent=new Intent(WeatherActivity.this,ChooseAreaActivity.class);
                        intent.putExtra("from_weather_activity",true);
                        startActivity(intent);
                        finish();
                        break;
                    case "设置":
                        break;
                    case "关于":
                        break;
                    case "退出":
                        finish();
                        break;
                }
            }
        });
        weatherInfoLayout=(LinearLayout)findViewById(R.id.weather_info_layout);
        cityNameText=(TextView)findViewById(R.id.cityname);
        publishText=(TextView)findViewById(R.id.publish_text);
        weatherDespText=(TextView)findViewById(R.id.weather_desp);
        todayweath=(TextView)findViewById(R.id.todayweath);
        temp1Text=(TextView)findViewById(R.id.temp1);
        todaytemp=(TextView)findViewById(R.id.todaytemp);
        tomarrowtemp=(TextView)findViewById(R.id.tomarrowtemp);
        tomarrowweath=(TextView)findViewById(R.id.tomarrowweath);
        currentDateText=(TextView)findViewById(R.id.current_date);
//        lyText=(TextView)findViewById(R.id.ly_text);
//        clText=(TextView)findViewById(R.id.cl_text);
//        ssdText=(TextView)findViewById(R.id.ssd_text);
        //imageView=(ImageView)findViewById(R.id.weather_image);
        todayimag=(ImageView)findViewById(R.id.todayimag);
        tomarrowimag=(ImageView)findViewById(R.id.tomarrowimg);
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
       // switchCity=(Button)findViewById(R.id.switch_city);
        //refreshWeather=(Button)findViewById(R.id.refresh_weather);
       // switchCity.setOnClickListener(this);
        //refreshWeather.setOnClickListener(this);
        today=(RelativeLayout)findViewById(R.id.today);
        tomarrow=(RelativeLayout)findViewById(R.id.tomarrow);
        today.setOnClickListener(this);
        tomarrow.setOnClickListener(this);
        AdView adView=new AdView(this, AdSize.FIT_SCREEN);
        LinearLayout adlayout=(LinearLayout)findViewById(R.id.adLayout);
        adlayout.addView(adView);
    }
    private void initMenu(){
        Menuitem swith=new Menuitem("切换",R.drawable.ic_swap_horiz_black_36dp);
        Menuitem set=new Menuitem("设置",R.drawable.ic_settings_black_36dp);
        Menuitem info=new Menuitem("关于",R.drawable.ic_info_outline_black_36dp);
        Menuitem quilt=new Menuitem("退出",R.drawable.ic_exit_to_app_black_36dp);
        menuItems.add(swith);
        menuItems.add(set);
        menuItems.add(info);
        menuItems.add(quilt);
    }
    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setLogo(R.drawable.ic_launcher);
        mToolbar.setTitle("首页");// 标题的文字需在setSupportActionBar之前，不然会无效
        // toolbar.setSubtitle("副标题");
        setSupportActionBar(mToolbar);
		/* 这些通过ActionBar来设置也是一样的，注意要在setSupportActionBar(toolbar);之后，不然就报错了 */
        // getSupportActionBar().setTitle("标题");
        // getSupportActionBar().setSubtitle("副标题");
        // getSupportActionBar().setLogo(R.drawable.ic_launcher);

		/* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过下面的两个回调方法来处理 */
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        //Toast.makeText(Detail.this, "action_settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_share:
                        //Toast.makeText(Detail.this, "action_share", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        initMenu();
        MenuAdapter adapter=new MenuAdapter(WeatherActivity.this,R.layout.menu,menuItems);
        menulist.setAdapter(adapter);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
//            case R.id.switch_city:
//                Intent intent=new Intent(this,ChooseAreaActivity.class);
//                intent.putExtra("from_weather_activity",true);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.refresh_weather:
//                publishText.setText("正在同步...");
//                SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(this);
//                String weatherCode=preferences.getString("weather_code","");
//                if(!TextUtils.isEmpty(weatherCode));{
//                queryWeatherInfo(weatherCode);
//            }
//            break;
            case R.id.today:
                Intent intent1=new Intent(this,Detail.class);
                intent1.putExtra("weather_data",weatherInfo);
                startActivity(intent1);
                finish();
                break;
            case R.id.tomarrow:
                Intent intent2=new Intent(this,Detail.class);
                intent2.putExtra("tomarrow_data",tomarrowinfo);
                startActivity(intent2);
                finish();
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
        todaytemp.setText(prefs.getString("temp1",""));
        tomarrowtemp.setText(prefs.getString("temp2",""));
        weatherDespText.setText(prefs.getString("weather_desp", ""));
        tomarrowweath.setText(prefs.getString("weather2",""));
        todayweath.setText(prefs.getString("weather_desp",""));
        String weatherinfo=prefs.getString("weather_desp","");
        weatherInfo=new Weatherinfo();
        weatherInfo.setCityname(prefs.getString("city_name",""));
        weatherInfo.setTemp(prefs.getString("temp1",""));
        weatherInfo.setWeather(prefs.getString("weather_desp", ""));
        weatherInfo.setLy("旅游："+prefs.getString("ly_text","")+"  ");
        weatherInfo.setCl("晨练："+prefs.getString("cl_text","")+"  ");
        weatherInfo.setSsd("舒适度："+prefs.getString("ssd_text","")+"  ");
        weatherInfo.setCy(prefs.getString("cloth",""));
        tomarrowinfo=new Weatherinfo();
        tomarrowinfo.setCityname(prefs.getString("city_name",""));
        tomarrowinfo.setTemp(prefs.getString("temp2",""));
        tomarrowinfo.setWeather(prefs.getString("weather2", ""));
        tomarrowinfo.setLy("旅游："+"无信息"+"  ");
        tomarrowinfo.setCl("晨练："+"无信息"+"  ");
        tomarrowinfo.setSsd("舒适度："+"无信息"+"  ");
        tomarrowinfo.setCy("无指导信息");
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
        publishText.setText("今日"+prefs.getString("publish_time","")+":00发布");
        currentDateText.setText(prefs.getString("current_date",""));
//        lyText.setText("旅游："+prefs.getString("ly_text","")+"  ");
//        clText.setText("晨练："+prefs.getString("cl_text","")+"  ");
//        ssdText.setText("舒适度："+prefs.getString("ssd_text","")+"  ");
        switch (weatherstate){
            case Qing:
                todayimag.setImageResource(R.drawable.org3_ww0);
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
                todayimag.setImageResource(R.drawable.org3_ww2);
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
                todayimag.setImageResource(R.drawable.org3_ww1);
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
                todayimag.setImageResource(R.drawable.org3_ww7);
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
                todayimag.setImageResource(R.drawable.org3_ww8);
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
                todayimag.setImageResource(R.drawable.org3_ww19);
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
                todayimag.setImageResource(R.drawable.org3_ww10);
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
                todayimag.setImageResource(R.drawable.org3_ww3);
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
                Animation dropanim4=AnimationUtils.loadAnimation(this,R.anim.drop);
                drop.startAnimation(dropanim4);
                break;
            case Leizhenyu:
                todayimag.setImageResource(R.drawable.org3_ww4);
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
                Animation dropanim5=AnimationUtils.loadAnimation(this,R.anim.drop);
                drop.startAnimation(dropanim5);
                break;
            case Yujiaxue:
                todayimag.setImageResource(R.drawable.org3_ww6);
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
                todayimag.setImageResource(R.drawable.org3_ww14);
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
                todayimag.setImageResource(R.drawable.org3_ww15);
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
                todayimag.setImageResource(R.drawable.org3_ww16);
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
                todayimag.setImageResource(R.drawable.org3_ww17);
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
        String tomarowwether=prefs.getString("weather2","");
        int weatherstate1=0;
            if (weatherinfo.contains("小雨"))
                weatherstate1=Xiaoyu;
            else if (tomarowwether.contains("中雨"))
                weatherstate1=Zhongyu;
            else if (weatherinfo.contains("大雨"))
                weatherstate1=Dayu;
            else if (tomarowwether.contains("暴雨"))
                weatherstate1=Baoyu;
            else if (tomarowwether.contains("阵雨"))
                weatherstate1=Zhenyu;
            else if (tomarowwether.contains("雷阵雨"))
                weatherstate1=Leizhenyu;
            else if (tomarowwether.contains("雨夹雪"))
                weatherstate1=Yujiaxue;
            else if (tomarowwether.contains("小雪"))
                weatherstate1=Xiaoxue;
            else if (tomarowwether.contains("中雪"))
                weatherstate1=Zhongxue;
            else if (tomarowwether.contains("大雪"))
                weatherstate1=Daxue;
            else if (tomarowwether.contains("暴雪"))
                weatherstate1=Baoxue;
            else if(tomarowwether.contains("晴")) {
                weatherstate1 = Qing;
            }
            else if (tomarowwether.contains("阴"))
                weatherstate1=Yin;
            else if (tomarowwether.contains("多云"))
                weatherstate1=Duoyun;
        switch (weatherstate1){
            case Qing:
                tomarrowimag.setImageResource(R.drawable.org3_ww0);
                break;
            case Yin:
                tomarrowimag.setImageResource(R.drawable.org3_ww2);

                break;
            case Duoyun:
                tomarrowimag.setImageResource(R.drawable.org3_ww1);

                break;
            case Xiaoyu:
                tomarrowimag.setImageResource(R.drawable.org3_ww7);

                break;
            case Zhongyu:
                tomarrowimag.setImageResource(R.drawable.org3_ww8);

                break;
            case Dayu:
                tomarrowimag.setImageResource(R.drawable.org3_ww19);

                break;
            case Baoyu:
                tomarrowimag.setImageResource(R.drawable.org3_ww10);

                break;
            case Zhenyu:
                tomarrowimag.setImageResource(R.drawable.org3_ww3);

                break;
            case Leizhenyu:
                tomarrowimag.setImageResource(R.drawable.org3_ww4);

                break;
            case Yujiaxue:
                tomarrowimag.setImageResource(R.drawable.org3_ww6);

                break;
            case Xiaoxue:
                tomarrowimag.setImageResource(R.drawable.org3_ww14);

                break;
            case Zhongxue:
                tomarrowimag.setImageResource(R.drawable.org3_ww15);

                break;
            case Daxue:
                tomarrowimag.setImageResource(R.drawable.org3_ww16);

                break;
            case Baoxue:
                tomarrowimag.setImageResource(R.drawable.org3_ww17);

                break;
        }
        bg.setVisibility(View.VISIBLE);
        weatherInfoLayout.setVisibility(View.VISIBLE);
        //cityNameText.setVisibility(View.VISIBLE);
        Intent intent=new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
    @Override
    public void onBackPressed(){
       if(mDrawerLayout.isDrawerOpen(menulist))
           mDrawerLayout.closeDrawers();
        else
           finish();
    }

}
