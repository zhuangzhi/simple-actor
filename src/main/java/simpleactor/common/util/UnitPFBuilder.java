package simpleactor.common.util;
/**
 * Copyright (C) 2009-2014 Typesafe Inc. <http://www.typesafe.com>
 */

/**
 * A builder for {@link scala.PartialFunction}.
 * This is a specialized version of {@link } to map java
 * void methods to {@link scala.runtime.BoxedUnit}.
 *
 *
 * This is an EXPERIMENTAL feature and is subject to change until it has received more real world testing.
 */
public final class UnitPFBuilder extends AbstractPFBuilder<Object, Object> {

    /**
     * Create a UnitPFBuilder.
     */
    public UnitPFBuilder() {
    }

    /**
     * Add a new case statement to this builder.
     *
     * @param type   a type to match the argument against
     * @param apply  an action to apply to the argument if the type matches
     * @return       a builder with the case statement added
     */
    public <I> UnitPFBuilder match(final Class<I> type,
                                      final FI.UnitApply<I> apply) {
        addStatement(new UnitCaseStatement(
                type::isInstance, apply));
        return this;
    }

    /**
     * Add a new case statement to this builder.
     *
     * @param type       a type to match the argument against
     * @param predicate  a predicate that will be evaluated on the argument if the type matches
     * @param apply      an action to apply to the argument if the type matches and the predicate returns true
     * @return           a builder with the case statement added
     */
    public <I> UnitPFBuilder match(final Class<I> type,
                                      final FI.TypedPredicate<I> predicate,
                                      final FI.UnitApply<I> apply) {
        addStatement(new UnitCaseStatement(
                 o -> type.isInstance(o) && predicate.defined(type.cast(o)), apply));
        return this;
    }

    /**
     * Add a new case statement to this builder.
     *
     * @param object  the object to compare equals with
     * @param apply  an action to apply to the argument if the object compares equal
     * @return a builder with the case statement added
     */
    public <I> UnitPFBuilder matchEquals(final I object,
                                            final FI.UnitApply<I> apply) {
        addStatement(new UnitCaseStatement(
                object::equals, apply));
        return this;
    }

    /**
     * Add a new case statement to this builder.
     *
     * @param object  the object to compare equals with
     * @param predicate  a predicate that will be evaluated on the argument if the object compares equal
     * @param apply  an action to apply to the argument if the object compares equal
     * @return a builder with the case statement added
     */
    public  <I> UnitPFBuilder matchEquals(final I object,
                                            final FI.TypedPredicate<I> predicate,
                                            final FI.UnitApply<I> apply) {
        addStatement(new UnitCaseStatement(
                o -> object.equals(o) && predicate.defined((I)o), apply));
        return this;
    }

    /**
     * Add a new case statement to this builder, that matches any argument.
     * @param apply  an action to apply to the argument
     * @return       a builder with the case statement added
     */
    public <I> UnitPFBuilder matchAny(final FI.UnitApply<I> apply) {
        addStatement(new UnitCaseStatement(
                o -> true, apply));
        return this;
    }
}
