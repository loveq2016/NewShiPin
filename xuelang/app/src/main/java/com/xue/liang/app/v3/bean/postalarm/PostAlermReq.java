package com.xue.liang.app.v3.bean.postalarm;

/**
 * Created by Administrator on 2016/11/6.
 */

public class PostAlermReq {

    private String termi_type;//是  报警终端类型，
//    0: 中间件机顶盒(默认)
//    1: OTT 机顶盒
//    2：手机

    private String stb_id;//是  机顶盒序列号

    private String user_id;//否  用户 id

    private String stb_car_id;//是  机顶盒智能卡号

    private int stb_type;//是  机顶盒分类标识，等同于 termi_type
    //    0: 中间件机顶盒(默认)
//    1: OTT 机顶盒
//    2：手机
    private String stb_info;

    private int alerm_level; // 是 告警级别 0~5

    private int alerm_type;// 是  告警类型:
//    -1, 一键报警
//    0, 医疗救助
//    1, 火灾
//    2, 盗抢
//    3, 车祸
//    4, 个人求助
//    5, 危险伤害
//    6, 安全隐患
//    7, 信访纠纷
//    8, 民生服务
//    9, 其它
//    10, 火警 119
//    11, 公安 110
//    12, 急救中心 120
//    13, 普通报警
//    101, 医疗救助

    private String cam_dev_uid;  //是  摄像头唯一识别码 GUID

    private String cam_dev_name;// 是  摄像头名

    private String cam_url;// 是  摄像头的推流地址

    private String update_time;//   是  触发告警的时间

    private double longitude;//  否  精度

    private double latitude;//  否  维度


    public String getTermi_type() {
        return termi_type;
    }

    public void setTermi_type(String termi_type) {
        this.termi_type = termi_type;
    }

    public String getStb_id() {
        return stb_id;
    }

    public void setStb_id(String stb_id) {
        this.stb_id = stb_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStb_car_id() {
        return stb_car_id;
    }

    public void setStb_car_id(String stb_car_id) {
        this.stb_car_id = stb_car_id;
    }

    public int getStb_type() {
        return stb_type;
    }

    public void setStb_type(int stb_type) {
        this.stb_type = stb_type;
    }

    public String getStb_info() {
        return stb_info;
    }

    public void setStb_info(String stb_info) {
        this.stb_info = stb_info;
    }

    public int getAlerm_level() {
        return alerm_level;
    }

    public void setAlerm_level(int alerm_level) {
        this.alerm_level = alerm_level;
    }

    public int getAlerm_type() {
        return alerm_type;
    }

    public void setAlerm_type(int alerm_type) {
        this.alerm_type = alerm_type;
    }

    public String getCam_dev_uid() {
        return cam_dev_uid;
    }

    public void setCam_dev_uid(String cam_dev_uid) {
        this.cam_dev_uid = cam_dev_uid;
    }

    public String getCam_dev_name() {
        return cam_dev_name;
    }

    public void setCam_dev_name(String cam_dev_name) {
        this.cam_dev_name = cam_dev_name;
    }

    public String getCam_url() {
        return cam_url;
    }

    public void setCam_url(String cam_url) {
        this.cam_url = cam_url;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
