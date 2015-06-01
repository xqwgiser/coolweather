package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xqw.coolweather.R;

import java.util.List;

/**
 * Created by xqw on 2015/5/26.
 */
public class MenuAdapter extends ArrayAdapter<Menuitem> {
    private int resourceId;
    public MenuAdapter(Context context,int textviewResourceId,List<Menuitem>objects){
        super(context,textviewResourceId,objects);
        resourceId=textviewResourceId;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        Menuitem menuitem=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        ImageView menuimage=(ImageView)view.findViewById(R.id.menu_image);
        TextView menucontext=(TextView)view.findViewById(R.id.menuitem);
        menuimage.setImageResource(menuitem.getImageId());
        menucontext.setText(menuitem.getContext());
        return view;
    }
}
