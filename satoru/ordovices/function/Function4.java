package satoru.ordovices.function;

import satoru.util.tuple.Tuple3;
import satoru.util.tuple.Tuple4;

@FunctionalInterface
public interface Function4<A1, A2, A3, A4, R> extends Function {
    R primitiveApply(A1 a1, A2 a2, A3 a3, A4 a4);

    default R apply(A1 a1, A2 a2, A3 a3, A4 a4, R ifNull) {
        if (a1 == null || a2 == null || a3 == null || a4==null) {
            return ifNull;
        } else {
            return primitiveApply(a1, a2, a3, a4);
        }
    }

    default R apply(A1 a1, A2 a2, A3 a3, A4 a4) {
        return apply(a1, a2, a3, a4,null);
    }

    default R apply(Tuple4<A1,A2,A3,A4> a){
        return apply(a.a1(), a.a2(), a.a3(), a.a4());
    }
}
