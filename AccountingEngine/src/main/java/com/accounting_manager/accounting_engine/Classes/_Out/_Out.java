package com.accounting_manager.accounting_engine.Classes._Out;

public class _Out<E> {
    E ref;
    
    public _Out( ){
        ref = null;
    }
    
    public _Out(E e ){
        ref = e;
    }
    public E get() { return ref; }
    public void set( E e ){ this.ref = e; }

    public String toString() {
        return ref.toString();
    }
}