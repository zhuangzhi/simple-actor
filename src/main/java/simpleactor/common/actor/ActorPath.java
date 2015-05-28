package simpleactor.common.actor;

import simpleactor.common.util.Tuple;
import simpleactor.common.util.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public interface ActorPath extends Comparable<ActorPath> {
    Address address();
    String name();
    ActorPath parent();
    ActorPath child(String child);
    default ActorPath child(Iterator<String> names) {
        ActorPath path = this;
        while(names.hasNext()){
            path = path.child(names.next());
        }
        return path;
    }
    Iterable<String> getElements();
    RootActorPath root() ;
    default String toStringWithoutAddress() {
        StringBuilder sb = new StringBuilder();
        getElements().forEach(x->sb.append("/").append(x));
        return sb.toString();
    }

//
    Integer undefinedUid = 0;
    List<String> emptyActorPath = new ArrayList<>();

    int uid();
    ActorPath withUid(int uid);



    static Tuple2<String, Integer> splitNameAndUid(String path) {
        int i = path.indexOf('#');
        if(i<0) return Tuple.to(path, undefinedUid);
        return Tuple.to(path.substring(0,i), Integer.valueOf(path.substring(i+1)));
    }
}

final class RootActorPath implements ActorPath {

    final Address address;
    final String name;
    public RootActorPath(Address address) {
        this(address,"/");
    }

    public RootActorPath(Address address, String name) {
        this.address = address;
        this.name = name;
    }

    @Override
    public Address address() {
        return address;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ActorPath parent() {
        return this;
    }

    @Override
    public ActorPath child(String child) {
        Tuple2<String, Integer> t = ActorPath.splitNameAndUid(child);
        return new ChildActorPath(this, t.getT1(),t.getT2());
    }

    @Override
    public Iterable<String> getElements() {
        return ActorPath.emptyActorPath;
    }

    @Override
    public RootActorPath root() {
        return this;
    }

    @Override
    public int uid() {
        return ActorPath.undefinedUid;
    }

    @Override
    public ActorPath withUid(int uid) {
        if (uid==ActorPath.undefinedUid) return this;
        throw  new IllegalStateException(String.format("RootActorPath must have undefinedUid, %d!=%d", uid, ActorPath.undefinedUid));
    }

    @Override
    public int compareTo(ActorPath o) {
        if (o instanceof RootActorPath) {
            return toString().compareTo(o.toString());
        }
        return 1;
    }

    @Override
    public String toString() {
        return address + name;
    }
}

class ChildActorPath implements ActorPath {
    final ActorPath parent;
    final String name;
    final Integer uid;

    public ChildActorPath(ActorPath parent, String name, Integer uid) {
        this.parent = parent;
        this.name = name;
        this.uid = uid;
    }

    public ChildActorPath(ActorPath parent, String name) {
        this(parent,name,ActorPath.undefinedUid);
    }

    @Override
    public int uid() {
        return uid;
    }

    @Override
    public ActorPath withUid(int uid) {
        if (this.uid.equals(uid)) {
            return this;
        }
        return new ChildActorPath(parent, name, uid);
    }

    @Override
    public Address address() {
        return root().address();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ActorPath parent() {
        return parent;
    }

    @Override
    public ActorPath child(String child) {
        Tuple2<String, Integer> t = ActorPath.splitNameAndUid(child);
        return new ChildActorPath(this, t.getT1(),t.getT2());
    }

    @Override
    public Iterable<String> getElements() {
        List<String> list = new ArrayList<>();
        ActorPath p = this;
        while(!(p instanceof RootActorPath)) {
            list.add(p.name());
            p = p.parent();
        }
        return list;
    }

    @Override
    public RootActorPath root() {
        ActorPath ap = this;
        while(!(ap instanceof RootActorPath)) {
            ap = ap.parent();
        }
        return (RootActorPath)ap;
    }

    @Override
    public int compareTo(ActorPath o) {
        return 0;
    }

    @Override
    public String toString() {
        int length = toStringLength();
        return address() + name;
    }

    private int toStringLength () {
        return toStringOffset() + name.length();
    }

    private int toStringOffset() {
        if (parent instanceof RootActorPath) {
            return parent.address().toString().length() + parent.name().length();
        } else {
            return ((ChildActorPath)parent).toStringLength()+1;
        }
    }

    private StringBuilder buildToString(StringBuilder sb, int length, int diff,  Function<RootActorPath, String> rootString) {
        ActorPath p = this;
        sb.setLength(length);
        while(!(p instanceof RootActorPath)) {
            ChildActorPath c = (ChildActorPath)p;
            int start = c.toStringLength() + diff;
            int end = start + c.name.length();
            sb.replace(start, end, c.name);
            if (p!=this) {
                sb.replace(end, end+1, "/");
            }
            p = p.parent();
        }
        String rstr = rootString.apply((RootActorPath)p);
        sb.replace(0, rstr.length(),rstr);
        return sb;
    }
}
