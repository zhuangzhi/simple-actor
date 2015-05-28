package simpleactor.common.util;

public abstract class Tuple {
    public abstract int size();

    public static  <T1> Tuple1<T1> to(T1 t1) {return new Tuple1(t1);}
    public static  <T1,T2> Tuple2<T1,T2> to(T1 t1,T2 t2) {return new Tuple2<>(t1,t2);}


}
