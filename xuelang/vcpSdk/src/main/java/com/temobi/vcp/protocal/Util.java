package com.temobi.vcp.protocal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 工具类
 * 
 * @author Temobi
 * 
 */
public class Util {

	// 时间处理
	public static final String PATTERN1 = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN2 = "yyyy/MM/dd HH:mm:ss";
	public static final SimpleDateFormat DATE_FORMAT1 = new SimpleDateFormat(
			PATTERN1);
	public static final SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat(
			PATTERN2);
	/**
	 * 图片压缩 从Bitmap--->file
	 * @param bmp
	 * @param file
	 */
	public static Bitmap compressBmpToFile(Bitmap bmp, File file) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 80;// 个人喜欢从80开始,
		bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > 100) {
			baos.reset();
			options -= 10;
			bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		return bmp;
		
		
	}
	public static Bitmap compressImage(Bitmap image) {  
		  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    }  
	
	
	/**
	 * 字符串转化为Date String convert to Date
	 * 
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static java.util.Date parseDate(String dateStr)
			throws ParseException {
		if (null == dateStr || "".equals(dateStr)) {
			return null;
		}

		java.util.Date date = null;
		if (dateStr.indexOf("/") != -1) {
			date = DATE_FORMAT2.parse(dateStr);
		} else {
			date = DATE_FORMAT1.parse(dateStr);
		}

		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return new java.util.Date(calendar.getTimeInMillis());
		}
		return null;
	}
}
