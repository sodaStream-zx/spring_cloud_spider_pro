package spider.myspider.spiderComponent;

import commoncore.entity.responseEntity.CrawlDatum;
import commoncore.entity.responseEntity.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spider.spiderCore.http.Requester;
import spider.spiderCore.http.myHttpRequestImp.DefaultHttpRequest;

@Component
public class MyRequester implements Requester {
    @Autowired
    DefaultHttpRequest defaultHttpRequestUtill;

    /**
     * @Title：${enclosing_method}
     * @Description: [实现 requester接口，调用自身方法获取网页]
     * 代理设置 proxy
     */
    @Override
    public ResponsePage getResponse(CrawlDatum crawlDatum) {
        ResponsePage responsePage = defaultHttpRequestUtill.converterResponsePage(crawlDatum);
        // HttpRequest request = new HttpRequest(crawlDatum);
       /* Proxy proxy=new Proxy(Proxy.Type.HTTP, new InetSocketAddress("14.18.16.67",80));
//        HashMap<String ,Integer> proxy= site.getProxys();
//        for (Map.Entry<String, Integer> entry : proxy.entrySet()) {
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//            proxys.add(entry.getKey(),entry.getValue());
//        }
//        request.setProxy(proxys.nextRandom());
        request.setProxy(proxy);*/
        return responsePage;
    }
}
