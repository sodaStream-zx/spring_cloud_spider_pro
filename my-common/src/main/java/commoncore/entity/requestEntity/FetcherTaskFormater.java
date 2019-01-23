package commoncore.entity.requestEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 网站链接数据结构格式化
 *
 * @author Twilight
 */
public class FetcherTaskFormater {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String datumToString(FetcherTask task) {
        StringBuilder sb = new StringBuilder();

        sb.append("[URL: ").append(task.getUrl()).append(",STATUS: ");

        switch (task.getStatus()) {
            case FetcherTask.STATUS_DB_SUCCESS:
                sb.append("success");
                break;
            case FetcherTask.STATUS_DB_FAILED:
                sb.append("failed");
                break;
            case FetcherTask.STATUS_DB_UNEXECUTED:
                sb.append("unexecuted");
                break;
            default:
                sb.append("unexecuted");
                break;
        }

        //加入执行时间
        sb.append(",ExecuteTime:")
                .append(sdf.format(new Date(task.getExecuteTime())))
                .append(",ExecuteCount:")
                .append(task.getExecuteCount())
                .append(",Method:")
                .append(task.getMethod() + "]");
        return sb.toString();
    }
}
