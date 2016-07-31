package com.temobi.vcp.sdk.data;

import java.io.Serializable;

////一个星期计划，以小时为单位
public class WeekInfo implements Serializable{

	public int nPlanID;//计划ID
	public int bEnable;// //是否启用  0不启动，1启用
	public int bCycle;////是否循环  0不循环，1循环
	public String sWeekFlag;////1101101 表示星期三和星期六没计划
	public char[][] sWeek;////格式;String[7][25],星期一为数组0，值为" 011100001100110011001101" 24位字符表示0-23小时，0表示该小时不操作，1表示该小时操作
}
