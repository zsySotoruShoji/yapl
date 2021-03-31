package satoru.ordovices.parser;

import satoru.ordovices.function.Function2;
import satoru.ordovices.parse.Combinators;
import satoru.ordovices.parse.TokenList;
import satoru.util.MutableWrapper;
import satoru.util.tuple.Tuple2;

import java.util.function.Supplier;

public class LazyParserTuple2<A1, A2>
        implements Parser<Tuple2<A1,A2>>{

    private final Tuple2<Parser<A1>, Supplier<Parser<A2>>> p;

    public LazyParserTuple2(Parser<A1> p1, Supplier<Parser<A2>> p2){
        this.p = new Tuple2<>(p1, p2);
    }

    public static <A1,A2> LazyParserTuple2<A1,A2> t(
            Parser<A1> p1, Supplier<Parser<A2>> p2){
        return new LazyParserTuple2<>(p1, p2);
    }

    @Override
    public Tuple2<A1, A2> primitiveParse(TokenList tl) {
        A1 a1 = p.a1().primitiveParse(tl);
        if(tl.isFailed()) return null;
        return new Tuple2<>(a1, p.a2().get().primitiveParse(tl));
    }

    public <R> Parser<R> primitiveApply(Function2<A1,A2,R> f){
        return tl -> {
            A1 a1 = p.a1().parse(tl);
            if(tl.isFailed()) return null;
            return f.primitiveApply(a1, p.a2().get().parse(tl));
        };
    }

    public <R> Parser<R> apply(Function2<A1,A2,R> f){
        return tl -> {
            A1 a1 = p.a1().parse(tl);
            if(tl.isFailed()) return null;
            return f.apply(a1, p.a2().get().parse(tl));
        };
    }

}
