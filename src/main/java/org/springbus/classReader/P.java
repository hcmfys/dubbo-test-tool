package org.springbus.classReader;


import org.objectweb.asm.util.ASMifier;

public class P {

    public  static void  main(String[] args) throws Exception {

        args=new String[]{"-debug","org.springbus.classReader.Min"};
        ASMifier.main(args);
    }
}
