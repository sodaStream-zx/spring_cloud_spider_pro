package spider.spiderCore.entitys;

import java.util.List;

/**
 * @author Twilight
 */
public class ListUtils {
    public static <T> T getByIndex(List<T> list, int index){
        int realIndex = index;
        if (index < 0) {
            realIndex = list.size() + index;
        }
        return list.get(realIndex);
    }

}
