package com.temobi.vcp.protocal;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;
import android.util.Log;

/**
 * 密码学中的高级加密标准（Advanced Encryption Standard，AES），
 * 又称  高级加密标准Rijndael加密法，是美国联邦政府采用的�?��区块加密标准�?
 * 这个标准用来替代原先的DES，已经被多方分析且广为全世界�?��用�?
 * 经过五年的甄选流程，高级加密标准由美国国家标准与�?��研究�?（NIST）于2001�?1�?6日发布于FIPS PUB 197，并�?002�?�?6日成为有效的标准�?
 * 密钥长度的最少支持为128�?92�?56，分组长�?28�?
 * AES加密数据块和密钥长度可以�?28比特�?92比特�?56比特中的任意�?��
 * 注意�?
 * 1.密匙长度�?6byte,不够16byte的补0，超�?6byte的截断多出的部分
 * 2.明文的数据块长度选用16byte,不足时补0
 * @author lixu
 * Jun 7, 2011-common
 */
public class AESForC {
	public static final int KEY_LEN = 16;//密匙长度
	public static final int BLOCK_SIZE = 16;//块长�?
	
	//不足16位字节补�?
	private static final byte[][] FILL_Array = new byte[][]
	                     {{},{0},{0,0},{0,0,0},{0,0,0,0},{0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}
	                     ,{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0}
	                     ,{0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
	
	 /**
     * 加密函数
     *
     * @param realData
     *            加密数据
     * @param realKey
     *            密钥
     * @return 返回加密后的数据
     */
    public static byte[] ECBEncrypt(byte[] data, byte[] key) {
    	//把密匙设置为16字节
    	byte[] realKey = formatKey(key);
    	
    	//把需要加密的明文设置�?6字节的�?数，否则�?
    	byte[] realData = data;
    	if(realData.length%BLOCK_SIZE != 0)
    	{
    		int fillLen = BLOCK_SIZE-data.length%BLOCK_SIZE;//�?��填充的字节数
    		realData = new byte[data.length+fillLen];
    		System.arraycopy(data, 0, realData, 0, data.length);
    		System.arraycopy(FILL_Array[fillLen], 0, realData, data.length, fillLen);
    	}
    	
        try {
            // AES算法要求有一个可信任的随机数�?
            SecureRandom sr = new SecureRandom();
                     
            // �?��SecretKey对象 
            SecretKey secretKey = new SecretKeySpec(realKey, "AES");

            // using AES in ECB mode  DESede/CBC/PKCS5Padding//"AES/ECB/NoPadding"
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);

            // 执行加密操作
            byte encryptedData[] = cipher.doFinal(realData);

            return encryptedData;
        } catch (Exception e) {
            System.err.println("AES算法，加密数据出�?");
            e.printStackTrace();
        }

        return null;
    }
    
    public static byte[] ECBDecrypt(byte[] data, byte[] key) {
    	//把密匙设置为16字节
    	byte[] realKey = formatKey(key);
    	
        try {
            // AES算法要求有一个可信任的随机数�?
            SecureRandom sr = new SecureRandom();
                       
            // �?��SecretKey对象 
            SecretKey secretKey = new SecretKeySpec(realKey, "AES");

            // using AES in ECB mode
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);

            // 执行加密操作
            byte encryptedData[] = cipher.doFinal(data);

            return encryptedData;
        } catch (Exception e) {
            System.err.println("AES算法，解密数据出�?");
            e.printStackTrace();
        }

        return null;
    }
    
    /**
     * 格式化密匙为16字节
     * @param key
     * @return
     */
    private static byte[] formatKey(byte[] key)
    {
       	//把密匙设置为16字节
    	byte[] realKey = null;
    	if(key.length==16)
    	{
    		realKey = key;
    	}
    	else
    	{
    		realKey = new byte[KEY_LEN];
    		if(key.length<KEY_LEN)
    		{
    			System.arraycopy(key, 0, realKey, 0, key.length);
    			System.arraycopy(FILL_Array[KEY_LEN-key.length], 0, realKey, key.length, KEY_LEN-key.length);
    		}
    		else
    		{
    			System.arraycopy(key, 0, realKey, 0, KEY_LEN);
    		}
    	}
    	return realKey;
    }
    
    static char hexDigits[] = { '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f' };
    //MD5加密算法
	public static String getMD5String(String input) throws Exception{
		Log.i("getMD5String input:", input);
		//获得MD5摘要算法�?MessageDigest 对象
		MessageDigest mdInst = MessageDigest.getInstance("MD5");
		//使用指定的字节更新摘�?
		mdInst.update(input.getBytes());
		//获得密文
		byte[] md = mdInst.digest();
		//把密文转换成十六进制的字符串形式
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        //返回字符�?
		String output = new String(str);
		Log.i("getMD5String output:", output);
		return output;
	}
	
	//aes加密 base64编码
	public static String aesEncode(String data, String key){
		Log.i("AESForC", "input:"+data);
		Log.i("AESForC", "key:"+key);
		//aes加密
		byte[] aesbytes = AESForC.ECBEncrypt(data.getBytes(), key.getBytes());
		//base64编码
		String encodeStr = new String(Base64.encode(aesbytes, Base64.NO_WRAP));
		Log.i("AESForC", "encodeStr:"+encodeStr);
		return encodeStr;
	}
}