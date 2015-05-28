package simpleactor.common.util;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:zhuangzhi.liu@gmail.com">Zhuangzhi Liu</a>
 *         Created on: 2015/4/22
 */
public abstract class Option<A> {

    public Option() {
    }

    public abstract boolean isEmpty();

    public abstract A get() ;

    public boolean isDefined() {return !isEmpty();}

    public A getOrElse(Supplier<A> def) {
        return isEmpty()?def.get():get();
    }

    public A orNull() {
        return isEmpty()? null:get();
    }

    public <B> Option<B> map(Function<A,B> f) {
        return isEmpty()?None:new Some<>(f.apply(get()));
    }

    public <B> B fold(Supplier<B> ifEmpty,Function<A,B> f) {
        return (isEmpty()) ? ifEmpty.get():f.apply(get());
    }

    public <B> Option<B> flatMap(Function<A,Option<B>> f) {
        return isEmpty() ? None : f.apply(get());
    }

    public Option<A> filter(Predicate<A> p) {
        return (isEmpty()||p.test(get())) ? this : None;
    }

    public Option<A> filterNot(Predicate<A> p) {
        return (isEmpty()||!p.test(get())) ? this : None;
    }

    static class  NoneClass extends  Option {

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Object get() {
            throw new NoSuchElementException("None.get");
        }
    }

    public static NoneClass None = new NoneClass();

}
