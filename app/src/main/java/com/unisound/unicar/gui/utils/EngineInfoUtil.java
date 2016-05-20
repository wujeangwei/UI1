/**
 * Copyright (c) 2012-2016 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : EngineInfoUtil.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.utils
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2016-2-16
 */
package com.unisound.unicar.gui.utils;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.unisound.unicar.gui.preference.SessionPreference;

/**
 * @author xiaodong.he
 * 
 */
public class EngineInfoUtil {

    public static final String TAG = EngineInfoUtil.class.getSimpleName();

    public static boolean isASREngineDueDate = false;


    /**
     * updateGuiVersionByEngineInfo
     * 
     * @author xiaodong.he
     * @date 2016-01-11
     * @param infoJson
     */
    public static void updateGuiVersionByEngineInfo(JSONObject infoJson) {
        if (infoJson == null) {
            return;
        }
        try {
            int engineInfoType = infoJson.getInt(SessionPreference.KEY_ENGINE_INFO_ASRKEY);
            String dueDateStr = infoJson.getString(SessionPreference.KEY_ENGINE_INFO_ASRVALUE);
            // "2016-9-30"
            Date dueDate = DateUtil.toDate(dueDateStr, "yyyy-MM-dd");
            isASREngineDueDate = DateUtil.isDueDate(dueDate);
            Logger.d(TAG, "updateGuiVersionByEngineInfo---engineType = " + engineInfoType
                    + "; dueDateStr:" + dueDateStr + "; isASREngineDueDate: " + isASREngineDueDate);
            switch (engineInfoType) {
                case SessionPreference.VALUE_ENGINE_INFO_ASRKEY_NO_LIMIT:
                    GUIConfig.isTestVersion = false;
                    break;
                case SessionPreference.VALUE_ENGINE_INFO_ASRKEY_TIME_LIMIT:
                    GUIConfig.isTestVersion = true;
                    break;
                case SessionPreference.VALUE_ENGINE_INFO_ASRKEY_PACKAGE_LIMIT:
                    GUIConfig.isTestVersion = false;
                    break;
                case SessionPreference.VALUE_ENGINE_INFO_ASRKEY_TIME_PACKAGE_LIMIT:
                    GUIConfig.isTestVersion = true;
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
