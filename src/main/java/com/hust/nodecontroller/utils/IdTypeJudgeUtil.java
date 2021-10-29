package com.hust.nodecontroller.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.communication.BlockchainModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class IdTypeJudgeUtil {

    private static final Logger logger = LoggerFactory.getLogger(IdTypeJudgeUtil.class);


    public static int TypeJudge(String identification){
        if(identification.split("\\.").length == 4)
            return 1;
        else if(identification.split("\\.").length == 3)
            return 2;
        else if(identification.startsWith("10064") || identification.startsWith("10096") || identification.startsWith("20128") || identification.startsWith("300121"))
            return 3;
        else if(identification.contains("/") && identification.split("\\/").length ==2){
            String[] tmp = identification.split("\\/");
            if (tmp[0].contains(".") && tmp[0].split("\\.").length == 3 && tmp[1].contains(".") && tmp[1].split("\\.").length == 5){
                String[] prefix = tmp[0].split("\\.");
                String[] subfix = tmp[1].split("\\.");
                if(prefix[0].length() == 3 && prefix[1].length() == 3 && prefix[2].length() == 6 && subfix[0].length() == 2 && subfix[1].length() == 2 && subfix[2].length() == 2 && subfix[3].length() == 8 && subfix[4].length() == 6)
                    return 4;
            }
        }
        return 0;
    }

    public static JSONObject handleResolve(String id) throws JSONException {
        URL url = null;
        HttpURLConnection httpConn = null;
        BufferedReader in = null;
        StringBuffer buffer = new StringBuffer();
        try{
            url = new URL("http://hdl.handle.net/api/handles/"+id);
            in = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8") );
            String str = null;
            while((str = in.readLine()) != null) {
                buffer.append( str );
            }
        } catch (Exception ex) {
        } finally{
            try{
                if(in!=null) {
                    in.close();
                }
            }catch(IOException ex) {
            }
        }
        String data = buffer.toString();
        JSONObject handle = JSONObject.parseObject(data);
        JSONArray values=handle.getJSONArray("values");
        int handlelen= values.size();
        JSONObject result = new JSONObject();
        for(int i=0;i<handlelen;i++)
        {
            JSONObject tmp = new JSONObject();
            tmp.put("type",values.getJSONObject(i).get("type").toString());
            tmp.put("value",values.getJSONObject(i).getJSONObject("data").get("value").toString());
            tmp.put("timestamp",values.getJSONObject(i).get("timestamp").toString());
            result.put(String.valueOf(i),tmp);
        }
        return result;
    }

    public static JSONObject ecodeResolve(String id){
        Process proc;
        String[] title={" 'Ecode编码：\\u3000'"," '产品名称：\\u3000'"," '型号名称：\\u3000'"," '企业名称：\\u3000'"," '回传时间：\\u3000'"};
        String[] value=new String[title.length];
        try {

            proc = Runtime.getRuntime().exec("python /root/hust/Ecode.py " + id);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            line = in.readLine();
            String[] ecode=line.split(",");
            for(int i=0;i< 12;i++)
            {
                for(int j=0;j<title.length;j++)
                {
                    if(ecode[i+8].equals(title[j]))
                    {
                        value[j]=ecode[i+9];
                        break;
                    }
                }
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("encoding",value[0]);
        jsonObject.put("name",value[1]);
        jsonObject.put("model",value[2]);
        jsonObject.put("company",value[3]);
        jsonObject.put("registrationDate",value[4]);
        return jsonObject;
    }

    public static JSONObject oidResolve(String id){
        String[] title = {"站点：", "数字OID：", "中文OID：", "英文OID：", "应用范围：", "申请机构中文名：", "申请机构英文名：", "申请机构中文地址：", "申请机构英文地址：", "申请机构网址：", "申请机构邮编：", "申请机构传真："};
        String[] value = new String[title.length];
        try {
//            File file=new File("D:\\OID.py");    //定位脚本文件所在位置

            Process proc = Runtime.getRuntime().exec("python /root/hust/OID.py " + id);

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = null;
            line = in.readLine();

            int index = line.indexOf("]");
            String lines = line.substring(2, index - 1);
            String[] oid = lines.split("', '|', \"|\", '");
            for (int i = 0; i < oid.length - 1; i++)//-1忽略title数组对oid数组尾项内容的匹配判断以解决数组越界问题（不管最后一项是title值还是value值，都是可以忽略title数组对oid数组尾项内容的匹配判断而不影响结果）
            {
                for (int j = 0; j < title.length; j++) {
                    if ((oid[i].equals(title[j])) && (!oid[i + 1].contains("：")))//项匹配且oid下一项是我们想要的内容值
                    {
                        value[j] = oid[i + 1];
                        break;
                    }
                }
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("site",value[0]);
        jsonObject.put("numberOID",value[1]);
        jsonObject.put("chineseOID",value[2]);
        jsonObject.put("englishOID",value[3]);
        jsonObject.put("application",value[4]);
        jsonObject.put("chinesename",value[5]);
        jsonObject.put("englishname",value[6]);
        jsonObject.put("chineseaddress",value[7]);
        jsonObject.put("englishaddress",value[8]);
        jsonObject.put("website",value[9]);
        jsonObject.put("zipcode",value[10]);
        jsonObject.put("fax",value[11]);
        return jsonObject;
    }
}
