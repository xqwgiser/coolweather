package util;

import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by xqw on 2015/5/9.
 */
public class MyLocationListener implements BDLocationListener {
    private String sb;
    private String countycode;
    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null) {
            sb = null;
            return;
        }
        if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
            sb = location.getDistrict();
        }


    }
    public String getlocationId(InputStream inputStream){
        InputStreamReader inputStreamReader=null;
        try {
            inputStreamReader=new InputStreamReader(inputStream,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        BufferedReader reader=new BufferedReader(inputStreamReader);
        StringBuffer stringBuffer=new StringBuffer("");
        String line;
        try {
            while ((line=reader.readLine())!=null){
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        String response=stringBuffer.toString();
        if(!TextUtils.isEmpty(response)){
            String[]allcities=response.split("\n");
            if(allcities!=null&&allcities.length>0){
                for(int i=0;i<allcities.length;i++){
                    if(allcities[i].contains(sb)){
                        String[] code=allcities[i].split("=");
                        countycode=code[0];
                    }
                }

            }
        }
        return countycode;
    }
}