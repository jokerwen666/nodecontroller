package com.hust.nodecontroller.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.enums.IdentityTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class IdTypeJudgeUtil {
    private static final Logger logger = LoggerFactory.getLogger(IdTypeJudgeUtil.class);
    private static final String[] OID_PREFIX = {"1.2.156", "2.13.156"};
    private static final String[] HANDLE_PREFIX = {"10","11","20","21","22","25","27","44","77","86"};
    private static final String[] ECODE_PREFIX = {"10064", "10096", "20128","300121"};
    private static final String[] DOMAIN_SUFFIX = {".biz", ".com", ".edu", ".gov", ".info", ".int" ,".mil", ".name", ".net", ".org", ".pro", ".xyz"};
    private static final String PATTERN_DHT = "086\\.[0-9]{3}\\.[0-9]{6}\\/[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{8}\\.[0-9]{6}";
    private static final Integer OID_PART_NUM = 4;
    private static final Integer HANDLE_PART_NUM = 3;
    private static final Integer ECODE_PART_NUM = 1;



    public static IdentityTypeEnum typeJudge(String identification){
        Integer length = identification.split("\\.").length;
        String firstPart = identification.split("\\.")[0];

        if (Pattern.matches(PATTERN_DHT, identification)) {
            return IdentityTypeEnum.IDENTITY_TYPE_DHT;
        }

        else if (length.equals(OID_PART_NUM) && isStartWithString(identification, OID_PREFIX)) {
            return IdentityTypeEnum.IDENTITY_TYPE_OID;
        }

        else if (length.equals(HANDLE_PART_NUM) && isHaveString(HANDLE_PREFIX, firstPart)) {
            return IdentityTypeEnum.IDENTITY_TYPE_HANDLE;
        }

        else if (length.equals(ECODE_PART_NUM) && isStartWithString(identification, ECODE_PREFIX)) {
            return IdentityTypeEnum.IDENTITY_TYPE_ECODE;
        }

        else if (isEndWithString(identification, DOMAIN_SUFFIX)) {
            return IdentityTypeEnum.IDENTITY_TYPE_DNS;
        }

        else {
            return IdentityTypeEnum.IDENTITY_TYPE_NOT_SUPPORT;
        }
    }

    public static String dnsResolve(String domain) throws Exception{
        CalStateUtil.dnsQueryCount++;
        JSONObject dns = new JSONObject();
        try {
            InetAddress[] ip = InetAddress.getAllByName(domain);
            for (int i = 0; i < ip.length; i++) {
                String key = "answer" + i;
                dns.put(key, ip[i].getHostAddress());
            }
            return EncDecUtil.sMEncrypt(dns.toString());
        } catch (Exception e) {
            throw new Exception("DNS解析失败");

        }
    }

    public static String handleResolve(String id) throws JSONException {
        CalStateUtil.handleQueryCount++;
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
        int valueSize= values.size();
        JSONObject result = new JSONObject();

        result.put("handle", handle.getString("handle"));

        for(int i = 0; i < valueSize; i++)
        {
            String type = values.getJSONObject(i).getString("type");
            String value = values.getJSONObject(i).getJSONObject("data").get("value").toString();
            result.put(type, value);
        }
        return EncDecUtil.sMEncrypt(result.toString());
    }

    public static String ecodeResolve(String id){
        CalStateUtil.ecodeQueryCount++;
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
        return EncDecUtil.sMEncrypt(jsonObject.toString());
    }


    public static String oidResolve(String id){
        CalStateUtil.oidQueryCount++;
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
        return EncDecUtil.sMEncrypt(jsonObject.toJSONString());
    }

    private static boolean isHaveString(String[] strArray, String a) {
        for (String s : strArray) {
            if (s.equals(a)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isStartWithString(String a, String[] strArray) {
        for (String s: strArray) {
            if (a.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEndWithString(String identification, String[] suffixes) {
        for (String suffix : suffixes) {
            if (identification.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

}
