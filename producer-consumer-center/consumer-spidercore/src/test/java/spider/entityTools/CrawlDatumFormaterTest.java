package spider.entityTools;

import commoncore.entity.requestEntity.CrawlDatum;
import commoncore.entity.requestEntity.CrawlDatumFormater;
import org.junit.Test;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-11-15:11
 */
public class CrawlDatumFormaterTest {

    @Test
    public void datumToString() {
        String s = CrawlDatumFormater.datumToString(new CrawlDatum("1123124124"));
        System.out.println("s =" + s);
    }
}