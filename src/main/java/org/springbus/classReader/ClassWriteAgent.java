package org.springbus.classReader;


import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;

public class ClassWriteAgent {

    public static void main(String[] args) throws IOException {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_6, ACC_PUBLIC, "Student", null, "java/lang/Object", null);
        cw.visitField(Opcodes.ACC_PRIVATE, "age", "I", null, 120).visitEnd();

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(5, l0);
        mv.visitIntInsn(BIPUSH, 10);
        mv.visitVarInsn(ISTORE, 1);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLineNumber(6, l1);
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitVarInsn(ILOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLineNumber(7, l2);
        mv.visitInsn(RETURN);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitLocalVariable("args", "[Ljava/lang/String;", null, l0, l3, 0);
        mv.visitLocalVariable("z", "I", null, l1, l3, 1);
        mv.visitMaxs(2, 2);
        mv.visitEnd();

        cw.visitEnd();
        //将cw转换成字节数组写到文件里面去
        byte[] data = cw.toByteArray();
        File file = new File("E:\\bi\\ssl\\Student.class");
        FileOutputStream fout = new FileOutputStream(file);
        fout.write(data);
        fout.close();
    }


}
