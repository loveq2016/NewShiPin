package com.xue.liang.app.v2.utils;

import com.xue.liang.app.v2.data.bean.MemberInfo;
import com.xue.liang.app.v2.data.reponse.YiDongAlarmResp;
import com.xue.liang.app.v2.data.reponse.YidongGroupMemberResp;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/10/11.
 */
public class XmlAnalysisUtils {

    /**
     * 解析移动6995报警平台XML返回
     *
     * @param xmlInfo
     */
    public static YiDongAlarmResp analysisYiDongSendCallXML(String xmlInfo) {
        YiDongAlarmResp yiDongAlarmResp = null;
        try {
            yiDongAlarmResp = getYidongAlarm(xmlInfo);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return yiDongAlarmResp;

    }


    /**
     * 从XML文件中读取数据
     *
     * @param xmlInfo
     */
    private static YiDongAlarmResp getYidongAlarm(String xmlInfo) throws Exception {

        InputStream xml = new ByteArrayInputStream(xmlInfo.getBytes());

        YiDongAlarmResp yiDongAlarmResp = null;
// 获得pull解析器工厂
        XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
//获取XmlPullParser的实例
        XmlPullParser pullParser = pullParserFactory.newPullParser();
// 设置需要解析的XML数据
        pullParser.setInput(xml, "UTF-8");
// 取得事件
        int event = pullParser.getEventType();
// 若为解析到末尾
        while (event != XmlPullParser.END_DOCUMENT) // 文档结束
        {
// 节点名称
            String nodeName = pullParser.getName();
            switch (event) {
                case XmlPullParser.START_DOCUMENT: // 文档开始
                    yiDongAlarmResp = new YiDongAlarmResp();
                    break;
                case XmlPullParser.START_TAG: // 标签开始
                    if ("Response".equals(nodeName)) {
                        String resultCode = pullParser.getAttributeValue(0);
                        String resultMsg = pullParser.getAttributeValue(1);
                        String action = pullParser.getAttributeValue(2);

                        yiDongAlarmResp.setResultMsg(resultMsg);
                        yiDongAlarmResp.setResultCode(resultCode);
                        yiDongAlarmResp.setAction(action);
                    }

                    break;
                case XmlPullParser.END_TAG: // 标签结束
                    break;

            }
            event = pullParser.next(); // 下一个标签
        }

        return yiDongAlarmResp;
    }


    /**
     * 从XML文件中读取数据
     *
     * @param xmlInfo
     */
    public static YidongGroupMemberResp getGroupMemberFromXml(String xmlInfo) throws Exception {

        InputStream xml = new ByteArrayInputStream(xmlInfo.getBytes());

        YidongGroupMemberResp yidongGroupMemberResp = null;
// 获得pull解析器工厂
        XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
//获取XmlPullParser的实例
        XmlPullParser pullParser = pullParserFactory.newPullParser();
// 设置需要解析的XML数据
        pullParser.setInput(xml, "UTF-8");
// 取得事件
        int event = pullParser.getEventType();
// 若为解析到末尾
        while (event != XmlPullParser.END_DOCUMENT) // 文档结束
        {
// 节点名称
            String nodeName = pullParser.getName();
            switch (event) {
                case XmlPullParser.START_DOCUMENT: // 文档开始
                    yidongGroupMemberResp = new YidongGroupMemberResp();
                    break;
                case XmlPullParser.START_TAG: // 标签开始
                    if ("Response".equals(nodeName)) {
                        String resultCode = pullParser.getAttributeValue(0);
                        String resultMsg = pullParser.getAttributeValue(1);
                        String action = pullParser.getAttributeValue(2);

                        yidongGroupMemberResp.getYiDongAlarmResp().setResultMsg(resultMsg);
                        yidongGroupMemberResp.getYiDongAlarmResp().setResultCode(resultCode);
                        yidongGroupMemberResp.getYiDongAlarmResp().setAction(action);
                    }
                    if ("Member".equals(nodeName)) {
                        MemberInfo memberInfo = new MemberInfo();
                        String trueName = pullParser.getAttributeValue(0);
                        String tel = pullParser.getAttributeValue(1);
                        String groupName = pullParser.getAttributeValue(2);
                        memberInfo.setGroupName(groupName);
                        memberInfo.setTel(tel);
                        memberInfo.setTrueName(trueName);

                        yidongGroupMemberResp.getMemberList().add(memberInfo);


                    }

                    break;
                case XmlPullParser.END_TAG: // 标签结束
                    break;

            }
            event = pullParser.next(); // 下一个标签
        }

        return yidongGroupMemberResp;
    }

}
