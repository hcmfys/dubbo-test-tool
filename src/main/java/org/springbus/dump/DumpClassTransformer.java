package org.springbus.dump;

import javassist.ClassPool;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class DumpClassTransformer implements ClassFileTransformer {

    private static ClassPool pool;

    private String packageName;

    public DumpClassTransformer(String agentOps) {
        this.packageName = agentOps;
    }

    static {
        pool = ClassPool.getDefault();
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        String curClassName = className.replace("/", ".");
        try {
            if (curClassName.indexOf(packageName) != -1) {
                System.out.println(" loading ..." + curClassName);
                CtClass ctClass = pool.makeClass(new ByteArrayInputStream(classfileBuffer), false);
                ctClass.writeFile("./out/");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
