package spider.myspider.spiderComponent;

import com.google.gson.Gson;
import commoncore.entity.httpEntity.HttpResponse;
import commoncore.entity.httpEntity.ResponsePage;
import commoncore.entity.requestEntity.CrawlDatum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import spider.spiderCore.http.IProxyGain;
import spider.spiderCore.http.ISendRequest;

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
    public static final Logger LOG = LoggerFactory.getLogger(DefaultHttpRequest.class);
    @Autowired
    private HttpConfig httpConfig;
    @Autowired
    private IProxyGain iProxyGain;

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
        // responsePage.obj(httpResponse);
        //LOG.debug(responsePage.toString());
        return responsePage;
    }


    /**
     * desc: 设置请求头
     **/
    @Override
    public void configHttpRequest(HttpURLConnection httpURLConnection) {
        httpURLConnection.setInstanceFollowRedirects(httpConfig.isFollowRedirects());
        httpURLConnection.setDoInput(httpConfig.isDoinput());
        httpURLConnection.setDoOutput(httpConfig.isDooutput());
        httpURLConnection.setConnectTimeout(httpConfig.getTimeoutForConnect());
        httpURLConnection.setReadTimeout(httpConfig.getTimeoutForRead());
        MultiValueMap header = httpConfig.getHeaderMap();
        if (!header.containsKey("User-Agent")) {
            header.set("User-Agent", httpConfig.getUserAgent());
        }
        if (!header.containsKey("Cookie")) {
            header.set("Cookie", httpConfig.getCookie());
        }
        if (header.size() > 0) {
            Set<Map.Entry<String, List<String>>> keyAndVaule = header.entrySet();
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
        int realRedirectNum = Math.max(0, httpConfig.getMaxRedirect());
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
                MultiValueMap postBody = httpConfig.getPostBodyMap();
                if (postBody != null && postBody.size() > 0) {
                    OutputStream postStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(postStream, StandardCharsets.UTF_8));
                    String jsonPostBody = new Gson().toJson(postBody);
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
                        if (redirect == httpConfig.getMaxRedirect()) {
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
            int maxsize = httpConfig.getMaxReceiveSize() * 1024 * 1024;
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
        httpConfig.getHeaderMap().remove(key);
    }

    /**
     * desc: 在现有header上添加key value
     **/
    public void addHeader(String key, String value) {
        if (!checkNull(key, value)) {
            httpConfig.getHeaderMap().add(key, value);
        }
    }

    /**
     * desc: 设置key value 如果存在则覆盖
     **/
    public void setHeader(String key, String value) {
        if (!checkNull(key, value)) {
            httpConfig.getHeaderMap().set(key, value);
        }
    }

    /**
     * desc: headerMap key value 空值检查
     **/
    public boolean checkNull(String key, String value) {
        if (key == null || "".equals(key)) {
            return true;
        } else {
            return value == null || "".equals(value);
        }

    }

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
}
