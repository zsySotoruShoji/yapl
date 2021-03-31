package satoru.ordovices.function;

import satoru.util.tuple.Tuple2;
import satoru.util.tuple.Tuple3;

@FunctionalInterface
public interface Function3<A1, A2, A3, R> extends Function {
    R primitiveApply(A1 a1, A2 a2, A3 a3);

    default R apply(A1 a1, A2 a2, A3 a3, R ifNull) {
        if (a1 == null || a2 == null || a3 == null) {
            return ifNull;
        } else {
            return primitiveApply(a1, a2, a3);
        }
    }

    default R apply(A1 a1, A2 a2, A3 a3) {
        return apply(a1, a2, a3, null);
    }

    default R apply(Tuple3<A1,A2,A3> a){
        return apply(a.a1(), a.a2(), a.a3());
    }
}
