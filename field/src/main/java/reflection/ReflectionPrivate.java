package reflection;

import java.lang.reflect.Field;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：reflection
 *
 * @author asdf2014
 * @since 2015/11/16
 */
public class ReflectionPrivate {

    public SimpleBean createSimpleBean(Integer i, Long l, String s) throws NoSuchFieldException, IllegalAccessException {

        SimpleBean simpleBean = new SimpleBean();

        Class clazz = simpleBean.getClass();
        Field fI = clazz.getDeclaredField("i");
        Field fL = clazz.getDeclaredField("l");
        Field fS = clazz.getDeclaredField("s");

        fI.setAccessible(true);
        fL.setAccessible(true);
        fS.setAccessible(true);

        Class type = fI.getType();
        String strI = i.toString();

        Object objI = Integer.parseInt(strI);

        if (Integer.class == type) {
            fI.set(simpleBean, objI);
        } else if (Long.class == type) {
        } else if (String.class == type) {
        }

        fL.set(simpleBean, l);
        fS.set(simpleBean, s);
        return simpleBean;
    }
}
