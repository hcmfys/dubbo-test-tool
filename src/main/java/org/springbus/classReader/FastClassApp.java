package org.springbus.classReader;

import net.sf.cglib.reflect.FastClass;

import java.lang.reflect.InvocationTargetException;

public class FastClassApp {

    public  static  void  main(String [] args) throws InvocationTargetException {
        // FastClass动态子类实例
        FastClass fastClass = FastClass.create(DelegateClass.class);
        Object o = fastClass.newInstance(new Class[]{String.class}, new Object[]{"java"});
        fastClass.invoke("add", new Class[]{String.class, Integer.class}, o, new Object[]{"java", 12});

    }


}
