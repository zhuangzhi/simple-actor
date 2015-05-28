package simpleactor.common.util;

public class Tuple2<T1,T2> extends Tuple1<T1> {
    T2 t2;

    public Tuple2() {
    }

    public Tuple2(T1 t1, T2 t2) {
        super(t1);
        this.t2 = t2;
    }

    public T2 getT2() {
        return t2;
    }

    public void setT2(T2 t2) {
        this.t2 = t2;
    }

    @Override
    public int size() {
        return 2;
    }
}
