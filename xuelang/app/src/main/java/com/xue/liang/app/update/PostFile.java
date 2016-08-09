package com.xue.liang.app.update;
 
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class PostFile {
       static String BOUNDARY = java.util.UUID.randomUUID().toString();
       static  String PREFIX = "--";
       static String  LINEND = "\r\n";
       static String MULTIPART_FROM_DATA = "multipart/form-data";
       static String CHARSET = "UTF-8"; 
       static String result =null;
       static int SO_TIMEOUT=5*1000;
       
      //上传代码，第一个参数，为要使用的URL，第二个参数，为表单内容，第三个参数为要上传的文件，可以上传多个文件，这根据需要页定
    public static String post(String actionUrl, Map<String, String> params,
            Map<String, File> files) throws IOException {
         try{
             URL url =new URL(actionUrl);
             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
             conn.setReadTimeout(SO_TIMEOUT);
             conn.setConnectTimeout(SO_TIMEOUT);
             conn.setDoInput(true);// 允许输入流
             conn.setDoOutput(true);// 允许输出流
             conn.setUseCaches(false);// 不允许使用缓存
             conn.setRequestMethod("POST");// 请求方式
             conn.setRequestProperty("Charset",CHARSET);// 设置编码
             conn.setRequestProperty("connection","keep-alive");
             conn.setRequestProperty("Charsert", CHARSET);
             conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA +";boundary="+ BOUNDARY);
             /**
              * 当文件不为空，把文件包装并且上传
              */
              DataOutputStream dos =new DataOutputStream(
                            conn.getOutputStream());
              StringBuffer sb =new StringBuffer();
              for(Map.Entry<String, String> entry : params.entrySet()) {
                        sb.append(PREFIX);
                        sb.append(BOUNDARY);
                        sb.append(LINEND);
                        sb.append("Content-Disposition: form-data; name=\""+ entry.getKey() +"\""+ LINEND);
                        sb.append("Content-Type: application/octet-stream; charset="+CHARSET+ LINEND);
                        sb.append(LINEND);
                        sb.append(entry.getValue());
                        sb.append(LINEND);
                   }
                   if(files!=null){
                   for (Map.Entry<String, File> file : files.entrySet()) {
                       sb.append(PREFIX);
                       sb.append(BOUNDARY);
                       sb.append(LINEND);
                       /**
                        * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                        * filename是文件的名字，包含后缀名的 比如:abc.png
                        */
                       sb.append("Content-Disposition: form-data; name=\""+file.getKey()+"\"; filename=\""+ file.getValue() +"\""+ LINEND);
                       sb.append("Content-Type: application/octet-stream; charset="+CHARSET+ LINEND);
                       sb.append(LINEND);
    
                       dos.write(sb.toString().getBytes());
                       InputStream is =new FileInputStream(file.getValue());
                       byte[] bytes =new byte[1024];
                       int len = 0;
                       while((len = is.read(bytes)) != -1) {
                            dos.write(bytes, 0, len);
                       }
                       is.close();
                       dos.write(LINEND.getBytes());
                   }
                   byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes(); 
                   dos.write(end_data);
                   dos.flush();
                   /**
                    * 获取响应码 200=成功 当响应成功，获取响应的流
                    */
                   int res= conn.getResponseCode();
                   
                   InputStream input = conn.getInputStream();
                   StringBuffer sb1 =new StringBuffer();
                   int ss;
                   while((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                   }
                   result = sb1.toString();
             }
        }catch(MalformedURLException e) {
             e.printStackTrace();
        }catch(IOException e) {
             e.printStackTrace();
        }
        return result;
    }
    
    
    /**
     * 上传单张图片加参数
     *@paramfile 文件
     *@paramfileName 服务器接口图片参数名称
     *@paramRequestURL 上传URL
     *@paramparams  参数
     *@return
     */
    public static String PostData(File file,String fileName, String RequestURL,Map<String, String> params) {
     
         try{
              URL url =new URL(RequestURL);
              HttpURLConnection conn = (HttpURLConnection) url.openConnection();
              conn.setReadTimeout(SO_TIMEOUT);
              conn.setConnectTimeout(SO_TIMEOUT);
              conn.setDoInput(true);// 允许输入流
              conn.setDoOutput(true);// 允许输出流
              conn.setUseCaches(false);// 不允许使用缓存
              conn.setRequestMethod("POST");// 请求方式
              conn.setRequestProperty("Charset",CHARSET);// 设置编码
              conn.setRequestProperty("connection","keep-alive");
              conn.setRequestProperty("Charsert", CHARSET);
              conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA +";boundary="+ BOUNDARY);

              if(file !=null) {
                    /**
                     * 当文件不为空，把文件包装并且上传
                     */
                    DataOutputStream dos =new DataOutputStream(
                              conn.getOutputStream());
                    StringBuffer sb =new StringBuffer();

                    for(Map.Entry<String, String> entry : params.entrySet()) {
                         sb.append(PREFIX);
                         sb.append(BOUNDARY);
                         sb.append(LINEND);
                         sb.append("Content-Disposition: form-data; name=\""+ entry.getKey() +"\""+ LINEND);
                         sb.append("Content-Type: application/octet-stream; charset="+CHARSET+ LINEND);
                         sb.append(LINEND);
                         sb.append(entry.getValue());
                         sb.append(LINEND);
                    }

                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINEND);
                    /**
                     * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                     * filename是文件的名字，包含后缀名的 比如:abc.png
                     */
                    sb.append("Content-Disposition: form-data; name=\""+fileName+"\"; filename=\""+ file.getName() +"\""+ LINEND);
                    sb.append("Content-Type: application/octet-stream; charset="+CHARSET+ LINEND);
                    sb.append(LINEND);

                    dos.write(sb.toString().getBytes());
                    InputStream is =new FileInputStream(file);
                    byte[] bytes =new byte[1024];
                    int len = 0;
                    while((len = is.read(bytes)) != -1) {
                         dos.write(bytes, 0, len);
                    }
                    is.close();
                    dos.write(LINEND.getBytes());
                    byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND)
                              .getBytes();
                    dos.write(end_data);
                    dos.flush();
                    /**
                     * 获取响应码 200=成功 当响应成功，获取响应的流
                     */
                    int res= conn.getResponseCode();
                    
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 =new StringBuffer();
                    int ss;
                    while((ss = input.read()) != -1) {
                         sb1.append((char) ss);
                    }
                    result = sb1.toString();
              }
         }catch(MalformedURLException e) {
              e.printStackTrace();
         }catch(IOException e) {
              e.printStackTrace();
         }
         return result;
    }
}