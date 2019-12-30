package org.springbus.classReader;

public class Box<T,R> {
    private  R r;
    private  T t;
    R getBox(){
        return r;
    }
    private void setAge(T t) {
        this.t =t;
    }

}
