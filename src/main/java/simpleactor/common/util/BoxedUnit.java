package simpleactor.common.util;

public final class BoxedUnit implements java.io.Serializable {
    private static final long serialVersionUID = 8405543498931817370L;

    public final static BoxedUnit UNIT = new BoxedUnit();

    public final static Class<Void> TYPE = java.lang.Void.TYPE;

    private Object readResolve() { return UNIT; }

    private BoxedUnit() { }

    public boolean equals(java.lang.Object other) {
        return this == other;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        return "()";
    }
}
