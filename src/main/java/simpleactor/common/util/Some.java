package simpleactor.common.util;

public class Some <T> extends Option<T> {
    final T t;

    public Some(T t) {
        this.t = t;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public T get() {
        return t;
    }
}