package satoru.ordovices.parser;

import satoru.ordovices.function.Function4;
import satoru.ordovices.parse.Combinators;
import satoru.ordovices.parse.TokenList;
import satoru.util.MutableWrapper;
import satoru.util.tuple.Tuple4;

public class ParserTuple4<A1,A2,A3,A4> implements Parser<Tuple4<A1,A2,A3,A4>>{

    private final Tuple4<Parser<A1>, Parser<A2>, Parser<A3>, Parser<A4>> p;

    public ParserTuple4(
            Parser<A1> p1,
            Parser<A2> p2,
            Parser<A3> p3,
            Parser<A4> p4){
        if(p1==null || p2==null || p3==null || p4==null){
            throw new IllegalArgumentException();
        }
        this.p = new Tuple4<>(p1, p2, p3, p4);
    }

    public ParserTuple4(Parser<Tuple4<A1,A2,A3,A4>> p){
        if(p==null) throw new IllegalArgumentException();
        MutableWrapper<Tuple4<A1,A2,A3,A4>> tmp2 = new MutableWrapper<>(null);
        Parser<Tuple4<A1,A2,A3,A4>> tmp = tl -> {
            tmp2.t = p.parse(tl);
            return tmp2.t;
        };
        this.p = new Tuple4<>(
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
                }
        );
    }

    public static <A1,A2,A3,A4> ParserTuple4<A1,A2,A3,A4> t(
            Parser<A1> p1,
            Parser<A2> p2,
            Parser<A3> p3,
            Parser<A4> p4){
        return new ParserTuple4<>(p1, p2, p3, p4);
    }


    @Override
    public Tuple4<A1,A2,A3,A4> primitiveParse(TokenList tl) {
        return new Tuple4<>(
                p.a1().primitiveParse(tl),
                p.a2().primitiveParse(tl),
                p.a3().primitiveParse(tl),
                p.a4().primitiveParse(tl));
    }

    public <R> Parser<R> primitiveApply(Function4<A1,A2,A3,A4,R> f){
        return tl -> f.primitiveApply(
                p.a1().parse(tl),
                p.a2().parse(tl),
                p.a3().parse(tl),
                p.a4().parse(tl));
    }

    public <R> Parser<R> apply(Function4<A1,A2,A3,A4,R> f){
        return tl -> f.apply(
                p.a1().parse(tl),
                p.a2().parse(tl),
                p.a3().parse(tl),
                p.a4().parse(tl));
    }

}
