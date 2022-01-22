package com.hust.nodecontroller.utils;

import com.alibaba.fastjson.JSONArray;
import com.hust.nodecontroller.exception.ControlSubSystemException;
import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.answerstruct.*;
import com.hust.nodecontroller.infostruct.answerstruct.QueryGoodsInfoAnswer;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @program nodecontroller
 * @Description HTTP请求封装类，当POST和GET请求调用出现异常时向上层调用函数继续抛出异常
 * @Author jokerwen666
 * @Date  2022-01-18 19:28
 **/
public class PostRequestUtil {
    public static NormalAnswer getBlockChainAnswer(String url, JSONObject json) throws ControlSubSystemException {
        JSONObject resJson = sendPostPacket(url, json);
        NormalAnswer response = new NormalAnswer();
        response.setMessage(resJson.getString("message"));
        response.setStatus(resJson.getIntValue("status"));

        if (response.getMessage() == null) {
            response.setMessage("区块链节点请求失败！");
        }

        return response;
    }

    public static NormalAnswer getDhtAnswer(String url, JSONObject json) throws ControlSubSystemException {
        JSONObject resJson = sendPostPacket(url, json);
        NormalAnswer response = new NormalAnswer();
        response.setStatus(resJson.getIntValue("status"));
        if (resJson.getIntValue("status") == 0) {
            response.setMessage(resJson.getString("wrongInformation"));
        } else {
            response.setMessage(resJson.getString("message"));
        }

        if (response.getMessage() == null) {
            response.setMessage("区块链节点请求失败！");
        }

        return response;
    }


    public static AuthorityManagementSystemAnswer getAuthorityManagementAnswer(String url, JSONObject json) throws ControlSubSystemException {
        JSONObject resJson = sendPostPacket(url, json);
        AuthorityManagementSystemAnswer response = new AuthorityManagementSystemAnswer();

        if (resJson.getIntValue("status") == 0) {
            response.setMessage(resJson.getString("message"));
            response.setStatus(resJson.getIntValue("status"));
            if (response.getMessage() == null) {
                response.setMessage("区块链节点（权限管理子系统）请求失败！");
            }
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

    public static IdentityManagementSystemAnswer getIdentityManagementAnswer(String url, JSONObject json) throws ControlSubSystemException {
        JSONObject resJson = sendPostPacket(url, json);
        IdentityManagementSystemAnswer response = new IdentityManagementSystemAnswer();

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

    public static ResultVerifySystemAnswer getResultVerifyAnswer(String url, JSONObject json) throws ControlSubSystemException {
        JSONObject resJson = sendPostPacket(url, json);
        ResultVerifySystemAnswer response = new ResultVerifySystemAnswer();

        if (resJson.getIntValue("status") == 0) {
            response.setMessage(resJson.getString("message"));
            response.setStatus(resJson.getIntValue("status"));
            if (response.getMessage() == null) {
                response.setMessage("区块链节点（结果校验子系统）请求失败！");
            }
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

    public static QueryGoodsInfoAnswer queryGoodsInfoAnswer(String url) throws ControlSubSystemException {
        JSONObject resJson = sendGetPacket(url);
        QueryGoodsInfoAnswer response = new QueryGoodsInfoAnswer();

        if(!resJson.getBoolean("IsSuccess")){
            response.setMessage(resJson.getString("Message"));
            response.setStatus(0);
            if (response.getMessage() == null) {
                response.setMessage("企业产品信息查询请求失败！");
            }
            return response;
        }

        JSONObject dataJson = resJson.getJSONObject("Data");

        response.setStatus(1);
        response.setMessage("Query CompanyInfo Success!");
        response.setInformation(dataJson);
        response.setJsonStr(resJson.toString());

        return response;
    }

    public static NormalAnswer queryIdentityOwnerAnswer(String url, JSONObject json) throws ControlSubSystemException {
        JSONObject resJson = sendPostPacket(url, json);
        NormalAnswer response = new NormalAnswer();

        if (resJson.getIntValue("status") == 0) {
            response.setStatus(resJson.getIntValue("status"));
            response.setMessage(resJson.getString("message"));
            if (response.getMessage() == null) {
                response.setMessage("查询产品标识所有者请求失败！");
            }
            return response;
        }

        JSONArray jsonArray = resJson.getJSONArray("data");
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        JSONObject jsonRecord = jsonObject.getJSONObject("Record");
        String owner = jsonRecord.getString("onwer");

        response.setStatus(1);
        response.setMessage(owner);

        return response;

    }

    public static GetDhtNodeInfoAnswer getOwnNodeInfo(String url, JSONObject json) throws ControlSubSystemException {
        JSONObject resJson;
        GetDhtNodeInfoAnswer response = new GetDhtNodeInfoAnswer();
        resJson = sendPostPacket(url, json);

        if (resJson.getIntValue("status") == 0) {
            response.setMessage(resJson.getString("message"));
            response.setStatus(resJson.getIntValue("status"));
            if (response.getMessage() == null) {
                response.setMessage("查询自身DHT节点信息请求失败！");
            }
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

    public static QueryAllByPrefixAnswer getAllByPrefix(String url, String prefix, String matchString) throws ControlSubSystemException {
        //查询页面序号
        int pageNum = 1;
        //当前页面查询返回标识总数
        int pageQueryCount = 0;
        //所有页面查询返回标识总数
        int totalQueryCount = 0;
        //页面标记（用于翻页使用）
        String bookmark = "";

        List<JSONObject> data = new ArrayList<>();
        QueryAllByPrefixAnswer response = new QueryAllByPrefixAnswer();

        //当检索到第一页或者当前页查询总数不为0是进入循环
        while(true){
            JSONObject jsonToBc = new JSONObject();
            jsonToBc.put("prefix",prefix);
            jsonToBc.put("pageNum",bookmark);
            jsonToBc.put("pageSize",10);
            jsonToBc.put("peer_name","peer0");

            JSONObject resJson = sendPostPacket(url,jsonToBc);

            //当查询信息报错时，直接返回查询错误
            if (resJson.getIntValue("status") == 0) {
                response.setMessage(resJson.getString("message"));
                response.setStatus(resJson.getIntValue("status"));
                if (response.getMessage() == null) {
                    response.setMessage("根据企业前缀查询标识请求失败！");
                }
                return response;
            }

            JSONArray jsonArray = resJson.getJSONArray("data");
            //data为空时，说明查找到的标识数为0，跳出循环
            pageQueryCount = jsonArray.size();
            if (pageQueryCount == 0) {
                break;
            }

            JSONArray recordArray = resJson.getJSONArray("ResponseMetadata");
            //获取页面标记，用于翻页
            bookmark = recordArray.getJSONObject(0).getJSONObject("ResponseMetadata").getString("Bookmark");

            JSONObject pageJson = new JSONObject();
            List<JSONObject> identityList = new ArrayList<>();

            //在该页中遍历每个查询返回标识信息
            for (int i = 0; i < pageQueryCount; i++){
                //identityData为存储当前查询标识的json数据
                JSONObject identityData = new JSONObject();
                //job为每一个具体的标识查询信息
                JSONObject job = jsonArray.getJSONObject(i);
                //从job中获取标识
                String identity = job.getString("Key");
                if (!identity.contains(matchString)) {
                    continue;
                }
                //从job中获取标识对应的记录
                JSONObject idData = job.getJSONObject("Record");
                //从记录中获取url哈希
                String urlHash = idData.getString("abstract");
                //从记录中获取产品信息哈希
                String goodsHash = idData.getString("mappingData_hash");
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

            pageJson.put("pageID",pageNum);
            pageJson.put("identityList",identityList);
            data.add(pageJson);
            totalQueryCount = totalQueryCount + pageQueryCount;

            //如果当前页的查询总数小于10条，说明下一页必定没有内容，直接跳出循环
            if (pageQueryCount < 10) {
                break;
            } else {
                pageNum++;
            }
        }

        response.setStatus(1);
        response.setMessage("Query by Prefix Success!");
        response.setTotalCount(totalQueryCount);
        response.setPageCount(pageNum);
        response.setData(data);

        return response;
    }


    public static QueryIdentityRankAnswer queryIdRankByPrefix(String url, JSONObject json) throws ControlSubSystemException {
        JSONObject resJson = sendPostPacket(url,json);
        QueryIdentityRankAnswer idRankInfo = new QueryIdentityRankAnswer();
        List<JSONObject> idList = new LinkedList<>();

        if (resJson.getIntValue("status") == 0) {
            idRankInfo.setStatus(0);
            idRankInfo.setMessage("获取企业节点标识排行失败！");
        }

        JSONArray jsonArray = resJson.getJSONArray("identityList");
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

    public static int queryNodeIdentityNum(String url, JSONObject json) throws ControlSubSystemException{
        JSONObject resJson = sendPostPacket(url, json);

        if (resJson.getIntValue("status") == 0) {
            throw new ControlSubSystemException("查询节点存储标识个数失败！");
        }

        return Integer.parseInt(resJson.getJSONObject("message").getString("idNums"));
    }

    private static JSONObject sendPostPacket(String url, JSONObject json) throws ControlSubSystemException {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");

        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        HttpEntity<String> requestEntity = new HttpEntity<String>(json.toString(), headers);
        try {
            String res = client.exchange(url, method, requestEntity, String.class).getBody();
            return JSONObject.parseObject(res);
        } catch (RestClientException e) {
            throw new ControlSubSystemException(e.getMessage());
        }
    }

    private static JSONObject sendGetPacket(String url) throws ControlSubSystemException{
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.GET;
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");

        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(headers);

        try {
            return client.exchange(url, method, requestEntity, JSONObject.class).getBody();
        } catch (RestClientException e) {
            throw new ControlSubSystemException(e.getMessage());
        }
    }
}
