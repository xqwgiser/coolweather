package model;

/**
 * Created by xqw on 2015/5/26.
 */
public class Menuitem {
    private String context;
    private int imageId;
    public Menuitem(String context,int imageId){
        this.context=context;
        this.imageId=imageId;
    }
    public String getContext(){
        return context;
    }
    public int getImageId(){
        return  imageId;
    }
}
