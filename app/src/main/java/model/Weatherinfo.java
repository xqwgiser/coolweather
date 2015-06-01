package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xqw on 2015/5/28.
 */
public class Weatherinfo implements Parcelable{
    private String cityname;
    private String temp;
    private String weather;
    private String cl;
    private String ly;
    private String ssd;
    private String cy;
    public String getCityname(){
        return cityname;
    }
    public void setCityname(String cityname){
        this.cityname=cityname;
    }
    public String getTemp(){
        return temp;
    }
    public void setTemp(String temp){
        this.temp=temp;
    }
    public String getWeather(){
        return weather;
    }
    public void setWeather(String weather){
        this.weather=weather;
    }
    public String getCl(){
        return cl;
    }
    public void setCl(String cl){
        this.cl=cl;
    }
    public String getLy(){
        return ly;
    }
    public void setLy(String ly){
        this.ly=ly;
    }
    public String getSsd(){
        return ssd;
    }
    public void setSsd(String ssd){
        this.ssd=ssd;
    }
    public String getCy(){
        return cy;
    }
    public void setCy(String cy){
        this.cy=cy;
    }
    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest,int flags){
        dest.writeString(cityname);
        dest.writeString(temp);
        dest.writeString(weather);
        dest.writeString(cl);
        dest.writeString(ly);
        dest.writeString(ssd);
        dest.writeString(cy);
    }
    public static final Creator<Weatherinfo>CREATOR=new Creator<Weatherinfo>() {
        @Override
        public Weatherinfo createFromParcel(Parcel source) {
            Weatherinfo weatherinfo=new Weatherinfo();
            weatherinfo.cityname=source.readString();
            weatherinfo.temp=source.readString();
            weatherinfo.weather=source.readString();
            weatherinfo.cl=source.readString();
            weatherinfo.ly=source.readString();
            weatherinfo.ssd=source.readString();
            weatherinfo.cy=source.readString();
            return weatherinfo;
        }

        @Override
        public Weatherinfo[] newArray(int size) {
            return new Weatherinfo[size];
        }
    };
}
