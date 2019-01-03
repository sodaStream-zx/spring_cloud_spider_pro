package spider.spiderCore.http.myHttpRequestImp;

import com.google.gson.Gson;
import commoncore.entity.responseEntity.CrawlDatum;
import commoncore.entity.responseEntity.ResponsePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import spider.spiderCore.http.IProxyGain;
import spider.spiderCore.http.ISendRequest;
import spider.spiderCore.http.httpImp.HttpRequest;
import spider.spiderCore.http.httpImp.HttpResponse;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * @author 一杯咖啡
 * @desc 请求发送器
 * @createTime 2018-12-28-13:17
 */
@Component
@Scope("prototype")
public class DefaultHttpRequest implements ISendRequest<ResponsePage> {
    public static final Logger LOG = LoggerFactory.getLogger(HttpRequest.class);

    @Value(value = "${httpconfig.maxRedirect}")
    private int maxRedirect = 0;
    @Value(value = "${httpconfig.maxReceiveSize}")
    private int maxReceiveSize = 100;
    @Value(value = "${httpconfig.timeoutForConnect}")
    private int timeoutForConnect = 200;
    @Value(value = "${httpconfig.timeoutForRead}")
    private int timeoutForRead = 200;
    @Value(value = "${httpconfig.userAgent}")
    private String userAgent = "test string";
    private String cookie = null;

    @Autowired
    private IProxyGain iProxyGain;

    private boolean doinput = true;
    private boolean dooutput = true;
    private boolean followRedirects = false;

    private MultiValueMap headerMap = new LinkedMultiValueMap();
    private MultiValueMap postBodyMap = new LinkedMultiValueMap();

    public DefaultHttpRequest() {
    }

    /**
     * desc: 将httpResponse 封装为responsePage
     **/
    @Override
    public ResponsePage converterResponsePage(CrawlDatum crawlDatum) {
        HttpResponse httpResponse = this.sendRequest(crawlDatum);
        ResponsePage responsePage = new ResponsePage(
                crawlDatum,
                httpResponse.code(),
                httpResponse.contentType(),
                httpResponse.content()
        );
        responsePage.obj(httpResponse);
        //LOG.debug(responsePage.toString());
        return responsePage;
    }


    /**
     * desc: 设置请求头
     **/
    @Override
    public void configHttpRequest(HttpURLConnection httpURLConnection) {
        httpURLConnection.setInstanceFollowRedirects(followRedirects);
        httpURLConnection.setDoInput(doinput);
        httpURLConnection.setDoOutput(dooutput);
        httpURLConnection.setConnectTimeout(timeoutForConnect);
        httpURLConnection.setReadTimeout(timeoutForRead);
        if (!headerMap.containsKey("User-Agent")) {
            headerMap.set("User-Agent", userAgent);
        }
        if (!headerMap.containsKey("Cookie")) {
            headerMap.set("Cookie", cookie);
        }
        if (headerMap.size() > 0) {
            Set<Map.Entry<String, List<String>>> keyAndVaule = headerMap.entrySet();
            keyAndVaule.forEach(stringListEntry -> {
                int valueLen = stringListEntry.getValue().size();
                for (int i = 0; i < valueLen; i++) {
                    httpURLConnection.addRequestProperty(stringListEntry.getKey(), stringListEntry.getValue().get(i));
                }
            });
        }
    }


    /**
     * desc: 发送请求，返回响应数据
     **/
    @Override
    public HttpResponse sendRequest(CrawlDatum crawlDatum) {
        HttpURLConnection con = null;
        InputStream is = null;
        int code = -1;
        int realRedirectNum = Math.max(0, maxRedirect);
        try {
            URL url = new URL(crawlDatum.url());
            HttpResponse response = new HttpResponse(url);
            //请求重试次数
            for (int redirect = 0; redirect <= realRedirectNum; redirect++) {
                Proxy proxy = iProxyGain.nextRandom();
                if (proxy == null) {
                    con = (HttpURLConnection) url.openConnection();
                } else {
                    con = (HttpURLConnection) url.openConnection(proxy);
                }
                con.setRequestMethod(crawlDatum.getMethod());
                this.configHttpRequest(con);
                //post 请求写入信息
                if (postBodyMap != null && postBodyMap.size() > 0) {
                    OutputStream postStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(postStream, StandardCharsets.UTF_8));
                    String jsonPostBody = new Gson().toJson(postBodyMap);
                    bufferedWriter.write(jsonPostBody);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
                code = con.getResponseCode();
                /*只记录第一次返回的code*/
                if (redirect == 0) {
                    response.code(code);
                }
                if (code == HttpURLConnection.HTTP_NOT_FOUND) {
                    response.setNotFound(true);
                    return response;
                }
                boolean needBreak = false;
                switch (code) {
                    case HttpURLConnection.HTTP_MOVED_PERM:
                    case HttpURLConnection.HTTP_MOVED_TEMP:
                        response.setRedirect(true);
                        if (redirect == maxRedirect) {
                            throw new Exception("redirect to much time");
                        }
                        String location = con.getHeaderField("Location");
                        if (location == null) {
                            throw new Exception("redirect with no location");
                        }
                        String originUrl = url.toString();
                        url = new URL(url, location);
                        response.setRealUrl(url);
                        LOG.info("redirect from " + originUrl + " to " + url.toString());
                        continue;
                    default:
                        needBreak = true;
                        break;
                }
                if (needBreak) {
                    break;
                }
            }
            is = con.getInputStream();
            String contentEncoding = con.getContentEncoding();
            if (contentEncoding != null && contentEncoding.equals("gzip")) {
                is = new GZIPInputStream(is);
            }

            byte[] buf = new byte[2048];
            int read;
            int sum = 0;
            int maxsize = maxReceiveSize * 1024 * 1024;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((read = is.read(buf)) != -1) {
                if (maxsize > 0) {
                    sum = sum + read;
                    if (maxsize > 0 && sum > maxsize) {
                        read = maxsize - (sum - read);
                        bos.write(buf, 0, read);
                        break;
                    }
                }
                bos.write(buf, 0, read);
            }
            response.content(bos.toByteArray());
            response.headers(con.getHeaderFields());
            bos.close();

            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.error("someting was wrong");
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("关闭输入流出错");
                }
            }
        }
    }

    public void removeHeader(String key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        headerMap.remove(key);
    }

    /**
     * desc: 在现有header上添加key value
     **/
    public void addHeader(String key, String value) {
        if (!checkNull(key, value)) {
            headerMap.add(key, value);
        }
    }

    /**
     * desc: 设置key value 如果存在则覆盖
     **/
    public void setHeader(String key, String value) {
        if (!checkNull(key, value)) {
            headerMap.set(key, value);
        }
    }


    /**
     * desc: headerMap key value 空值检查
     **/
    public boolean checkNull(String key, String value) {
        if (key == null || "".equals(key)) {
            return true;
        } else return value == null || "".equals(value);
    }

    /*@Override
    public String toString() {
        return "HttpRequestUtil{" +
                "maxRedirect=" + maxRedirect +
                ", maxReceiveSize=" + maxReceiveSize +
                ", timeoutForConnect=" + timeoutForConnect +
                ", timeoutForRead=" + timeoutForRead +
                ", userAgent='" + userAgent + '\'' +
                ", doinput=" + doinput +
                ", dooutput=" + dooutput +
                ", followRedirects=" + followRedirects +
                ", headerMap=" + headerMap +
                ", postBodyMap=" + postBodyMap +
                '}';
    }*/

    static {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception ex) {
            LOG.info("Exception", ex);
        }
    }

    public int getMaxRedirect() {
        return maxRedirect;
    }

    public void setMaxRedirect(int maxRedirect) {
        this.maxRedirect = maxRedirect;
    }

    public int getMaxReceiveSize() {
        return maxReceiveSize;
    }

    public void setMaxReceiveSize(int maxReceiveSize) {
        this.maxReceiveSize = maxReceiveSize;
    }

    public int getTimeoutForConnect() {
        return timeoutForConnect;
    }

    public void setTimeoutForConnect(int timeoutForConnect) {
        this.timeoutForConnect = timeoutForConnect;
    }

    public int getTimeoutForRead() {
        return timeoutForRead;
    }

    public void setTimeoutForRead(int timeoutForRead) {
        this.timeoutForRead = timeoutForRead;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public IProxyGain getiProxyGain() {
        return iProxyGain;
    }

    public void setiProxyGain(IProxyGain iProxyGain) {
        this.iProxyGain = iProxyGain;
    }

    public boolean isDoinput() {
        return doinput;
    }

    public void setDoinput(boolean doinput) {
        this.doinput = doinput;
    }

    public boolean isDooutput() {
        return dooutput;
    }

    public void setDooutput(boolean dooutput) {
        this.dooutput = dooutput;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public MultiValueMap getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(MultiValueMap headerMap) {
        this.headerMap = headerMap;
    }

    public MultiValueMap getPostBodyMap() {
        return postBodyMap;
    }

    public void setPostBodyMap(MultiValueMap postBodyMap) {
        this.postBodyMap = postBodyMap;
    }
}
