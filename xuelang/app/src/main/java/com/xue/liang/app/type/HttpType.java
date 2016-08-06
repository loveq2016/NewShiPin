package com.xue.liang.app.type;

/**
 * Created by Administrator on 2016/8/6.
 */
public enum HttpType {
    FireAlarm(102), // 火警
    TheftAlarm(103), // 盗窃

    HurtAlarm(104), // 伤害
    OtherAlarm(101);// 其它

    private int value;

    HttpType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
