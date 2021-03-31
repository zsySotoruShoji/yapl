package satoru.ordovices.function;

@FunctionalInterface
public interface Function1<A,R> extends Function {
    R primitiveApply(A a);

    default R apply(A a, R ifNull){
        if(a == null){
            return ifNull;
        }else{
            return primitiveApply(a);
        }
    }

    default R apply(A a){
        return apply(a, null);
    }

    Function1<?,?> ID = a -> a;
}

