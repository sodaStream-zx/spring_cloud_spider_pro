package commoncore.customUtils;

/**
 * desc: 字符串分解工具
 *
 * @author Twilight
 */
public class StringSplitUtil {
    /**
     * desc:分解规则 ，
     **/
    public static String[] splitRule(String rule) {
        String[] list = rule.split(",");
        return list;
    }

    /**
     * desc:剪切字符串 length ：20
     **/
    public static String SubStr(String str) {
        if (str.length() > 20) {
            str = str.substring(0, 19);
        }
        return str;
    }
}
