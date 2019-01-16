package commoncore.entity.requestEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 网站链接数据结构格式化
 *
 * @author Twilight
 */
public class CrawlDatumFormater {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String datumToString(CrawlDatum datum) {
        StringBuilder sb = new StringBuilder();

        sb.append("[URL: ").append(datum.getUrl()).append(",STATUS: ");

        switch (datum.getStatus()) {
            case CrawlDatum.STATUS_DB_SUCCESS:
                sb.append("success");
                break;
            case CrawlDatum.STATUS_DB_FAILED:
                sb.append("failed");
                break;
            case CrawlDatum.STATUS_DB_UNEXECUTED:
                sb.append("unexecuted");
                break;
            default:
                sb.append("unexecuted");
                break;
        }

        //加入执行时间
        sb.append(",ExecuteTime:")
                .append(sdf.format(new Date(datum.getExecuteTime())))
                .append(",ExecuteCount:")
                .append(datum.getExecuteCount())
                .append(",Method:")
                .append(datum.getMethod() + "]");
        return sb.toString();
    }
}
