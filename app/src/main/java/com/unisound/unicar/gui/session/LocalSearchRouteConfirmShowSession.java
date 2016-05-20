/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * 
 * @FileName : LocalSearchRouteConfirmShowSession.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.session
 * @Author : Alieen
 * @CreateDate : 2015-07-21
 */
package com.unisound.unicar.gui.session;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.unisound.unicar.gui.location.operation.LocationModelProxy;
import com.unisound.unicar.gui.model.LocationInfo;
import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.route.baidu.BaiduMap;
import com.unisound.unicar.gui.route.operation.GaodeMap;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.view.LocalSearchRouteConfirmView;
import com.unisound.unicar.gui.view.LocalSearchRouteConfirmView.IRouteWaitingContentViewListener;

/**
 * Navigation & Local Search route show session
 * 
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Alieen
 * @CreateDate : 2015-07-2
 * @ModifiedBy : xiaodong
 * @ModifiedDate: 2015-09-14
 * @Modified:
 */
public class LocalSearchRouteConfirmShowSession extends BaseSession {
    public static final String TAG = "LocalSearchRouteConfirmShowSession";

    private Context mContext;

    private LocalSearchRouteConfirmView mRouteContentView = null;

    private double toLat = 0.0;
    private double toLng = 0.0;
    private String toCity = "";
    /** to name */
    private String toPoi = "";
    /** toAddress XD added 20160107 */
    private String toAddress = "";

    private final int AMAP_INDEX = 1;
    private final int BAIDU_INDEX = 2;
    private final int MAPBAR_INDEX = 3;
    private final int RITU_INDEX = 4;

    private LocationInfo mLocationInfo; // XD added 20150911
    private double mFromLat = 0.0;
    private double mFromLng = 0.0;
    private String mFromPoi = "";
    private String mFromeCity = "";
    private String mCondition = "";
    private int mStyle = 2;
    // add tyz 20160129 途经点类表
    private String mPathPoints = "";

    public LocalSearchRouteConfirmShowSession(Context context, Handler sessionManagerHandler) {
        super(context, sessionManagerHandler);
        this.mContext = context;
    }

    public void putProtocol(JSONObject jsonProtocol) {
        super.putProtocol(jsonProtocol);
        addQuestionViewText(mQuestion);
        Logger.d(TAG, "putProtocol : " + jsonProtocol.toString());
        try {
            JSONObject data = jsonProtocol.getJSONObject(SessionPreference.KEY_DATA);
            toPoi = data.getString(SessionPreference.KEY_NAME);
            JSONObject poiinfo = new JSONObject(data.getString("location"));
            toLat = poiinfo.getDouble("lat");
            toLng = poiinfo.getDouble("lng");
            toCity = poiinfo.getString("city");
            toPoi = poiinfo.getString("name");
            toAddress = poiinfo.getString("address"); // XD added 20160107
            mCondition = poiinfo.getString("condition");
            mPathPoints = poiinfo.getString("pathPoints");
            changeConditionToStyle(mCondition);
            Logger.d(TAG, "mPathPoints = " + mPathPoints);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        getCurrentLocation();

        addAnswerViewText(mAnswer);
        mRouteContentView = new LocalSearchRouteConfirmView(mContext);
        mRouteContentView.setListener(mRouteViewListener);
        mRouteContentView.setEndPOI(toPoi);
        addAnswerView(mRouteContentView);
    }

    /**
     * EBUS_NO_SUBWAY：不要坐地铁 EBUS_WALK_FIRST：最少步行 EBUS_TRANSFER_FIRST：最少换乘 ECAR_DIS_FIRST：最短路程
     * ECAR_FEE_FIRST: 较少费用 TIME_FIRST：最少时间 style:导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5
     * 不走高速且避免收费；6 不走高速且躲避拥堵；7 躲避收费和拥堵；8 不走高速躲避收费和拥堵))
     * 
     * @param condition
     */
    private void changeConditionToStyle(String condition) {
        Logger.d(TAG, "condition = " + condition);
        mStyle = 2;// 每次进来设置值为默认值
        if ("TIME_FIRST".equals(condition)) {
            mStyle = 0;
        } else if ("ECAR_FEE_FIRST".equals(condition)) {
            mStyle = 1;
        } else if ("ECAR_DIS_FIRST".equals(condition)) {
            mStyle = 2;
        } else if ("ECAR_AVOID_TRAFFIC_JAM".equals(condition)) {
            mStyle = 4;
        }
    }

    /**
     * XD added 20150915
     */
    private void getCurrentLocation() {
        Logger.d(TAG, "!--->getCurrentLocation--");
        mLocationInfo = LocationModelProxy.getInstance(mContext).getLastLocation();
        // mLocationInfo = WindowService.mLocationInfo;
        if (mLocationInfo != null) {
            mFromLat = mLocationInfo.getLatitude();
            mFromLng = mLocationInfo.getLongitude();
            mFromeCity = mLocationInfo.getCity();
            mFromPoi = mLocationInfo.getProvince();
        }
    }

    // public static void setFromLat(double fromLat){
    // Logger.d(TAG, "RouteShowSession set fromLat : " + fromLat);
    // mFromLat = fromLat;
    // }
    // public static void setFromLng(double fromLng){
    // Logger.d(TAG, "RouteShowSession set fromLng : " + fromLng);
    // mFromLng = fromLng;
    // }
    // public static void setFromPoi(String fromPoi){
    // Logger.d(TAG, "RouteShowSession set fromPoi : " + fromPoi);
    // mFromPoi = "出发地";
    // }

    protected IRouteWaitingContentViewListener mRouteViewListener =
            new IRouteWaitingContentViewListener() {

                @Override
                public void onCancel() {
                    Logger.d(TAG, "!--->mCallContentViewListener---onCancel()-----");
                    onUiProtocal(SessionPreference.EVENT_NAME_ON_CONFIRM_CANCEL,
                            SessionPreference.EVENT_PROTOCAL_ON_CONFIRM_CANCEL_CALL);
                    mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
                }

                @Override
                public void onOk() {
                    Logger.d(TAG, "!--->mCallContentViewListener---onOk()-----");
                    onUiProtocal(SessionPreference.EVENT_NAME_ON_CONFIRM_OK,
                            SessionPreference.EVENT_PROTOCAL_ON_CONFIRM_OK);
                    mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
                    showRoute(mStyle);
                }

                @Override
                public void onTimeUp() {

                    Logger.d(TAG, "!--->mCallContentViewListener---onTimeUp()-----");
                    onUiProtocal(SessionPreference.EVENT_NAME_ON_CONFIRM_TIME_UP,
                            SessionPreference.EVENT_PROTOCAL_ON_CONFIRM_TIME_UP);
                    // mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
                    showRoute(mStyle);
                }
            };

    private void showRoute(int style) {
        int mapIndex = UserPerferenceUtil.getMapChoose(mContext);
        switch (mapIndex) {
            case AMAP_INDEX:
                Logger.d(TAG, "mapIndex : " + mapIndex + " use amap route ...");
                // XD modify 20150914
                GaodeMap.showRoute(mContext, "driving", mFromLat, mFromLng, mFromeCity, mFromPoi,
                        toLat, toLng, toCity, toPoi, toAddress, style);

                // GaodeMap.showRoute(mContext, "driving", toLat, toLng,toCity, toPoi);
                break;
            case BAIDU_INDEX:
                Logger.d(TAG, "mapIndex : " + mapIndex + " use baidu route ...");
                // XD modify 20150914
                BaiduMap.showRoute(mContext, BaiduMap.ROUTE_MODE_DRIVING, mFromLat, mFromLng,
                        mFromeCity, mFromPoi, toLat, toLng, toCity, toPoi);

                // startNavi(mFromLat, mFromLng, toLat, toLng, mFromPoi, toPoi);
                break;
            case MAPBAR_INDEX:
                Logger.d(TAG, "mapIndex : " + mapIndex + " use mapbar route ...");
                JSONObject mJson = new JSONObject();
                try {
                    mJson.put("toLatitude", toLat);
                    mJson.put("toLongtitude", toLng);
                    mJson.put("toPoi", toPoi);
                    Logger.d(TAG, "msg = " + mJson.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent mIntent = new Intent();
                String action = "android.intent.action.SEND_POIINFO";
                mIntent.setAction(action);
                mIntent.putExtra("poi_info", mJson.toString());
                Logger.d(TAG, "msg = " + action.toString());
                mContext.sendBroadcast(mIntent);
                break;
            case RITU_INDEX:
                Logger.d(TAG, "mapIndex : " + mapIndex + " use ritu route ...");
                Intent intent = new Intent("android.intent.action.ritu.keyword.name");

                double mlng = toLng;
                double mlat = toLat;

                if (toLng < 1000) {
                    mlng = mlng * 3600 * 2560;
                    mlat = mlat * 3600 * 2560;
                }
                int lng = (int) mlng;
                int lat = (int) mlat;

                Logger.d(TAG, "mapIndex : " + mapIndex + " use ritu route ...====lng:" + lng
                        + "======lat:" + lat);
                intent.putExtra("navi_keyword_name", lng + "," + lat + "," + toPoi);
                mContext.sendBroadcast(intent);
                break;

            default:
                break;
        }
    }


    /*
     * public void startNavi(double fromLat, double fromLng, double toLat, double toLng, String
     * fromPoi, String toPoi) { fromLat =
     * LocationModelProxy.getInstance(mContext).getLastLocation().getLatitude(); fromLng =
     * LocationModelProxy.getInstance(mContext).getLastLocation().getLongitude(); fromPoi =
     * LocationModelProxy.getInstance(mContext).getLastLocation().getAddress();
     * 
     * //先将高德坐标转换为百度坐标 LatLng pt1 = new LatLng(fromLat, fromLng); LatLng pt2 = new LatLng(toLat,
     * toLng);
     * 
     * Logger.d(TAG, "from lng : [ " + fromLat + " -- " + fromLng + " ]"); Logger.d(TAG,
     * "to lng : [ " + toLat + " -- " + toLng + " ]"); Logger.d(TAG, "from poi : " + fromPoi +
     * " to poi : " + toPoi);
     * 
     * // 构建 导航参数 NaviPara para = new NaviPara(); para.startPoint = pt1; para.startName = fromPoi;
     * para.endPoint = pt2; para.endName = toPoi;
     * 
     * try { BaiduMapNavigation.openBaiduMapNavi(para, mContext); } catch
     * (BaiduMapAppNotSupportNaviException e) { e.printStackTrace(); } }
     */

    @Override
    public void onTTSEnd() {
        super.onTTSEnd();
        Logger.d(TAG, "!--->mCallContentViewListener---onTTSEnd()-----");
        // mRouteContentView.startCountDownTimer(GUIConfig.TIME_DELAY_AUTO_CONFIRM);
        showRoute(mStyle);
        mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
    }

    @Override
    public void release() {
        Logger.d(TAG, "!--->release");
        super.release();
        if (mRouteContentView != null) {
            mRouteContentView.cancelCountDownTimer();
        }
        mRouteContentView.setListener(null);
    }

}
