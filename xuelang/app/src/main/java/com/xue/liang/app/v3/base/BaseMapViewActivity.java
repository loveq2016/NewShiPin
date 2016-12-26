package com.xue.liang.app.v3.base;

import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;

/**
 * Created by jikun on 2016/12/26.
 */

public abstract class BaseMapViewActivity extends BaseActivity {

    //private LatLng latlng = new LatLng(39.761, 116.434);

    private MarkerOptions markerOption;
    private AMap aMap;
    private MapView mapView;


    protected void initMapView(Bundle savedInstanceState) {
        mapView = getMapView();
        mapView.onCreate(savedInstanceState); // 此方法必须重写
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }

    public abstract MapView getMapView();

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 在地图上添加marker
     */
    protected void addMarkersToMap(double latitude, double longitude) {
        LatLng latlng = new LatLng(latitude, longitude);

        changeCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        latlng, 18, 30, 30)));
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latlng)
                .draggable(true);
        aMap.addMarker(markerOption);
    }
    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update) {

        aMap.moveCamera(update);

    }
}
