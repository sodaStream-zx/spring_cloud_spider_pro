package spider.spiderCore.ihttp.myHttpRequestImp;

import commoncore.entity.httpEntity.ResponseData;
import commoncore.entity.requestEntity.FetcherTask;
import commoncore.exceptionHandle.MyException;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import spider.myspider.httpComponent.DefaultHttpRequest;

import java.io.IOException;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-29-17:06
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultHttpRequestTest {
    private static final Logger log = Logger.getLogger(DefaultHttpRequestTest.class);
    @Autowired
    DefaultHttpRequest defaultHttpRequest;

    @Test
    public void sendRequest() throws IOException, MyException {
        String url = "https://blog.csdn.net/u012426327/article/details/77155556";

        FetcherTask fetcherTask = new FetcherTask("https://blog.csdn.net/scyatcs/article/details/25042351");

        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        header.add("Cookie", "TY_SESSION_ID=0255bf13-3192-4c98-9ac3-1237a7bf0600; uuid_tt_dd=8311821275652513019_20180330; kd_user_id=f7348300-802c-4c08-bab0-9a006596bbf3; UN=HAKUNATATA; __yadk_uid=xfM8n0vZZX7xbKratUAzM6SWtzsVnbEV; __utma=17226283.1085313314.1529463397.1529463397.1529463397.1; smidV2=201806251145075909c72466c480b776320dbfd4a1c1280060f3355574fd090; bdshare_firstime=1530687145319; ADHOC_MEMBERSHIP_CLIENT_ID1.0=ffccd7fd-108f-307d-3053-ade1c3fd5b88; ARK_ID=JSd44f07dd873b83dae8ccd3d19bd2b429d44f; _ga=GA1.2.1085313314.1529463397; BT=1543413905371; dc_session_id=10_1543458212936.544874; tipShow=true; Hm_ct_6bcd52f51e9b3dce32bec4a3997715ac=1788*1*PC_VC!5744*1*HAKUNATATA; Hm_lvt_6bcd52f51e9b3dce32bec4a3997715ac=1543582976,1543941956,1544634895,1544705654; firstDie=1; dc_tos=pkp27f; c-login-auto=21");
        header.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.6788.400 QQBrowser/10.3.2767.400");
        //defaultHttpRequest.setHeader(header);

        ResponseData httpResponse = defaultHttpRequest.converterResponsePage(new FetcherTask(url));
        //  log.info("request= " + defaultHttpRequest.get().toString());
        log.info("html == :::" + httpResponse.getHtml());
        log.info("contentTpe == :::" + httpResponse.getContentType());
        log.info("url == :::" + httpResponse.getFetcherTask().getUrl());
        // log.info(httpRequestUtil.toString());
    }
}