import java.util.Arrays;
import java.util.List;

/**
 * Created by duhc on 2017/7/20.
 */
public class Demo {
    public static void main(String[] args) {
        int[] arrays = new int[]{1,2,3,4,5,5632};
        Integer[] arras = new Integer[]{1,2,3,4,5};
        System.out.println(arrays.length);
        List list = Arrays.asList(arrays);
        List list1  = Arrays.asList(arras);
        System.out.println(list.size());
        System.out.println(list1.size());
    }
}
