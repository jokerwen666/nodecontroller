package com.hust.nodecontroller.utils;

import com.alibaba.fastjson.JSONArray;
import com.hust.nodecontroller.infostruct.*;
import com.alibaba.fastjson.JSONObject;
import org.apache.zookeeper.data.Id;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhang Bowen
 * @Description
 *  该类属于辅助工具类
 *  用于封装post请求
 *
 * @ClassName PostRequestUtil
 * @date 2020.09.13 17:45
 */
public class PostRequestUtil {


    /**
     * @Description : 向目的url发送json数据，返回normalmsg类型
     * @author : Zhang Bowen
     * @date :  2020.10.12 17:12
     * @param url : 目的服务器url
     * @param json : 要发送的json数据
     * @return : com.hust.nodecontroller.infostruct.NormalMsg
     */
    public static NormalMsg getNormalResponse(String url, JSONObject json) throws Exception {

        JSONObject resJson = SendPostPacket(url, json);
        NormalMsg response = new NormalMsg();
        response.setMessage(resJson.getString("message"));
        response.setStatus(resJson.getIntValue("status"));
        return response;
    }

    public static NormalMsg getNormalResponse_(String url, JSONObject json) throws Exception {

        JSONObject resJson = SendPostPacket(url, json);
        NormalMsg response = new NormalMsg();
        response.setStatus(resJson.getIntValue("status"));
        if (resJson.getIntValue("status") == 0)
            response.setMessage(resJson.getString("wrongInformation"));
        else
            response.setMessage(resJson.getString("message"));
        return response;
    }


    /**
     * @Description : 向指定url发送json数据，返回amsysteminfo数据，用于返回鉴权子系统查询结果
     * @author : Zhang Bowen
     * @date : 2020.10.12 17:13
     * @param url : 目的服务器url
     * @param json : 要发送的url
     * @return : com.hust.nodecontroller.infostruct.AMSystemInfo
     */
    public static AMSystemInfo getAMQueryResponse(String url, JSONObject json) throws Exception {
        JSONObject resJson = SendPostPacket(url, json);
        AMSystemInfo response = new AMSystemInfo();

        if (resJson.getIntValue("status") == 0) {
            response.setMessage(resJson.getString("message"));
            response.setStatus(resJson.getIntValue("status"));
            return response;
        }

        JSONObject dataJson = resJson.getJSONObject("data");
        response.setPrefix(dataJson.getString("prefix"));
        response.setAuthority(dataJson.getString("authority"));
        response.setOwner(dataJson.getString("onwer"));
        response.setOrg_name(dataJson.getString("org_name"));
        response.setKey(dataJson.getString("key"));
        response.setMessage(resJson.getString("message"));
        response.setStatus(resJson.getIntValue("status"));

        return response;
    }


    /**
     * @Description : 向指定url发送json数据，返回imsysteminfo数据，用于返回标识管理子系统查询结果
     * @author : Zhang Bowen
     * @date : 2020.10.12 19:47
     * @param url : 目的服务器url
     * @param json : 要发送的json数据
     * @return : com.hust.nodecontroller.infostruct.IMSystemInfo
     */
    public static IMSystemInfo getIMQueryResponse(String url, JSONObject json) throws Exception {
        JSONObject resJson = SendPostPacket(url, json);
        IMSystemInfo response = new IMSystemInfo();

        if (resJson.getIntValue("status") == 0) {
            response.setMessage(resJson.getString("wrongInformation"));
            response.setStatus(resJson.getIntValue("status"));
            return response;
        }

        JSONObject dataJson = resJson.getJSONObject("message");
        response.setStatus(resJson.getIntValue("status"));
        response.setMessage(dataJson.getString("feedback"));
        response.setMappingData(resJson.getString("url"));
        response.setNodeID("node" + dataJson.getString("nodeID"));

        return response;
    }

    /**
     * @Description : 向指定url发送json数据，返回rvsysteminfo数据，用于返回解析结果验证子系统查询结果
     * @author : Zhang Bowen
     * @date : 2020.10.12 19:50
     * @param url : 目的服务器url
     * @param json : 要发送的json数据
     * @return : com.hust.nodecontroller.infostruct.RVSystemInfo
     */
    public static RVSystemInfo getRVQueryResponse(String url, JSONObject json) throws Exception {
        JSONObject resJson = SendPostPacket(url, json);
        RVSystemInfo response = new RVSystemInfo();

        if (resJson.getIntValue("status") == 0) {
            response.setMessage(resJson.getString("message"));
            response.setStatus(resJson.getIntValue("status"));
            return response;
        }

        JSONObject dataJson = resJson.getJSONObject("data");
        response.setUrlHash(dataJson.getString("abstract"));
        response.setMappingDataHash(dataJson.getString("mappingData_hash"));
        response.setMessage(resJson.getString("message"));
        response.setStatus(resJson.getIntValue("status"));

        return response;
    }

    /**
     * @Description : 向指定url发送json数据，返回ComQueryInfo数据，用于根据url查询对应产品的产品信息
     * @author : Zhang Bowen
     * @date : 2020.10.31 21:46
     * @param url : 目的服务器url
     * @return : com.hust.nodecontroller.infostruct.ComQueryInfo
     */
    public static ComQueryInfo getComQueryInfo(String url) throws Exception{
        JSONObject resJson = SendGetPacket(url);
        ComQueryInfo response = new ComQueryInfo();

        if(!resJson.getBoolean("IsSuccess")){
            response.setMessage(resJson.getString("Message"));
            response.setStatus(0);
            return response;
        }

        JSONObject dataJson = resJson.getJSONObject("Data");

        response.setStatus(1);
        response.setMessage("Query CompanyInfo Success!");
        response.setInformation(dataJson.toString());
        response.setJsonStr(resJson.toString());

        return response;
    }

    /**
     * @Description : 向指定url发送json数据，返回NodeState数据，用于查询所有的dht解析结点状态
     * @author : Zhang Bowen
     * @date : 2020.10.30 18:06
     * @param url : 目的服务器url
     * @param json : 要发送的json数据
     * @return : com.hust.nodecontroller.infostruct.NodeState
     */
    public static NodeState getAllNodeState(String url, JSONObject json) throws Exception{
        JSONObject resJson = SendPostPacket(url, json);
        NodeState response = new NodeState();

        if (resJson.getIntValue("status") == 0) {
            response.setMessage(resJson.getString("message"));
            response.setStatus(resJson.getIntValue("status"));
            return response;
        }

        JSONObject dataJson = resJson.getJSONObject("message");

        String jsonStr = dataJson.getString("feedback");

        JSONObject nodeListJson = JSONObject.parseObject(jsonStr);

        int nodeCount = 0;
        List<JSONObject> nodeList = new ArrayList<JSONObject>();

        for (Map.Entry<String,Object> entry : nodeListJson.entrySet()) {
            JSONObject dhtNode = new JSONObject();

            String dhtNodeID = entry.getKey();
            dhtNodeID = dhtNodeID.replace("node","");

            JSONObject dhtNodeInfo = (JSONObject) entry.getValue();
            String latitude = dhtNodeInfo.getString("Latitude");
            String longitude = dhtNodeInfo.getString("Longtitude");
            String city = dhtNodeInfo.getString("City");
            int nodeNums = dhtNode.getIntValue("NodeNums");

            dhtNode.put("nodeID",Integer.parseInt(dhtNodeID));
            dhtNode.put("latitude",latitude);
            dhtNode.put("longitude",longitude);
            dhtNode.put("city",city);
            dhtNode.put("nodeNums",nodeNums);

            nodeList.add(dhtNode);
            nodeCount++;

        }

        response.setNodeCount(nodeCount);
        response.setDomainID(dataJson.getIntValue("domainID"));
        response.setBoundaryID(dataJson.getIntValue("bNodeID"));
        response.setMessage("全部DHT节点信息查询成功！");

        response.setNodeList(nodeList);
        response.setStatus(1);
        return response;
    }

    /**
     * @Description : 向指定url发送json数据，返回IdentityInfo数据，用于查询某企业前缀下所有的标识信息
     * @author : Zhang Bowen
     * @date : 2020.10.30 18:18
     * @param url : 目的服务器url
     * @param prefix : 企业前缀
     * @return : com.hust.nodecontroller.infostruct.IdentityInfo
     */
    public static IdentityInfo getAllByPrefix(String url, String prefix) throws Exception{
        //设置初值
        int pageNum = 1; //查询页面序号
        int pageQueryCount = 0; //当前页面查询返回标识总数
        int totalQueryCount = 0; //所有页面查询返回标识总数
        String bookmark = ""; //页面标记（用于翻页使用）

        List<JSONObject> data = new ArrayList<>();
        IdentityInfo response = new IdentityInfo();

        //当检索到第一页或者当前页查询总数不为0是进入循环
        while(true){
            JSONObject jsonToBC = new JSONObject();
            jsonToBC.put("prefix",prefix);
            jsonToBC.put("pageNum",bookmark);
            jsonToBC.put("pageSize",10);
            jsonToBC.put("peer_name","peer1");

            JSONObject resJson = SendPostPacket(url,jsonToBC);

            //当查询信息报错时，直接返回查询错误
            if (resJson.getIntValue("status") == 0) {
                response.setMessage(resJson.getString("message"));
                response.setStatus(resJson.getIntValue("status"));
                return response;
            }

            JSONArray jsonArray = resJson.getJSONArray("data");
            //data为空时，说明查找到的标识数为0，跳出循环
            pageQueryCount = jsonArray.size();
            if (pageQueryCount == 0) break;

            JSONArray recordArray = resJson.getJSONArray("ResponseMetadata");
            //获取页面标记，用于翻页
            bookmark = recordArray.getJSONObject(0).getJSONObject("ResponseMetadata").getString("Bookmark");

            JSONObject pageJson = new JSONObject();
            List<JSONObject> identityList = new ArrayList<>();

            //在该页中遍历每个查询返回标识信息
            for (int i = 0; i < pageQueryCount; i++){

                JSONObject identityData = new JSONObject(); //identityData为存储当前查询标识的json数据

                JSONObject job = jsonArray.getJSONObject(i); //job为每一个具体的标识查询信息

                String identity = job.getString("Key"); //从job中获取标识

                JSONObject idData = job.getJSONObject("Record"); //从job中获取标识对应的记录
                String urlHash = idData.getString("abstract"); //从记录中获取url哈希
                String goodsHash = idData.getString("mappingData_hash"); //从记录中获取产品信息哈希

                identityData.put("identity", identity);
                identityData.put("urlHash", urlHash);
                identityData.put("goodsHash", goodsHash);

                identityList.add(identityData);
            }

            pageJson.put("pageID",pageNum);
            pageJson.put("identityList",identityList);
            data.add(pageJson);
            totalQueryCount = totalQueryCount + pageQueryCount;

            //如果当前页的查询总数小于10条，说明下一页必定没有内容，直接跳出循环
            if (pageQueryCount < 10)
                break;
            else
                pageNum++;
        }

        response.setStatus(1);
        response.setMessage("Query by Prefix Success!");
        response.setTotalCount(totalQueryCount);
        response.setPageCount(pageNum);
        response.setData(data);

        return response;
    }


    /**
     * @Description : 设定发送向指定url发送json数据
     * @author : Zhang Bowen
     * @date : 2020.10.12 19:51
     * @param url :
     * @param json :
     * @return : com.alibaba.fastjson.JSONObject
     */
    private static JSONObject SendPostPacket(String url, JSONObject json) throws Exception{


        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");

        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        HttpEntity<String> requestEntity = new HttpEntity<String>(json.toString(), headers);

        String res = client.exchange(url, method, requestEntity, String.class).getBody();
        return JSONObject.parseObject(res);
    }

    private static JSONObject SendGetPacket(String url) throws Exception {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.GET;
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");

        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(headers);

        return client.exchange(url, method, requestEntity, JSONObject.class).getBody();
    }
}
