package satoru.ordovices.parser;

import satoru.ordovices.function.Function4;
import satoru.ordovices.function.Function5;
import satoru.ordovices.parse.TokenList;
import satoru.util.tuple.Tuple4;
import satoru.util.tuple.Tuple5;

import java.util.function.Supplier;

public class LazyParserTuple5<A1,A2,A3,A4,A5> implements Parser<Tuple5<A1,A2,A3,A4,A5>>{

    private final Parser<A1> p1;
    private final Supplier<Parser<A2>> p2;
    private final Supplier<Parser<A3>> p3;
    private final Supplier<Parser<A4>> p4;
    private final Supplier<Parser<A5>> p5;

    public LazyParserTuple5(
            Parser<A1> p1,
            Supplier<Parser<A2>> p2,
            Supplier<Parser<A3>> p3,
            Supplier<Parser<A4>> p4,
            Supplier<Parser<A5>> p5){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.p5 = p5;
    }

    public static <A1,A2,A3,A4,A5> LazyParserTuple5<A1,A2,A3,A4,A5> t(
            Parser<A1> p1,
            Supplier<Parser<A2>> p2,
            Supplier<Parser<A3>> p3,
            Supplier<Parser<A4>> p4,
            Supplier<Parser<A5>> p5){
        return new LazyParserTuple5<>(p1, p2, p3, p4, p5);
    }

    @Override
    public Tuple5<A1,A2,A3,A4,A5> primitiveParse(TokenList tl) {
        A1 a1 = p1.primitiveParse(tl);
        if(tl.isFailed()) return null;
        A2 a2 = p2.get().primitiveParse(tl);
        if(tl.isFailed()) return null;
        A3 a3 = p3.get().primitiveParse(tl);
        if(tl.isFailed()) return null;
        A4 a4 = p4.get().primitiveParse(tl);
        if(tl.isFailed()) return null;
        A5 a5 = p5.get().primitiveParse(tl);
        if(tl.isFailed()) return null;
        return new Tuple5<>(a1, a2, a3, a4, a5);
    }

    public <R> Parser<R> primitiveApply(Function5<A1,A2,A3,A4,A5,R> f){
        return tl -> {
            A1 a1 = p1.parse(tl);
            A2 a2 = p2.get().parse(tl);
            A3 a3 = p3.get().parse(tl);
            A4 a4 = p4.get().parse(tl);
            A5 a5 = p5.get().parse(tl);
            if(tl.isFailed()) return null;
            return f.primitiveApply(a1, a2, a3, a4, a5);
        };
    }

    public <R> Parser<R> apply(Function5<A1,A2,A3,A4,A5,R> f){
        return tl -> {
            A1 a1 = p1.parse(tl);
            A2 a2 = p2.get().parse(tl);
            A3 a3 = p3.get().parse(tl);
            A4 a4 = p4.get().parse(tl);
            A5 a5 = p5.get().parse(tl);
            if(tl.isFailed()) return null;
            return f.apply(a1, a2, a3, a4, a5);
        };
    }

}
