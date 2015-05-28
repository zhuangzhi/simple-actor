package simpleactor.common.actor;

import simpleactor.common.util.Option;
import simpleactor.common.util.Some;

import java.util.List;

public class ActorContext  {
    final ActorPath path;
    final ActorSystem system;
    final ActorExecutor executor;

    List<ActorRef> childrens;
    ActorRef parent;

    public ActorContext(ActorPath path, ActorSystem system, ActorExecutor executor) {
        this.path = path;
        this.system = system;
        this.executor = executor;
    }

    public ActorPath getPath() {
        return path;
    }

    public ActorSystem getSystem() {
        return system;
    }

    public ActorExecutor getExecutor() {
        return executor;
    }
}

final class Address {
    final String protocol;
    final String system ;
    final Option<String> host;
    final Option<Integer> port;
    public Address(String protocol, String system, String host, Integer port) {
        this(protocol,system,new Some<>(host), new Some<>(port));
    }
    public Address(String protocol, String system, Option<String> host, Option<Integer> port) {
        this.protocol = protocol;
        this.system = system;
        this.host = host;
        this.port = port;
    }

    public Address(String protocol, String system) {
        this(protocol,system,Option.None,Option.None);
    }

    public boolean hasLocalScope() {return host.isEmpty();}
    public boolean hasGlobalScope() {return host.isDefined();}
    String cacheToString = null;
    @Override
    public String toString() {
        if (cacheToString==null) {
            StringBuilder sb = new StringBuilder(protocol).append("://").append(system);
            if (host.isDefined()) sb.append('@').append(host.get());
            if (port.isDefined()) sb.append(':').append(port.get());
            cacheToString = sb.toString();
        }
        return cacheToString;
    }

    public String hostPort() {return toString().substring(protocol.length()+3);}


}
