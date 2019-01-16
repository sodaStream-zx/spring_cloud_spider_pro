/*
 * Copyright (C) 2014 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package commoncore.entity.httpEntity;

import commoncore.entity.requestEntity.CrawlDatum;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Page是爬取过程中，内存中保存网页爬取信息的一个容器，Page只在内存中存放，用于保存一些网页信息，方便用户进行自定义网页解析之类的操作。
 *
 * @author 一杯咖啡
 */
public class ResponsePage implements Serializable {

    public static final Logger LOG = LoggerFactory.getLogger(ResponsePage.class);
    private String siteName;
    private CrawlDatum crawlDatum;
    private String contentType;
    private Integer code;
    private Exception exception;
    private String html;
    private Document doc;
    private String charset;
    private byte[] content;

    public ResponsePage(CrawlDatum datum, Integer code, String contentType, byte[] content) {
        this.crawlDatum = datum;
        this.code = code;
        this.contentType = contentType;
        this.content = content.clone();
        //1.解析编码
        this.charset(content);
        //2.转化content 为html
        this.contentTohtml(content);
        //3.转化html 为doc
        this.contentTODoc(crawlDatum.getUrl());
    }

    /**
     * 返回网页的源码字符串
     *
     * @return 网页的源码字符串
     */
    public void contentTohtml(byte[] content) {
        if (StringUtils.isBlank(this.charset)) {
            this.charset = CharsetDetector.guessEncoding(content);
        }
        if (content == null) {
            this.html = null;
        } else {
            try {
                this.html = new String(content, charset);
            } catch (UnsupportedEncodingException e) {
                LOG.info("Exception when decoding ", e);
            }
        }
    }

    /**
     * 返回网页解析后的DOM树(Jsoup的Document对象) 已废弃，使用doc()方法代替
     *
     * @return 网页解析后的DOM树
     */
    public void contentTODoc(String url) {
        if (html != null) {
            this.doc = Jsoup.parse(html, url);
        } else {
            this.doc = null;
        }
    }

    public void charset(byte[] content) {
        if (charset == null) {
            this.charset = CharsetDetector.guessEncoding(content);
        }
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public CrawlDatum getCrawlDatum() {
        return crawlDatum;
    }

    public void setCrawlDatum(CrawlDatum crawlDatum) {
        this.crawlDatum = crawlDatum;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public byte[] getContent() {
        return content.clone();
    }

    public void setContent(byte[] content) {
        this.content = content.clone();
    }
}
