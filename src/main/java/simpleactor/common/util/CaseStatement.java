package simpleactor.common.util;

public class CaseStatement<F, P, T>
        implements PartialFunction<F, T> {
    final FI.Predicate predicate;
    final FI.Apply<P, T> apply;
    static public PartialFunction empty = empty_pf;


    public CaseStatement(FI.Predicate predicate, FI.Apply<P, T> apply) {
        this.predicate = predicate;
        this.apply = apply;
    }

    @Override
    public T apply(F f) {
        try {
            return apply.apply((P)f);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isDefinedX(F x) {
        return predicate.defined(x);
    }

}


class UnitCaseStatement<F>
        implements PartialFunction<F, Object> {
    final FI.Predicate<F> predicate;
    final FI.UnitApply<F> apply;

    public UnitCaseStatement(FI.Predicate<F> predicate, FI.UnitApply<F> apply) {
        this.predicate = predicate;
        this.apply = apply;
    }

    @Override
    public Object apply(F f) {
        try {
            apply.apply(f);
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public boolean isDefinedX(F x) {
        return predicate.defined(x);
    }
}
