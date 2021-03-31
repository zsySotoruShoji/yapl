package satoru.ordovices.parser;

import satoru.ordovices.function.Function4;
import satoru.ordovices.parse.Combinators;
import satoru.ordovices.parse.TokenList;
import satoru.util.tuple.Tuple4;

import java.util.function.Supplier;

public class LazyParserTuple4<A1,A2,A3,A4> implements Parser<Tuple4<A1,A2,A3,A4>>{

    private final Parser<A1> p1;
    private final Parser<A2> p2;
    private final Parser<A3> p3;
    private final Supplier<Parser<A4>> p4;

    public LazyParserTuple4(
            Parser<A1> p1,
            Parser<A2> p2,
            Parser<A3> p3,
            Supplier<Parser<A4>> p4){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    public static <A1,A2,A3,A4> LazyParserTuple4<A1,A2,A3,A4> t(
            Parser<A1> p1,
            Parser<A2> p2,
            Parser<A3> p3,
            Supplier<Parser<A4>> p4){
        return new LazyParserTuple4<>(p1, p2, p3, p4);
    }

    @Override
    public Tuple4<A1,A2,A3,A4> primitiveParse(TokenList tl) {
        A1 a1 = p1.primitiveParse(tl);
        A2 a2 = p2.primitiveParse(tl);
        A3 a3 = p3.primitiveParse(tl);
        if(tl.isFailed()) return null;
        return new Tuple4<>(a1, a2, a3, p4.get().primitiveParse(tl));
    }

    public <R> Parser<R> primitiveApply(Function4<A1,A2,A3,A4,R> f){
        return tl -> {
            A1 a1 = p1.parse(tl);
            A2 a2 = p2.parse(tl);
            A3 a3 = p3.parse(tl);
            if(tl.isFailed()) return null;
            return f.primitiveApply(a1, a2, a3, p4.get().parse(tl));
        };
    }

    public <R> Parser<R> apply(Function4<A1,A2,A3,A4,R> f){
        return tl -> {
            A1 a1 = p1.parse(tl);
            A2 a2 = p2.parse(tl);
            A3 a3 = p3.parse(tl);
            if(tl.isFailed()) return null;
            return f.apply(a1, a2, a3, p4.get().parse(tl));
        };
    }

}
