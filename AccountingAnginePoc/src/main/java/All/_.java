package All;

public class _<E> {
    E ref;
    public _( E e ){
        ref = e;
    }
    public E get() { return ref; }
    public void set( E e ){ this.ref = e; }

    public String toString() {
        return ref.toString();
    }
}