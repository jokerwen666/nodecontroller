package com.hust.nodecontroller.utils;

import com.alibaba.fastjson.JSONArray;
import com.hust.nodecontroller.infostruct.*;
import com.alibaba.fastjson.JSONObject;
import org.apache.zookeeper.data.Id;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.swing.plaf.basic.BasicButtonUI;
import java.util.*;

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
        response.setPermission(dataJson.getString("permisssion"));
        response.setMessage(resJson.getString("message"));
        response.setStatus(resJson.getIntValue("status"));

        return response;
    }


    public static NormalMsg getOwnerQueryResponse(String url, JSONObject json) throws Exception {
        JSONObject resJson = SendPostPacket(url, json);

        NormalMsg normalMsg = new NormalMsg();

        if (resJson.getIntValue("status") == 0) {
            normalMsg.setStatus(resJson.getIntValue("status"));
            normalMsg.setMessage(resJson.getString("message"));
            return normalMsg;
        }

        JSONArray jsonArray = resJson.getJSONArray("data");
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        JSONObject jsonRecord = jsonObject.getJSONObject("Record");
        String owner = jsonRecord.getString("onwer");

        normalMsg.setStatus(1);
        normalMsg.setMessage(owner);

        return normalMsg;

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

//        if(!resJson.getBoolean("IsSuccess")){
//            response.setMessage(resJson.getString("Message"));
//            response.setStatus(0);
//            return response;
//        }

        JSONObject dataJson = resJson.getJSONObject("Data");
        if (dataJson == null) {
            dataJson = resJson;
        }

        response.setStatus(1);
        response.setMessage("Query CompanyInfo Success!");
        response.setInformation(dataJson);
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
            String latitude = dhtNodeInfo.getString("latitude");
            String longitude = dhtNodeInfo.getString("longtitude");
            String city = dhtNodeInfo.getString("city");
            int nodeNums = dhtNode.getIntValue("idNums");

            dhtNode.put("nodeID",Integer.parseInt(dhtNodeID));
            dhtNode.put("latitude",latitude);
            dhtNode.put("longitude",longitude);
            dhtNode.put("city",city);
            dhtNode.put("nodeNums",nodeNums);

            nodeList.add(dhtNode);
            nodeCount++;

        }

        response.setNodeCount(nodeCount);
        response.setDomainID(dataJson.getString("domainID"));
        response.setBoundaryID(dataJson.getIntValue("bNodeID"));
        response.setMessage("全部DHT节点信息查询成功！");

        response.setNodeList(nodeList);
        response.setStatus(1);
        return response;
    }

    public static SystemTotalState getSystemTotalState(String url, JSONObject json) throws Exception {
        JSONObject resJson = SendPostPacket(url, json);
        SystemTotalState response = new SystemTotalState();

        if (resJson.getIntValue("status") == 0) {
            response.setMessage(resJson.getString("message"));
            response.setStatus(resJson.getIntValue("status"));
            return response;
        }

        JSONObject dataJson = resJson.getJSONObject("message");

        String jsonStr = dataJson.getString("feedback");

        JSONObject nodeListJson = JSONObject.parseObject(jsonStr);

        List<JSONObject> nodeList = new ArrayList<JSONObject>();
        int nodeCount = nodeListJson.size();

        response.setTotalNodeCount(nodeCount);
        response.setSystemQueryTimeout(CalStateUtil.queryTimeout / CalStateUtil.queryCount);
        response.setMessage("系统统计信息查询成功！");

        response.setStatus(1);
        return response;
    }

    /**
     * @Description : 向指定url发送json数据，返回DhtNodeInfo数据，用于查询本节点的DHT信息
     * @author : Zhang Bowen
     * @date : 2020.10.30 18:06
     * @param url : 目的服务器url
     * @param json : 要发送的json数据
     * @return : com.hust.nodecontroller.infostruct.DhtNodeInfo
     */
    public static DhtNodeInfo getOwnNodeInfo(String url, JSONObject json){
        JSONObject resJson;
        DhtNodeInfo response = new DhtNodeInfo();
        try {
            resJson = SendPostPacket(url, json);
        }catch (Exception e)
        {
            response.setStatus(0);
            response.setMessage("Dht not ready!");
            return response;
        }

        if (resJson.getIntValue("status") == 0) {
            response.setMessage(resJson.getString("message"));
            response.setStatus(resJson.getIntValue("status"));
            return response;
        }

        JSONObject dataJson = resJson.getJSONObject("message");
        response.setDomainName("domain"+dataJson.getString("domainID"));
        response.setCity(dataJson.getString("city"));
        response.setProvince(dataJson.getString("province"));
        response.setEnterprise(dataJson.getString("enterprise"));
        response.setOrgName(dataJson.getString("orgName"));
        response.setIdentityNum(dataJson.getString("idNums"));
        response.setNodeID(dataJson.getString("nodeID"));
        response.setLatitude(dataJson.getString("latitude"));
        response.setLongtitude(dataJson.getString("longtitude"));
        response.setMessage("自身DHT节点信息查询成功！");
        response.setStatus(1);
        return response;
    }


    public static SinglePageInfo getSinglePage(String url, String prefix, String matchString, String txid, int totalCount) throws Exception {
        JSONObject jsonToBC = new JSONObject();
        jsonToBC.put("prefix", prefix);
        jsonToBC.put("pageNum", txid);
        jsonToBC.put("pageSize", 10);
        jsonToBC.put("peer_name", "peer1");
        JSONObject resJson = SendPostPacket(url, jsonToBC);
        SinglePageInfo response = new SinglePageInfo();
        List<JSONObject> identityList = new ArrayList<>();

        // 当查询信息报错时，直接返回查询错误
        if (resJson.getIntValue("status") == 0) {
            response.setMessage(resJson.getString("message"));
            response.setStatus(resJson.getIntValue("status"));
            return response;
        }
        JSONArray jsonArray = resJson.getJSONArray("data");
        int pageCount = jsonArray.size();

        // 获取翻页id
        JSONArray recordArray = resJson.getJSONArray("ResponseMetadata");
        String bookmark = recordArray.getJSONObject(0).getJSONObject("ResponseMetadata").getString("Bookmark");

        // 该页数据保存
        for (int i = 0; i < pageCount; i++){
            JSONObject job = jsonArray.getJSONObject(i); //job为每一个具体的标识查询信息
            JSONObject identityData = new JSONObject(); //identityData为存储当前查询标识的json数据

            String identity = job.getString("Key"); //从job中获取标识
            if (!identity.contains(matchString)) {
                continue;
            }

            JSONObject idData = job.getJSONObject("Record"); //从job中获取标识对应的记录
            String urlHash = idData.getString("abstract"); //从记录中获取url哈希
            String goodsHash = idData.getString("mappingData_hash"); //从记录中获取产品信息哈希
            String permission = idData.getString("permisssion");
            if (permission.equals("1")) {
                permission = "all";
            } else {
                permission = "only";
            }

            identityData.put("identity", identity);
            identityData.put("urlHash", urlHash);
            identityData.put("goodsHash", goodsHash);
            identityData.put("queryAuthority",permission);
            identityList.add(identityData);
        }

        response.setStatus(1);
        response.setMessage("Query Single Page Success!");
        response.setCount(totalCount);
        response.setIdentityList(identityList);
        response.setTxid(bookmark);
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
    public static IdentityInfo getAllByPrefix(String url, String prefix, String matchString) throws Exception{
        //设置初值
        int pageNum = 0; //查询页面序号
        int pageQueryCount = 0; //当前页面查询返回标识总数
        int totalQueryCount = 0; //所有页面查询返回标识总数
        String bookmark = ""; //页面标记（用于翻页使用）

        List<JSONObject> data = new ArrayList<>();
        IdentityInfo response = new IdentityInfo();
        List<JSONObject> identityList = new ArrayList<>();

        // 每次获取1000个数据，直到获取不到
        while(true){
            JSONObject jsonToBC = new JSONObject();
            jsonToBC.put("prefix",prefix);
            jsonToBC.put("pageNum",bookmark);
            jsonToBC.put("pageSize",1000);
            jsonToBC.put("peer_name","peer1");
            JSONObject resJson = SendPostPacket(url,jsonToBC);

            //当查询信息报错时，直接返回查询错误
            if (resJson.getIntValue("status") == 0) {
                response.setMessage(resJson.getString("message"));
                response.setStatus(resJson.getIntValue("status"));
                return response;
            }

            //data为空时，说明查找到的标识数为0，跳出循环
            JSONArray jsonArray = resJson.getJSONArray("data");
            pageQueryCount = jsonArray.size();
            if (pageQueryCount == 0) {
                break;
            }

            //获取页面标记，用于翻页
            JSONArray recordArray = resJson.getJSONArray("ResponseMetadata");
            bookmark = recordArray.getJSONObject(0).getJSONObject("ResponseMetadata").getString("Bookmark");

            JSONObject pageJson = new JSONObject();

            //在该页中遍历每个查询返回标识信息,满10个作为一页
            for (int i = 0; i < pageQueryCount; i++){
                JSONObject job = jsonArray.getJSONObject(i); //job为每一个具体的标识查询信息
                JSONObject identityData = new JSONObject(); //identityData为存储当前查询标识的json数据

                String identity = job.getString("Key"); //从job中获取标识
                if (!identity.contains(matchString)) {
                    continue;
                }

                JSONObject idData = job.getJSONObject("Record"); //从job中获取标识对应的记录
                String urlHash = idData.getString("abstract"); //从记录中获取url哈希
                String goodsHash = idData.getString("mappingData_hash"); //从记录中获取产品信息哈希
                String permission = idData.getString("permisssion");
                if (permission.equals("1")) {
                    permission = "all";
                } else {
                    permission = "only";
                }

                identityData.put("identity", identity);
                identityData.put("urlHash", urlHash);
                identityData.put("goodsHash", goodsHash);
                identityData.put("queryAuthority",permission);
                identityList.add(identityData);


                if (identityList.size() == 10) {
                    // data不为空时，将页面数+1
                    pageNum++;
                    pageJson.put("pageID", pageNum);
                    pageJson.put("identityList", new ArrayList<JSONObject>(identityList));
                    data.add(pageJson);
                    identityList.clear();
                    totalQueryCount = totalQueryCount + 10;
                }
            }
        }

        if (identityList.size() > 0) {
            pageNum++;
            JSONObject lastPage = new JSONObject();
            lastPage.put("pageID", pageNum);
            lastPage.put("identityList", new ArrayList<JSONObject>(identityList));
            totalQueryCount = totalQueryCount + identityList.size();
            data.add(lastPage);
        }

        response.setStatus(1);
        response.setMessage("Query by Prefix Success!");
        response.setTotalCount(totalQueryCount);
        response.setPageCount(pageNum);
        response.setData(data);

        return response;
    }


    public static IdentityRankInfo queryIdRankByPrefix(String url, JSONObject json) throws Exception {
        JSONObject resJSON = SendPostPacket(url,json);
        IdentityRankInfo idRankInfo = new IdentityRankInfo();
        List<JSONObject> idList = new LinkedList<>();

        if (resJSON.getIntValue("status") == 0)
            throw new Exception("获取企业节点标识排行失败！");

        JSONArray jsonArray = resJSON.getJSONArray("identityList");
        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            JSONObject job = jsonArray.getJSONObject(i);
            JSONObject idInfo = new JSONObject();
            idInfo.put("identity", job.getString("identifier"));
            idInfo.put("goodsInfoUrl", job.getString("mappingData"));
            idInfo.put("resolveNums", job.getString("resolveNums"));
            idInfo.put("rankNum", i+1);
            idList.add(idInfo);
        }

        idRankInfo.setIdList(idList);
        idRankInfo.setIdNums(size);
        idRankInfo.setStatus(1);
        idRankInfo.setMessage("获取企业节点标识排行成功！");

        return idRankInfo;
    }


    public static BulkInfo getBulkQueryInfo(String url, JSONObject json) throws Exception{
        JSONArray resJson = SendPostPacket_(url, json);
        int jsonCount = resJson.size();
        BulkInfo bulkInfo = new BulkInfo();
        JSONArray data = new JSONArray();
        JSONObject idJson = new JSONObject();

        for (int i = 0; i < jsonCount - 1; i++){
            JSONObject idData = new JSONObject();
            idJson = resJson.getJSONObject(i);
            if (idJson.getIntValue("status") == 0)
                idData.put("message", idJson.getString("wrongInformation"));
            else{
                idData.put("identity", idJson.getJSONObject("message").getString("identity"));
                idData.put("url", idJson.getJSONObject("message").getString("mappingData"));
                idData.put("message","标识查询成功！");
            }
            data.add(idData);
        }
        idJson = resJson.getJSONObject(jsonCount-1);

        bulkInfo.setData(data);
        bulkInfo.setStatus(1);
        bulkInfo.setMessage("批量查询操作执行完成！");
        bulkInfo.setBeginTime(idJson.getString("startTime"));
        bulkInfo.setEndTime(idJson.getString("endTime"));
        bulkInfo.setCostTime(idJson.getString("costTime"));
        bulkInfo.setRate(idJson.getString("qps"));

        return bulkInfo;
    }

    public static BulkInfo getBulkRegisterInfo(String url, JSONObject json) throws Exception{
        JSONArray resJson = SendPostPacket_(url, json);
        int jsonCount = resJson.size();
        BulkInfo bulkInfo = new BulkInfo();
        JSONArray data = new JSONArray();
        JSONObject idJson = new JSONObject();

        for (int i = 0; i < jsonCount - 1; i++){
            JSONObject idData = new JSONObject();
            idJson = resJson.getJSONObject(i);
            if (idJson.getIntValue("status") == 0)
                idData.put("message", idJson.getString("wrongInformation"));
            else{
                idData.put("message","标识注册成功！");
            }
            data.add(idData);
        }
        idJson = resJson.getJSONObject(jsonCount-1);

        bulkInfo.setData(data);
        bulkInfo.setStatus(1);
        bulkInfo.setMessage("批量注册操作执行完成！");
        bulkInfo.setBeginTime(idJson.getString("startTime"));
        bulkInfo.setEndTime(idJson.getString("endTime"));
        bulkInfo.setCostTime(idJson.getString("costTime"));
        bulkInfo.setRate(idJson.getString("qps"));

        return bulkInfo;
    }

    /**
     * @Description : 设定向指定url发送json数据
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

    private static JSONArray SendPostPacket_(String url, JSONObject json) throws Exception{


        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");

        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        HttpEntity<String> requestEntity = new HttpEntity<String>(json.toString(), headers);

        String res = client.exchange(url, method, requestEntity, String.class).getBody();
        return JSONArray.parseArray(res);
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

    public static int QueryNodeIdTotal(String url, JSONObject json) throws Exception{
        JSONObject resJson = SendPostPacket(url, json);

        /*
          根据北邮的提供的findAllId json格式添加json解析，提取出标识总数
         */
        return Integer.parseInt(resJson.getJSONObject("message").getString("idNums"));
    }
}
