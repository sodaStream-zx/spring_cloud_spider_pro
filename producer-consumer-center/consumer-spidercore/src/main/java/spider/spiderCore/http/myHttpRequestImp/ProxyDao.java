package spider.spiderCore.http.myHttpRequestImp;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author 一杯咖啡
 * @desc 从数据库获取代理ip 列表
 * @createTime 2018-12-28-16:16
 */
public class ProxyDao {

    public static void main(String[] args) {
        InetSocketAddress proxyLocation = new InetSocketAddress("192.168.88.106", 808);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyLocation);
    }
}
