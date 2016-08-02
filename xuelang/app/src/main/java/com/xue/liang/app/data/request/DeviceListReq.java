package com.xue.liang.app.data.request;

/**
 * Created by jk on 2016/8/2.
 */
public class DeviceListReq extends BaseRequest {

    private String stream_type;

    private String region_id;

    public DeviceListReq(String terminal_type, String reg_tel, String reg_id,String stream_type,String region_id) {
        super(terminal_type, reg_tel, reg_id);
        this.stream_type=stream_type;
        this.region_id=region_id;
    }

    public String getStream_type() {
        return stream_type;
    }

    public void setStream_type(String stream_type) {
        this.stream_type = stream_type;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }
}
