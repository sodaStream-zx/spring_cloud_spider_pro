package commoncore.entity.requestEntity;


import java.io.Serializable;

/**
 * 爬取任务的数据结构
 *
 * @author 一杯咖啡
 */
public class CrawlDatum implements Serializable {
    private static final long serialVersionUID = 4L;
    /**
     * STATUS_DB_UNEXECUTED 未爬取
     * STATUS_DB_FAILED 爬取失败
     * STATUS_DB_SUCCESS 爬取成功
     */
    public final static int STATUS_DB_UNEXECUTED = 0;
    public final static int STATUS_DB_FAILED = 1;
    public final static int STATUS_DB_SUCCESS = 5;

    private String url = null;
    private long executeTime = System.currentTimeMillis();
    private int status = STATUS_DB_UNEXECUTED;
    private int executeCount = 0;
    private String method = "GET";

    public CrawlDatum() {
    }

    public CrawlDatum(String url) {
        this.url = url;
    }

    public int incrExecuteCount(int count) {
        executeCount += count;
        return executeCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(long fetchTime) {
        this.executeTime = fetchTime;
    }

    public int getExecuteCount() {
        return executeCount;
    }

    public void setExecuteCount(int executeCount) {
        this.executeCount = executeCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return CrawlDatumFormater.datumToString(this);
    }

    public String briefInfo() {
        return String.format("CrawlDatum: (URL: %s)", this.getUrl());
    }
}
