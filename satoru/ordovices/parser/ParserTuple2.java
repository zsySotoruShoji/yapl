package satoru.ordovices.parser;

import satoru.ordovices.function.Function2;
import satoru.ordovices.parse.TokenList;
import satoru.util.MutableWrapper;
import satoru.util.tuple.Tuple2;

public class ParserTuple2<A1, A2> extends ParserTuple
        implements Parser<Tuple2<A1,A2>>{

    private final Tuple2<Parser<A1>, Parser<A2>> p;

    public ParserTuple2(Parser<A1> p1, Parser<A2> p2){
        if(p1==null || p2==null){
            throw new IllegalArgumentException();
        }
        this.p = new Tuple2<>(p1, p2);
    }

    public ParserTuple2(Parser<Tuple2<A1,A2>> p){
        MutableWrapper<Tuple2<A1,A2>> tmp2 = new MutableWrapper<>(null);
        Parser<Tuple2<A1,A2>> tmp = tl -> {
            tmp2.t = p.parse(tl);
            return tmp2.t;
        };
        this.p = new Tuple2<>(
                tl -> {
                    tmp.parse(tl);
                    if(tl.isFailed()) return null;
                    return tmp2.t.a1();
                },
                tl -> {
                    if (tl.isFailed()) return null;
                    return tmp2.t.a2();
                }
        );
    }

    public static <A1,A2> ParserTuple2<A1,A2> t(Parser<A1> p1, Parser<A2> p2){
        return new ParserTuple2<>(p1, p2);
    }

    @Override
    public Tuple2<A1,A2> primitiveParse(TokenList tl) {
        return new Tuple2<>(p.a1().primitiveParse(tl),
                p.a2().primitiveParse(tl));
    }

    @Override
    public ParserTuple2<A1,A2> between(Parser<?> open, Parser<?> close){
        return new ParserTuple2<>(tl -> {
            open.parse(tl);
            Tuple2<A1,A2> ans = this.parse(tl);
            close.parse(tl);
            return ans;
        });
    }

    public <R> Parser<R> primitiveApply(Function2<A1,A2,R> f){
        return tl -> f.primitiveApply(
                p.a1().parse(tl),
                p.a2().parse(tl)
        );
    }

    public <R> Parser<R> apply(Function2<A1,A2,R> f){
        return tl -> f.apply(
                p.a1().parse(tl),
                p.a2().parse(tl)
        );
    }



}
