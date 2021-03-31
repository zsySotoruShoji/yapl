package satoru.ordovices.parser;

import satoru.ordovices.function.Function5;
import satoru.ordovices.parse.Combinators;
import satoru.ordovices.parse.TokenList;
import satoru.util.MutableWrapper;
import satoru.util.tuple.Tuple5;

public class ParserTuple5<A1,A2,A3,A4,A5>
        implements Parser<Tuple5<A1,A2,A3,A4,A5>>{

    private final Tuple5<Parser<A1>, Parser<A2>,
            Parser<A3>, Parser<A4>, Parser<A5>> p;

    public ParserTuple5(
            Parser<A1> p1,
            Parser<A2> p2,
            Parser<A3> p3,
            Parser<A4> p4,
            Parser<A5> p5){
        if(p1==null || p2==null || p3==null || p4==null || p5==null){
            throw new IllegalArgumentException();
        }
        p = new Tuple5<>(p1, p2, p3, p4, p5);
    }

    public ParserTuple5(Parser<Tuple5<A1,A2,A3,A4,A5>> p){
        if(p==null) throw new IllegalArgumentException();
        MutableWrapper<Tuple5<A1,A2,A3,A4,A5>> tmp2 = new MutableWrapper<>(null);
        Parser<Tuple5<A1,A2,A3,A4,A5>> tmp = tl -> {
            tmp2.t = p.parse(tl);
            return tmp2.t;
        };
        this.p = new Tuple5<>(
                tl -> {
                    tmp.parse(tl);
                    if(tl.isFailed()) return null;
                    return tmp2.t.a1();
                },
                tl -> {
                    if(tl.isFailed()) return null;
                    return tmp2.t.a2();
                },
                tl -> {
                    if(tl.isFailed()) return null;
                    return tmp2.t.a3();
                },
                tl -> {
                    if(tl.isFailed()) return null;
                    return tmp2.t.a4();
                },
                tl -> {
                    if(tl.isFailed()) return null;
                    return tmp2.t.a5();
                }
        );
    }

    public static <A1,A2,A3,A4,A5> ParserTuple5<A1,A2,A3,A4,A5> t(
            Parser<A1> p1,
            Parser<A2> p2,
            Parser<A3> p3,
            Parser<A4> p4,
            Parser<A5> p5){
        return new ParserTuple5<>(p1, p2, p3, p4, p5);
    }

    @Override
    public Tuple5<A1,A2,A3,A4,A5> primitiveParse(TokenList tl) {
        return new Tuple5<>(
                p.a1().primitiveParse(tl),
                p.a2().primitiveParse(tl),
                p.a3().primitiveParse(tl),
                p.a4().primitiveParse(tl),
                p.a5().primitiveParse(tl)

        );
    }

    public <R> Parser<R> primitiveApply(Function5<A1,A2,A3,A4,A5,R> f){
        return tl -> f.primitiveApply(
                p.a1().parse(tl),
                p.a2().parse(tl),
                p.a3().parse(tl),
                p.a4().parse(tl),
                p.a5().parse(tl)
        );
    }

    public <R> Parser<R> apply(Function5<A1,A2,A3,A4,A5,R> f){
        return tl -> f.apply(
                p.a1().parse(tl),
                p.a2().parse(tl),
                p.a3().parse(tl),
                p.a4().parse(tl),
                p.a5().parse(tl)
        );
    }

}
