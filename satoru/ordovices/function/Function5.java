package satoru.ordovices.function;

import satoru.util.tuple.Tuple5;

@FunctionalInterface
public interface Function5<A1, A2, A3, A4, A5, R> extends Function {
    R primitiveApply(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5);

    default R apply(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5, R ifNull) {
        if (a1==null || a2==null || a3==null || a4==null || a5==null) {
            return ifNull;
        } else {
            return primitiveApply(a1, a2, a3, a4, a5);
        }
    }

    default R apply(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5) {
        return apply(a1, a2, a3, a4, a5, null);
    }

    default R apply(Tuple5<A1,A2,A3,A4,A5> a){
        return apply(a.a1(), a.a2(), a.a3(), a.a4(), a.a5());
    }
}
