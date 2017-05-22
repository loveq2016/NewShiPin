package com.xue.liang.app.v2.utils;

import com.xue.liang.app.v2.base.Constant;
import com.xue.liang.app.v2.entity.GroupMember6995Entity;
import com.xue.liang.app.v2.entity.SendCall6995Entity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by jikun on 17/5/20.
 */

public class HttpInfo6995Builder {


    public static String buildSendCall(String phonenum, int type) {
        String encrypInfo = "";

        try {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Action=SendCall");
            stringBuilder.append("&Account=Xlgc");
            stringBuilder.append("&Password=Xlgc");
            stringBuilder.append("&SendTel=" + phonenum);
            stringBuilder.append("&SendType=" + type);//1-语音、2-短信、3-多方通话
            stringBuilder.append("&Content=报警求助");
            String pamars = stringBuilder.toString();

            encrypInfo = Des3DesUtils.encryptThreeDESECB(pamars, Constant.KEY_6995);
        } catch (Exception e) {

        }


        return encrypInfo;

    }

    public static String buildGetGroupMember(String phonenum) {
        String encrypInfo = "";

        try {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Action=GetGroupMember");
            stringBuilder.append("&Account=Xlgc");
            stringBuilder.append("&Password=Xlgc");
            stringBuilder.append("&UserNumber=" + phonenum);
            String pamars = stringBuilder.toString();
            encrypInfo = Des3DesUtils.encryptThreeDESECB(pamars, Constant.KEY_6995);
        } catch (Exception e) {

        }


        return encrypInfo;

    }

    public static String buildAddGroupMemberInfo(String phonenum, String addPhonenum, String addName) {
        String encrypInfo = "";

        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Action=AddMember");
            stringBuilder.append("&Account=Xlgc");
            stringBuilder.append("&Password=Xlgc");
            stringBuilder.append("&UserNumber=" + phonenum);
            stringBuilder.append("&DestUserNumber=" + addPhonenum);
            stringBuilder.append("|" + addName);
            String pamars = stringBuilder.toString();
            encrypInfo = Des3DesUtils.encryptThreeDESECB(pamars, Constant.KEY_6995);
        } catch (Exception e) {

        }


        return encrypInfo;

    }


    public static class XmlAnalysisUtils {

        /**
         * 解析移动6995报警平台XML返回
         *
         * @param xmlInfo
         */
        public static SendCall6995Entity analysisSendCall6995EntityXML(String xmlInfo) {
            SendCall6995Entity sendCall6995Entity = null;
            try {
                String des = Des3DesUtils.decryptThreeDESECB(xmlInfo, Constant.KEY_6995);
                sendCall6995Entity = getSendCall6995EntityEntity(des);
            } catch (Exception e) {

                e.printStackTrace();
            }
            return sendCall6995Entity;

        }


        /**
         * 从XML文件中读取数据
         *
         * @param xmlInfo
         */
        private static SendCall6995Entity getSendCall6995EntityEntity(String xmlInfo) throws Exception {

            InputStream xml = new ByteArrayInputStream(xmlInfo.getBytes());

            SendCall6995Entity sendCall6995Entity = null;
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
                        sendCall6995Entity = new SendCall6995Entity();
                        break;
                    case XmlPullParser.START_TAG: // 标签开始
                        if ("Response".equals(nodeName)) {
                            String resultCode = pullParser.getAttributeValue(0);
                            String resultMsg = pullParser.getAttributeValue(1);
                            String action = pullParser.getAttributeValue(2);

                            sendCall6995Entity.setResultMsg(resultMsg);
                            sendCall6995Entity.setResultCode(resultCode);
                            sendCall6995Entity.setAction(action);
                        }

                        break;
                    case XmlPullParser.END_TAG: // 标签结束
                        break;

                }
                event = pullParser.next(); // 下一个标签
            }

            return sendCall6995Entity;
        }


        public static GroupMember6995Entity analysisGroupMember6995EntityXML(String xmlInfo) {
            GroupMember6995Entity groupMember6995Entity = null;
            try {
                String des = Des3DesUtils.decryptThreeDESECB(xmlInfo, Constant.KEY_6995);
                groupMember6995Entity = getGroupMemberFromXml(des);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return groupMember6995Entity;


        }

        /**
         * 从XML文件中读取数据
         *
         * @param xmlInfo
         */
        public static GroupMember6995Entity getGroupMemberFromXml(String xmlInfo) throws Exception {

            String des = Des3DesUtils.decryptThreeDESECB(xmlInfo, Constant.KEY_6995);

            InputStream xml = new ByteArrayInputStream(des.getBytes());

            GroupMember6995Entity groupMember6995Entity = null;
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
                        groupMember6995Entity = new GroupMember6995Entity();
                        break;
                    case XmlPullParser.START_TAG: // 标签开始
                        if ("Response".equals(nodeName)) {
                            String resultCode = pullParser.getAttributeValue(0);
                            String resultMsg = pullParser.getAttributeValue(1);
                            String action = pullParser.getAttributeValue(2);

                            groupMember6995Entity.getSendCall6995Entity().setResultMsg(resultMsg);
                            groupMember6995Entity.getSendCall6995Entity().setResultCode(resultCode);
                            groupMember6995Entity.getSendCall6995Entity().setAction(action);
                        }
                        if ("Member".equals(nodeName)) {
                            GroupMember6995Entity.MemberInfo memberInfo = new GroupMember6995Entity.MemberInfo();
                            String trueName = pullParser.getAttributeValue(0);
                            String tel = pullParser.getAttributeValue(1);
                            String groupName = pullParser.getAttributeValue(2);
                            memberInfo.setGroupName(groupName);
                            memberInfo.setTel(tel);
                            memberInfo.setTrueName(trueName);

                            groupMember6995Entity.getMemberList().add(memberInfo);


                        }

                        break;
                    case XmlPullParser.END_TAG: // 标签结束
                        break;

                }
                event = pullParser.next(); // 下一个标签
            }

            return groupMember6995Entity;
        }

    }


}
