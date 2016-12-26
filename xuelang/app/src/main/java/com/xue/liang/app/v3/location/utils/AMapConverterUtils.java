package com.xue.liang.app.v3.location.utils;

import android.content.Context;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;

/**
 * Created by jikun on 2016/12/12.
 */

public class AMapConverterUtils {

    /**
     * 坐标转换  如果
     *
     * @param context
     * @param var1
     * @param var3
     * @return 返回null那么转换失败  不为null则成功
     */
    public static DPoint convert(Context context, double var1, double var3) {
        try {
            DPoint examplePoint = new DPoint(39.911127, 116.433608);
            //初始化坐标转换类
            CoordinateConverter converter = new CoordinateConverter(
                    context);
            /**
             * 设置坐标来源,这里使用百度坐标作为示例
             * 可选的来源包括：
             * <li>CoordType.BAIDU ： 百度坐标
             * <li>CoordType.MAPBAR ： 图吧坐标
             * <li>CoordType.MAPABC ： 图盟坐标
             * <li>CoordType.SOSOMAP ： 搜搜坐标
             * <li>CoordType.ALIYUN ： 阿里云坐标
             * <li>CoordType.GOOGLE ： 谷歌坐标
             * <li>CoordType.GPS ： GPS坐标
             */
            converter.from(CoordinateConverter.CoordType.BAIDU);
            //设置需要转换的坐标
            converter.coord(examplePoint);
            //转换成高德坐标
            DPoint destPoint = converter.convert();

            return destPoint;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //判断坐标是否高德地图可用
    private boolean checkIsAvaliable(Context context, double latitude, double longitude) {
        //初始化坐标工具类
        CoordinateConverter converter = new CoordinateConverter(
                context);
        //判断是否高德地图可用的坐标
        boolean result = converter.isAMapDataAvailable(latitude, longitude);
        return result;
//        if (result) {
//            tvCheckReult.setText("该坐标是高德地图可用坐标");
//        } else {
//            tvCheckReult.setText("该坐标不能用于高德地图");
//        }
    }
}
