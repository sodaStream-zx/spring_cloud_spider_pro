package commoncore.parseTools;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * desc: 对象序列化和反序列化工具
 *
 * @author 一杯咖啡
 */
public class SerializeUtil {
    private static final Logger LOG = Logger.getLogger(SerializeUtil.class);

    /**
     * @Title：${序列化}
     * @Description: [序列化对象为字符串]
     * @author <a href="mail to: 113985238@qq.com" rel="nofollow">whitenoise</a>
     */
    public static String serializeToString(Object obj) throws Exception {
        LOG.debug("对象序列化");
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(obj);
        //此处只能是ISO-8859-1,但是不会影响中文使用
        return byteOut.toString("ISO_8859_1");
    }

    /**
     * @Title：${反序列化}
     * @Description: [反序列化字符串为对象]
     * @author <a href="mail to: *******@******.com" rel="nofollow">作者</a>
     */
    public static Object deserializeToObject(String str) throws Exception {
        LOG.debug("对象反序列化，进入程序");
        ByteArrayInputStream byteIn = new ByteArrayInputStream(str.getBytes(StandardCharsets.ISO_8859_1));
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        return objIn.readObject();
    }
}
