package model;

/**
 * Created by xqw on 2015/5/4.
 */
public class Province {
    private int id;
    private String provinceName;
    private String ProvinceCode;
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getProvinceName(){
        return provinceName;
    }
    public void setProvinceName(String provinceName){
        this.provinceName=provinceName;
    }
    public String getProvinceCode(){
        return ProvinceCode;
    }
    public void setProvinceCode(String provinceCode){
        this.ProvinceCode=provinceCode;
    }
}
