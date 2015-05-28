package simpleactor.common.util;

public class Tuple1<T1> extends Tuple {
    T1 t1;

    public Tuple1() {
    }

    public Tuple1(T1 t1) {
        this.t1 = t1;
    }

    public T1 getT1() {
        return t1;
    }

    public void setT1(T1 t1) {
        this.t1 = t1;
    }

    @Override
    public int size() {
        return 1;
    }
}
