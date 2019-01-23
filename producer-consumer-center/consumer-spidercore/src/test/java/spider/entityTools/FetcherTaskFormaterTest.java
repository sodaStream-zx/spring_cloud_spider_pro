package spider.entityTools;

import commoncore.entity.requestEntity.FetcherTask;
import commoncore.entity.requestEntity.FetcherTaskFormater;
import org.junit.Test;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-11-15:11
 */
public class FetcherTaskFormaterTest {

    @Test
    public void datumToString() {
        String s = FetcherTaskFormater.datumToString(new FetcherTask("1123124124"));
        System.out.println("s =" + s);
    }
}