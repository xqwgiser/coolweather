package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import model.City;
import model.CoolWeatherDB;
import model.County;
import model.Province;

/**
 * Created by xqw on 2015/5/5.
 */
public class Utility {
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
        if(!TextUtils.isEmpty(response)){
            String[]allProvinces=response.split(",");
            if(allProvinces!=null&&allProvinces.length>0){
                for (String p:allProvinces){
                    String[]array=p.split("\\|");
                    Province province=new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }
    public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[]allCities=response.split(",");
            if(allCities!=null&&allCities.length>0){
                for (String c:allCities){
                    String[]array=c.split("\\|");
                    City city=new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }
    public synchronized static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            String[]allCounties=response.split(",");
            if(allCounties!=null&&allCounties.length>0){
                for (String c:allCounties){
                    String[]array=c.split("\\|");
                    County county=new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }
    public static void handleWeatherResponse(Context context,String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONObject weatherInfo=jsonObject.getJSONObject("weatherinfo");
            String cityName=weatherInfo.getString("city");
            String weatherCode=weatherInfo.getString("cityid");
            String temp1=weatherInfo.getString("temp1");
            String temp2=weatherInfo.getString("temp2");
            String weatherDesp=weatherInfo.getString("weather1");
            String weather2=weatherInfo.getString("weather2");
            String cloth=weatherInfo.getString("index_d");
            String publishTime=weatherInfo.getString("fchh");
            String cltext=weatherInfo.getString("index_cl");
            String lytext=weatherInfo.getString("index_tr");
            String ssdtext=weatherInfo.getString("index_co");
            saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,weather2,cloth,publishTime,cltext,lytext,ssdtext);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public static void saveWeatherInfo(Context context,String cityName,String weatherCode,String temp1,String temp2,String weatherDesp,String weather2,String cloth,String publishTime,String cltext,String lytext,String ssdtext){
        SimpleDateFormat adf=new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name",cityName);
        editor.putString("weather_code",weatherCode);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weather_desp",weatherDesp);
        editor.putString("weather2",weather2);
        editor.putString("cloth",cloth);
        editor.putString("publish_time",publishTime);
        editor.putString("current_date",adf.format(new Date()));
        editor.putString("cl_text",cltext);
        editor.putString("ly_text",lytext);
        editor.putString("ssd_text",ssdtext);
        editor.commit();
    }
}
