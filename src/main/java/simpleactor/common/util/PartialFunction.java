package simpleactor.common.util;

import java.util.function.Function;

/**
 * @author <a href="mailto:zhuangzhi.liu@gmail.com">Zhuangzhi Liu</a>
 *         Created on: 2015/4/22
 */
public interface PartialFunction <A,B> {
    B apply(A a);
    default boolean isDefinedX(A x) {
        return false;
    }


    default B applyOrElse(A  a, PartialFunction<A,B> f2) {
        return isDefinedX(a)?apply(a):f2.apply(a);
    }

    default PartialFunction<A,B> orElse(PartialFunction<A,B> that) {
        return new OrElse<>(this,that);
    }

    default <V> PartialFunction<A,V> andThen(PartialFunction<? super B,? extends V> that) {
        return new AndThen<>(this,that);
    }
    default PartialFunction<A,B> apply(Function<A,B> f) {
        return f::apply;
    }

    default Function<A,Option<B>> lift() {return new Lifted<>(this);}

    PartialFunction empty_pf = new PartialFunction() {
        @Override
        public Object apply(Object o) {
            throw new NoSuchMethodError();
        }

        @Override
        public boolean isDefinedX(Object x) {
            return false;
        }

        @Override
        public PartialFunction orElse(PartialFunction that) {
            return that;
        }

        @Override
        public Function lift() {
            return x -> Option.None;
        }

        @Override
        public boolean runWith(Function action, Object x) {
            return false;
        }

        @Override
        public PartialFunction andThen(PartialFunction that) {
            return this;
        }
    };

    default <U> boolean runWith(Function<B,U> action, A x) {
        Check<A,B> c = new Check<>();
        B z = applyOrElse(x,c);
        if (c.successful()) {
            action.apply(z);
            return true;
        } else {
            return false;
        }
    }

    static <T> Function<T, T> identity() {
        return t -> t;
    }

}
class Check<A,B> implements PartialFunction<A,B>{
    boolean r = true;
    @Override
    public B apply(A a) {
        r=false;
        return null;
    }
    public boolean isFailed() {return !r;}
    public boolean successful() {return r;}
}

class Lifted<A,B> implements Function<A,Option<B>> {
    final PartialFunction<A,B> pf;

    public Lifted(PartialFunction<A, B> pf) {
        this.pf = pf;
    }

    @Override
    public Option<B> apply(A a) {
        Check<A,B> c = new Check<>();
        B z = pf.applyOrElse(a, c);
        return c.successful()? new Some<>(z):Option.None;
    }
}


class OrElse<F,T> implements PartialFunction<F,T> {
    final PartialFunction<F,T> f1;
    final PartialFunction<F,T> f2;

    public OrElse(PartialFunction<F,T> f1, PartialFunction<F,T> f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    @Override
    public T apply(F f) {
        return f1.applyOrElse(f,f2);
    }

    @Override
    public T applyOrElse(F f, PartialFunction<F, T> defFunc) {
        Check<F,T> c = new Check<>();
        T z = f1.applyOrElse(f,c);
        return c.successful()?z:f2.applyOrElse(f,defFunc);
    }

    @Override
    public PartialFunction<F, T> orElse(PartialFunction<F, T> that) {
        return new OrElse<>(f1,f2.orElse(that));
    }

    @Override
    public <V> PartialFunction<F, V> andThen(PartialFunction<?super T, ?extends V> that) {
        return new OrElse<>(f1.andThen(that),f2.andThen(that));
    }
}

class AndThen<F,T,V> implements PartialFunction<F,V> {
    final PartialFunction<F,T> f1;
    final PartialFunction<?super T,?extends V> f2;

    public AndThen(PartialFunction<F,T> f1, PartialFunction<?super T,?extends V> f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    @Override
    public V apply(F f) {
        return f2.apply(f1.apply(f));
    }

    @Override
    public  V applyOrElse(F f, PartialFunction<F, V> defFunc) {
        Check<F,T> c = new Check<>();
        T z = f1.applyOrElse(f,c);
        return c.successful()?f2.apply(z):defFunc.apply(f);
    }

}

