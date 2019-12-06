package org.springbus.test.demo;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.util.List;

public class AttachMain {

    public static void main(String args[]) throws AttachNotSupportedException {
        VirtualMachine vm;
        List<VirtualMachineDescriptor> vmList= VirtualMachine.list();
        if(vmList==null || vmList.isEmpty()){
            System.out.println("当前没有java程序运行");
            return;
        }

        //展示所有运行中的java程序
        System.out.println("当前运行中的java程序：");
        for(int i=0;i<vmList.size();i++){
            System.out.println("["+i+"]  "+vmList.get(i).displayName()+" ,id:"+vmList.get(i).id());
        }
        System.out.println("请选择（输入序号）：");

        //选择其中一个java进程进行增强
        try{
            int num=System.in.read()-48;
            if(num!=-1&&num<vmList.size()){
                vm= VirtualMachine.attach(vmList.get(num));
                vm.loadAgent("E:\\mj-project\\vmattach\\target\\vmattach-1.0-SNAPSHOT.jar");
                System.out.println(" connected ok ");
                System.in.read();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
