package di.jsonparser.objects;

import java.util.Arrays;

public class BeansJson {

    private BeanJson[] beans;

    public BeanJson[] getBeans() {
        return beans;
    }

    @Override
    public String toString() {
        return "BeansJson{" +
            "beans=" + Arrays.toString(beans) +
            '}';
    }
}
