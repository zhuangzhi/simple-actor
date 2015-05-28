package simpleactor.common.util;

final public class Unit {
    static BoxedUnit box(Unit x) {return BoxedUnit.UNIT;}
    static Unit unbox(Object x) {return new Unit();}

    @Override
    public String toString() {
        return "actor unit";
    }
}
