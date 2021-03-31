package satoru.ordovices.parser;

import satoru.ordovices.function.Function3;
import satoru.ordovices.parse.Combinators;
import satoru.ordovices.parse.TokenList;
import satoru.util.tuple.Tuple3;

import java.util.function.Supplier;

public class LazyParserTuple3_2_3<A1,A2,A3> implements Parser<Tuple3<A1,A2,A3>>{

    private final Parser<A1> p1;
    private final Supplier<Parser<A2>> p2;
    private final Supplier<Parser<A3>> p3;

    public LazyParserTuple3_2_3(
            Parser<A1> p1, Supplier<Parser<A2>> p2, Supplier<Parser<A3>> p3){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public static <A1,A2,A3> LazyParserTuple3_2_3<A1,A2,A3> t(
            Parser<A1> p1,
            Supplier<Parser<A2>> p2,
            Supplier<Parser<A3>> p3){
        return new LazyParserTuple3_2_3<>(p1, p2, p3);
    }

    @Override
    public Tuple3<A1,A2,A3> primitiveParse(TokenList tl) {
        A1 a1 = p1.primitiveParse(tl);
        if(tl.isFailed()) return null;
        A2 a2 = p2.get().primitiveParse(tl);
        if(tl.isFailed()) return null;
        return new Tuple3<>(a1, a2, p3.get().primitiveParse(tl));
    }

    public <R> Parser<R> primitiveApply(Function3<A1,A2,A3,R> f){
        return tl -> {
            A1 a1 = p1.parse(tl);
            if(tl.isFailed()) return null;
            A2 a2 = p2.get().parse(tl);
            if(tl.isFailed()) return null;
            return f.primitiveApply(a1, a2, p3.get().parse(tl));
        };
    }

    public <R> Parser<R> apply(Function3<A1,A2,A3,R> f){
        return tl -> {
            A1 a1 = p1.parse(tl);
            if(tl.isFailed()) return null;
            A2 a2 = p2.get().parse(tl);
            if(tl.isFailed()) return null;
            return f.apply(a1, a2, p3.get().parse(tl));
        };
    }

}
