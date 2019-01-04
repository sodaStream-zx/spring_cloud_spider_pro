package spider.spiderCore.fetcher;

import org.springframework.stereotype.Component;

/**
 * @author 一杯咖啡
 * @desc 调度器各个组件状态管理
 * @createTime 2018-12-26-16:49
 */
@Component
public class FetcherState {
    private boolean feederRunnning = false;
    private boolean fetcherRunning = false;

    public FetcherState() {
    }

    public boolean isFeedRunnning() {
        return feederRunnning;
    }

    public void setFeedRunnning(boolean feederRunnning) {
        this.feederRunnning = feederRunnning;
    }

    public boolean isFetcherRunning() {
        return fetcherRunning;
    }

    public void setFetcherRunning(boolean fetcherRunning) {
        this.fetcherRunning = fetcherRunning;
    }
}