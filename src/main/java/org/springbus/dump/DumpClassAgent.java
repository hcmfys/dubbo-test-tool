package org.springbus.dump;

import java.lang.instrument.Instrumentation;

public class DumpClassAgent {

    public static void premain(String agentOps, Instrumentation inst) {
        try {
            inst.addTransformer(new DumpClassTransformer(agentOps));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
