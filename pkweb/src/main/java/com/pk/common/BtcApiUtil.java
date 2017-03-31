package com.pk.common;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 比特币接口
 * Created by pengkai
 *
 * @date 2016/10/14 0014.
 * @email pengxiankaikai@163.com
 */
public class BtcApiUtil {

    private static final Logger log = LoggerFactory.getLogger(BtcApiUtil.class);

    //key=>访问api的身份ID 详情参见http://qukuai.com/api
    private static final String key = "2ejf4jgfNoya8Y3GnQf68e4J23HherpUh1";
    //limit=>查询数量
    private static final int limit = 50;
    //请求方式
    private static final String requestMethodPost = "POST";
    private static final String requestMethodGet = "GET";

    //币种
    public static final String COIN_TYPE_BTC = "BTC"; //比特币
    public static final String COIN_TYPE_LTC = "LTC"; //莱特币

    //比特币真实值与查询值的换算比例
    public static final BigDecimal BTC_EXCHANGE_NUM = new BigDecimal(100000000);

    // ------------------------------ qukuai.com api ----------------------------- //

    /**
     * 获取比特币地址账户余额
     *
     * @param btcAddr 比特币地址
     * @return
     */
    public static BigDecimal queryBtcAccountBalance(String btcAddr) {
        Gson gson = new Gson();
        BigDecimal balance = new BigDecimal(-1);
        if (StringUtils.isNotBlank(btcAddr)) {
            String btcAccountInfo = getBtcAccountInfo(btcAddr);
            if (StringUtils.isNotBlank(btcAccountInfo)) {
                Map map = gson.fromJson(btcAccountInfo, Map.class);
                if (null != map && null != map.get("balance")) {
                    Double b = (Double) map.get("balance");
                    balance = new BigDecimal(b);
                }
            }
        }
        return balance;
    }

    /**
     * 获取比特币地址交易记录
     *
     * @param btcAddr 比特币地址
     * @return List<Map<String, Object>> 多条交易结果集 其中map中包含 交易时间，交易hash， 交易值；
     */
    public static List<Map<String, Object>> queryTradeValue(String btcAddr) {
        Gson gson = new Gson();
        List<Map<String, Object>> trades = new ArrayList<>();
        if (StringUtils.isNotBlank(btcAddr)) {
            try {
                String btcTransactionsInfo = getBtcTransactionsInfo(btcAddr);
                ArrayList arrayList = gson.fromJson(btcTransactionsInfo, ArrayList.class);
                for (int i = 0; i < arrayList.size(); i++) {
                    Map map = (Map) arrayList.get(i);
                    ArrayList outputs = (ArrayList) map.get("outputs");
                    for (Object o : outputs) {
                        Map out = (Map) o;
                        ArrayList addr = (ArrayList) out.get("address");
                        for (Object o1 : addr) {
                            String a = (String) o1;
                            if (btcAddr.equals(a)) {
                                Map<String, Object> m = new HashMap<>();
                                Double value = (Double) out.get("value");
                                BigDecimal b = new BigDecimal(value);
                                m.put("blockTime", map.get("block_time")); //交易时间
                                m.put("transactionHash", map.get("transaction_hash")); //交易hash
                                m.put("value", value);
                                trades.add(m);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("[by bitcoin API to obtain the transaction information error error message: {}] [] []", e);
                return new ArrayList<Map<String, Object>>();
            }
        }
        return trades;
    }

    /**
     * 根据比特币地址获取账户交易信息
     *
     * @param btcAddr
     * @return
     */
    public static String getBtcTransactionsInfo(String btcAddr) {
        String resInfo = null;
        StringBuilder urlStr = new StringBuilder("http://open.qukuai.com/address/transactions/");
        if (StringUtils.isNotBlank(btcAddr)) {
            urlStr.append(btcAddr);
            urlStr.append("?key=");
            urlStr.append(key);
            urlStr.append("&limit=");
            urlStr.append(limit);
            try {
                //接口访问路径
                URL url = new URL(urlStr.toString());
                //建立连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //设置允许输入输出
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //设置不用缓存，默认为true
                conn.setUseCaches(false);
                conn.setRequestMethod(requestMethodGet);
                // 设置传输类型:
                conn.setRequestProperty("contentType", "application/json");
                //开始连接
                conn.connect();
                int responseCode = conn.getResponseCode();
                if (200 == responseCode) {
                    InputStream in = conn.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    byte[] c = new byte[1024];
                    int lenI;
                    while ((lenI = in.read(c)) != -1) {
                        buffer.append(new String(c, 0, lenI));
                    }
//                    System.out.println(in.available());
//                    byte[] data = new byte[in.available()];
//                    in.read(data);
//                    resInfo = new String(data);
                    resInfo = buffer.toString();
                } else {
                    log.info("[access bitcoin API] [get an address transaction information] [request connection failure]");
                }
            } catch (Exception e) {
                log.error("[API] [access bitcoin transaction information to obtain an address] [abnormal] {}", e);
            }
        }
        return resInfo;
    }

    /**
     * 根据交易hash查找该笔交易的确认次数
     *
     * @param hash 哈希值
     * @return
     */
    public static double getTradeHashConfirmations(String hash) {
        double res = 0;
        Gson gson = new Gson();
        StringBuilder urlStr = new StringBuilder("http://open.qukuai.com/transaction/");
        if (StringUtils.isNotBlank(hash)) {
            urlStr.append(hash);
            urlStr.append("?key=");
            urlStr.append(key);
            try {
                //接口访问路径
                URL url = new URL(urlStr.toString());
                //建立连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //设置允许输入输出
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //设置不用缓存，默认为true
                conn.setUseCaches(false);
                conn.setRequestMethod(requestMethodGet);
                // 设置传输类型:
                conn.setRequestProperty("contentType", "application/json");
                //开始连接
                conn.connect();
                int responseCode = conn.getResponseCode();
                if (200 == responseCode) {
                    InputStream in = conn.getInputStream();
                    byte[] data = new byte[in.available()];
                    in.read(data);
                    String resInfo = new String(data);
                    Map m = gson.fromJson(resInfo, Map.class);
                    res = (double) m.get("confirmations");
                } else {
                    log.info("[access bitcoin API] [according to the transaction hash find the number of transactions confirmed] [request connection failed]");
                }
            } catch (Exception e) {
                log.error("[API] [access bitcoin transaction according to hash to find the transaction confirmation number] [abnormal] {}", e);
            }
        }
        return res;
    }

    /**
     * 根据比特币地址获取账户信息
     *
     * @param btcAddr 比特币地址
     * @return
     */
    public static String getBtcAccountInfo(String btcAddr) {
        String resInfo = null;
        StringBuilder urlStr = new StringBuilder("http://open.qukuai.com/address/");
        if (StringUtils.isNotBlank(btcAddr)) {
            urlStr.append(btcAddr);
            urlStr.append("?key=");
            urlStr.append(key);
            try {
                //接口访问路径
                URL url = new URL(urlStr.toString());
                //建立连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //设置允许输入输出
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //设置不用缓存，默认为true
                conn.setUseCaches(false);
                conn.setRequestMethod(requestMethodGet);
                // 设置传输类型:
                conn.setRequestProperty("contentType", "application/json");
                //开始连接
                conn.connect();
                int responseCode = conn.getResponseCode();
                if (200 == responseCode) {
                    InputStream in = conn.getInputStream();
                    byte[] data = new byte[in.available()];
                    in.read(data);
                    resInfo = new String(data);
                } else {
                    log.info("[access bitcoin API] [get an address account information] [request connection failure]");
                }
            } catch (Exception e) {
                log.error("[API] [bitcoin access account information to obtain an address] [abnormal] {}", e);
            }
        }
        return resInfo;
    }

    // ------------------------------ okcoin api ----------------------------- //

    /**
     * 使用okcoin api 返回该比特币地址的交易信息
     * @param btcAddr 比特币地址
     * @return
     */
    public static String getBtcTradeRecordByAddrOKcoin(String btcAddr, String coinType){
        String resInfo = null;
        StringBuilder urlStr = new StringBuilder("http://block.okcoin.cn/api/v1/address.do?address=");
        if (StringUtils.isNotBlank(btcAddr)) {
            urlStr.append(btcAddr);
            if (StringUtils.isNotBlank(coinType) && StringUtils.equals(coinType, COIN_TYPE_BTC)){
                urlStr.append("&symbol=BTC&size=");
            }else if (StringUtils.isNotBlank(coinType) && StringUtils.equals(coinType, COIN_TYPE_LTC)){
                urlStr.append("&symbol=LTC&size=");
            }else {
            }
            urlStr.append(limit);
            try {
                //接口访问路径
                URL url = new URL(urlStr.toString());
                //建立连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //设置允许输入输出
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //设置不用缓存，默认为true
                conn.setUseCaches(false);
                conn.setRequestMethod(requestMethodGet);
                // 设置传输类型:
                conn.setRequestProperty("contentType", "application/json");
                //开始连接
                conn.connect();
                int responseCode = conn.getResponseCode();
                if (200 == responseCode) {
                    InputStream in = conn.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    byte[] c = new byte[1024];
                    int lenI;
                    while ((lenI = in.read(c)) != -1) {
                        buffer.append(new String(c, 0, lenI));
                    }
                    resInfo = buffer.toString();
                } else {
                    log.info("[access okcoin bitcoin API] [return to the address of the bitcoin transaction information] [request connection failure]");
                }
            } catch (Exception e) {
                log.error("[API] [okcoin access bitcoin bitcoin address returns the transaction information] [abnormal] {}", e);
            }
        }
        return resInfo;
    }

    /**
     * 根据比特币或莱特币地址地址获取交易信息(只获取充值记录[转入])（okcoin接口）
     * @param btcAddr 比特币地址
     * @return List<Map<String, Object>> 转入结果集 Map-> receivedTime/交易时间　tradeHash／交易hash tradeValue/交易比特币值 confirmations/确认次数  rechargeAmount/充值总额（最后一条记录的数据为总额）
     */
    public static List<Map<String, Object>> getTradeRecordByOKcoin(String btcAddr, String coinType){
        Gson gson = new Gson();
        List<Map<String, Object>> res = new ArrayList<>();
        if (StringUtils.isNotBlank(btcAddr) && StringUtils.isNotBlank(coinType)){
            String btcInfo = getBtcTradeRecordByAddrOKcoin(btcAddr, coinType);
            if (StringUtils.isNotBlank(btcInfo)){
                //返回错误数据排除(空数据)
                if (!(btcInfo.indexOf("10010") >= 0)){
                    ArrayList btcInfos = gson.fromJson(btcInfo, ArrayList.class);
                    BigDecimal rechargeAmount = new BigDecimal(0); //充值总额
                    if (!CollectionUtils.isEmpty(btcInfos)){
                        for (int i = 0; i < btcInfos.size(); i++){
                            Map o = (Map) btcInfos.get(i);
                            //判断该地址的充值记录（判断规则：api返回的交易记录中查看in中是否有该地址，如果in中有而且out中也有该地址说明是转出；如果in中没有而out中有该地址说明是充值【转入】）
                            List vin = (List) o.get("vin");
                            boolean isRecharge = false;
                            if (!CollectionUtils.isEmpty(vin)){
                                for (int z = 0; z < vin.size(); z++){
                                    Map info = (Map) vin.get(z);
                                    Map prevOut = (Map) info.get("prev_out");
                                    Map scriptPubKey = (Map) prevOut.get("scriptPubKey");
                                    List addresses = (List) scriptPubKey.get("addresses");
                                    for (int x = 0; x < addresses.size(); x++){
                                        String address = (String) addresses.get(x);
                                        if (!btcAddr.equals(address)){
                                            isRecharge = true;
                                        }
                                    }
                                }
                            }
                            List vout = (List) o.get("vout");
                            if (!CollectionUtils.isEmpty(vout)){
                                for (int j = 0; j < vout.size(); j++){
                                    Map info = (Map) vout.get(j);
                                    Map scriptPubKey = (Map) info.get("scriptPubKey");
                                    List addresses = (List) scriptPubKey.get("addresses");
                                    String address = (String) addresses.get(0);
                                    if (isRecharge && btcAddr.equals(address)){
                                        Map<String, Object> map = new HashMap<>();
                                        rechargeAmount = rechargeAmount.add(new BigDecimal((Double) info.get("value")));
                                        map.put("receivedTime", o.get("received_time")); //交易时间
                                        map.put("tradeHash", o.get("txid")); //交易hash
                                        map.put("tradeValue", info.get("value")); //交易比特币值
                                        map.put("confirmations", o.get("confirmations")); //确认次数
                                        map.put("rechargeAmount", rechargeAmount); //充值总额（最后一条记录的数据为总额）
                                        res.add(map);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return res;
    }
}
