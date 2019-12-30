package org.springbus.classReader;

import org.objectweb.asm.*;

import java.io.IOException;

public class ClassReaderAgent  {



    private  static  void readFile() throws IOException {

        String className="org.springbus.classReader.Box";
        className="java.lang.String";
        ClassReader reader=new ClassReader(className);
        P("interface="+reader.getInterfaces());
        P("className="+reader.getClassName());
        P("itemCount="+reader.getItemCount());

        reader.accept(new ClassVisitor(Opcodes.ASM7) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                //P(" visit->access"+ access +" name="+name +  " signature="+signature);


                super.visit(version, access, name, signature, superName, interfaces);
            }

            @Override
            public void visitSource(String source, String debug) {
                P(" source="+  source);
                super.visitSource(source, debug);
            }

            @Override
            public ModuleVisitor visitModule(String name, int access, String version) {
                return super.visitModule(name, access, version);
            }

            @Override
            public void visitNestHost(String nestHost) {
                super.visitNestHost(nestHost);
            }

            @Override
            public void visitOuterClass(String owner, String name, String descriptor) {
                super.visitOuterClass(owner, name, descriptor);
            }

            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                P(" descriptor="+  descriptor);
                return super.visitAnnotation(descriptor, visible);
            }

            @Override
            public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
                P(" visitTypeAnnotation="+  descriptor);
                return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
            }

            @Override
            public void visitAttribute(Attribute attribute) {
                super.visitAttribute(attribute);
            }

            @Override
            public void visitNestMember(String nestMember) {
                super.visitNestMember(nestMember);
            }

            @Override
            public void visitInnerClass(String name, String outerName, String innerName, int access) {
                super.visitInnerClass(name, outerName, innerName, access);
            }

            @Override
            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
               // P(" field access"+ access +" name="+name + " descriptor ="+descriptor  +" signature="+signature);
                return super.visitField(access, name, descriptor, signature, value);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                P(" v->access"+ access +" name="+name + " descriptor ="+descriptor  +" signature="+signature);

                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }

            @Override
            public void visitEnd() {
                super.visitEnd();
            }
        }, 2);

    }
    public  static void  main(String[] args) throws IOException {
        readFile();
    }

    private  static  void  P(Object o) {
        System.out.println(o);
    }
}
