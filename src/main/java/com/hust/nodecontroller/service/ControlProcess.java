package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.enums.RequestTypeEnum;
import com.hust.nodecontroller.exception.ControlSubSystemException;
import com.hust.nodecontroller.infostruct.answerstruct.QueryAllByPrefixAnswer;
import com.hust.nodecontroller.infostruct.answerstruct.QueryIdAnswer;
import com.hust.nodecontroller.infostruct.requestrequest.BulkRegisterRequest;

/**
 * @program nodecontroller
 * @Description
 * @Author jokerwen666
 * @Date  2022-01-18 19:28
 **/
public interface ControlProcess {

    /**
    * 标识注册和更新操作处理
    * @param  client 请求用户
	* @param  identity 产品标识
	* @param  prefix 标识前缀
	* @param  data 标识数据
	* @param  dhtUrl dht节点url
	* @param  bcUrl 区块链节点url
	* @param  type 请求类型
    * @return void
    * @throws ControlSubSystemException 用户操作抛出的异常
    * @Author jokerwen666
    * @Date   2022/1/21
    */
    void registerAndUpdateHandle (String client, String identity, String prefix, JSONObject data, String dhtUrl, String bcUrl, RequestTypeEnum type) throws ControlSubSystemException;

    /**
    * 标识删除请求处理
    * @param  client 请求用户
	* @param  identity 产品标识
	* @param  prefix 标识前缀
	* @param  data
	* @param  dhtUrl
	* @param  bcUrl
	* @param  type
    * @return void
    * @throws
    * @Author jokerwen666
    * @Date   2022/1/21
    */
    void deleteHandle(String client, String identity, String prefix, String dhtUrl, String bcUrl, RequestTypeEnum type) throws ControlSubSystemException;

    /**
    * 处理用户操作（标识解析）
    * @param  client 请求用户
    * @param  identity 产品标识
    * @param  prefix 产品标识前缀
    * @param  domainPrefix 产品标识行业域
    * @param  dhtUrl dht节点url
    * @param  bcUrl 区块链节点url
    * @param  bcQueryOwner 区块链
    * @return com.hust.nodecontroller.infostruct.QueryResult
    * @throws ControlSubSystemException 用户操作抛出的异常
    * @Author jokerwen666
    * @Date   2022/1/20
    */
    QueryIdAnswer queryHandle(String client, String identity, String prefix, String domainPrefix, String dhtUrl, String bcUrl, String bcQueryOwner) throws ControlSubSystemException;

    /**
    * 根据企业前缀查询所有标识
    * @param  prefix 标识前缀
	* @param  client 请求用户
	* @param  matchString 匹配字段（模糊匹配）
	* @param  bcUrl 区块链url
    * @return com.hust.nodecontroller.infostruct.AnswerStruct.AllPrefixIdAnswer
    * @throws ControlSubSystemException 用户操作抛出的异常
    * @Author jokerwen666
    * @Date   2022/1/20
    */
    QueryAllByPrefixAnswer queryAllIdByPrefix(String prefix, String client, String matchString, String bcUrl) throws ControlSubSystemException;


    /** */
    int bulkRegister(BulkRegisterRequest bulkRegisterRequest, String dhtUrl, String bcUrl) throws ControlSubSystemException;
}
