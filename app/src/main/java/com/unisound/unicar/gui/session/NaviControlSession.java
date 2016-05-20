/**
 * Copyright (c) 2012-2016 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : NaviControlSession.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.session
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2016-1-21
 */
package com.unisound.unicar.gui.session;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.uninavi.UniCarNaviConstant;
import com.unisound.unicar.gui.uninavi.UniCarNaviUtil;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;

/**
 * 
 * @author xiaodong.he
 * @date 2016-1-21
 */
public class NaviControlSession extends BaseSession {

    private static final String TAG = NaviControlSession.class.getSimpleName();

    private String mOperator;

    /**
     * @param context
     * @param sessionManagerHandler
     */
    public NaviControlSession(Context context, Handler sessionManagerHandler) {
        super(context, sessionManagerHandler);
    }

    public void putProtocol(JSONObject jsonProtocol) {
        super.putProtocol(jsonProtocol);

        // XD 2016-2-4 add
        boolean isNeedSessionDone = true;

        mOperator = JsonTool.getJsonValue(mDataObject, SessionPreference.KEY_OPERATOR, "");
        Logger.d(TAG, "putProtocol---mOperator = " + mOperator);
        if (TextUtils.isEmpty(mOperator)) {
            return;
        }
        if (SessionPreference.VALUE_OPERATOR_ROUTE_AVOID_CONGESTION.equals(mOperator)) {
            UniCarNaviUtil.sendControlRoutePlan(mContext,
                    UniCarNaviConstant.VALUE_ROUTE_PLAN_AVOID_CONGESTION);
        } else if (SessionPreference.VALUE_OPERATOR_ROUTE_HIGHWAY.equals(mOperator)) {
            UniCarNaviUtil.sendControlRoutePlan(mContext,
                    UniCarNaviConstant.VALUE_ROUTE_PLAN_HIGHWAY);
        } else if (SessionPreference.VALUE_OPERATOR_ROUTE_NO_HIGHWAY.equals(mOperator)) {
            UniCarNaviUtil.sendControlRoutePlan(mContext,
                    UniCarNaviConstant.VALUE_ROUTE_PLAN_NO_HIGHWAY);
        } else if (SessionPreference.VALUE_OPERATOR_ROUTE_SAVE_MONEY.equals(mOperator)) {
            UniCarNaviUtil.sendControlRoutePlan(mContext,
                    UniCarNaviConstant.VALUE_ROUTE_PLAN_SAVE_MONEY);
        } else if (SessionPreference.VALUE_OPERATOR_START_NAVIGATION.equals(mOperator)) {
            UniCarNaviUtil.sendBeginNavigationAction(mContext);
        } else if (SessionPreference.VALUE_OPERATOR_ACT_NAVMODE_DEMO.equals(mOperator)) {
            UniCarNaviUtil.sendControlNaviAction(mContext,
                    UniCarNaviConstant.VALUE_OPERATION_EMULATE_NAVI_START);
        } else if (SessionPreference.VALUE_OPERATOR_ACT_NAV_PAUSE.equals(mOperator)) {
            UniCarNaviUtil.sendControlNaviAction(mContext,
                    UniCarNaviConstant.VALUE_OPERATION_EMULATE_NAVI_PAUSE);
        } else if (SessionPreference.VALUE_OPERATOR_ACT_NAV_CONTINUE.equals(mOperator)) {
            UniCarNaviUtil.sendControlNaviAction(mContext,
                    UniCarNaviConstant.VALUE_OPERATION_EMULATE_NAVI_CONTINUE);
        } else if (SessionPreference.VALUE_OPERATOR_ACT_NAVMODE_NIGHT.equals(mOperator)) {
            UniCarNaviUtil.sendSetDisplayModeAction(mContext,
                    UniCarNaviConstant.VALUE_DISPLAY_MODE_NIGHT);
        } else if (SessionPreference.VALUE_OPERATOR_ACT_NAVMODE_STANDARD.equals(mOperator)) {
            UniCarNaviUtil.sendSetDisplayModeAction(mContext,
                    UniCarNaviConstant.VALUE_DISPLAY_MODE_STANDARD);
        } else if (SessionPreference.VALUE_OPERATOR_ACT_NAV_SHOWTRAFFIC.equals(mOperator)) {
            UniCarNaviUtil.sendControlNaviAction(mContext,
                    UniCarNaviConstant.VALUE_OPERATION_ROAD_CONDITION_SHOW);
        } else if (SessionPreference.VALUE_OPERATOR_ACT_NAV_CLOSETRAFFIC.equals(mOperator)) {
            UniCarNaviUtil.sendControlNaviAction(mContext,
                    UniCarNaviConstant.VALUE_OPERATION_ROAD_CONDITION_CLOSE);
        } else if (SessionPreference.VALUE_OPERATOR_ACT_NAV_CLOSE.equals(mOperator)) {
            String type = JsonTool.getJsonValue(mDataObject, SessionPreference.KEY_TYPE, "");
            // XD 2016-2-4 modify
            boolean isPlayTTS = true;
            if (SessionPreference.PARAM_TYPE_NOT_PLAY_TTS.equals(type)) {
                isNeedSessionDone = false;
                isPlayTTS = false;
            }
            Logger.d(TAG, "ACT_NAV_CLOSE---isPlayTTS: " + isPlayTTS);
            UniCarNaviUtil.sendCloseNaviAction(mContext, isPlayTTS);
        }

        if (isNeedSessionDone) {
            mSessionManagerHandler.sendEmptyMessageDelayed(SessionPreference.MESSAGE_SESSION_DONE,
                    300);
        } else {
            mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_RELEASE_ONLY);
        }
    }


    @Override
    public void onTTSEnd() {
        Logger.d(TAG, "onTTSEnd---");
        super.onTTSEnd();

    }

}
