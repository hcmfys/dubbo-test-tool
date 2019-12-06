package org.springbus.dump;

import java.lang.instrument.Instrumentation;

public class DumpClassAgent {

    public static void premain(String agentOps, Instrumentation inst) {
        main(agentOps, inst);
    }

    public static void agentmain(String args, Instrumentation inst)
    {
        System.out.println("loadagent after main run.args=" + args);

        Class<?>[] classes = inst.getAllLoadedClasses();

        for (Class<?> cls : classes)
        {
            System.out.println(cls.getName());
        }

        System.out.println("agent run completely.");
    }


    public static void main(String agentOps, Instrumentation inst) {
        try {
            inst.addTransformer(new DumpClassTransformer(agentOps));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
