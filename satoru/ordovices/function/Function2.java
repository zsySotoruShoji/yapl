package satoru.ordovices.function;

import satoru.util.tuple.Tuple2;

@FunctionalInterface
public interface Function2<A1, A2, R> extends Function {
    R primitiveApply(A1 a1, A2 a2);

    default R apply(A1 a1, A2 a2, R ifNull) {
        if (a1 == null || a2 == null) {
            return ifNull;
        } else {
            return primitiveApply(a1, a2);
        }
    }

    default R apply(A1 a1, A2 a2) {
        return apply(a1, a2, null);
    }

    default R apply(Tuple2<A1,A2> a){
        return apply(a.a1(), a.a2());
    }
}
