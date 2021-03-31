package satoru.ordovices.parser;

import satoru.ordovices.function.Function3;
import satoru.ordovices.parse.Combinators;
import satoru.ordovices.parse.TokenList;
import satoru.util.MutableWrapper;
import satoru.util.tuple.Tuple3;

public class ParserTuple3<A1,A2,A3> implements Parser<Tuple3<A1,A2,A3>>{

    private final Tuple3<Parser<A1>, Parser<A2>, Parser<A3>> p;

    public ParserTuple3(Parser<A1> p1, Parser<A2> p2, Parser<A3> p3){
        if(p1==null || p2==null || p3==null){
            throw new IllegalArgumentException();
        }
        this.p = new Tuple3<>(p1, p2, p3);
    }

    public ParserTuple3(Parser<Tuple3<A1,A2,A3>> p){
        if(p==null) throw new IllegalArgumentException();
        MutableWrapper<Tuple3<A1,A2,A3>> tmp2 = new MutableWrapper<>(null);
        Parser<Tuple3<A1,A2,A3>> tmp = tl -> {
            tmp2.t = p.parse(tl);
            return tmp2.t;
        };
        this.p = new Tuple3<>(
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
            }
        );
    }

    public static <A1,A2,A3> ParserTuple3<A1,A2,A3> t(
            Parser<A1> p1, Parser<A2> p2, Parser<A3> p3){
        return new ParserTuple3<>(p1, p2, p3);
    }

    @Override
    public Tuple3<A1,A2,A3> primitiveParse(TokenList tl) {
        return new Tuple3<>(
                p.a1().primitiveParse(tl),
                p.a2().primitiveParse(tl),
                p.a3().primitiveParse(tl)
        );
    }

    @Override
    public ParserTuple3<A1,A2,A3> between(Parser<?> open, Parser<?> close){
        return new ParserTuple3<>(tl -> {
            open.parse(tl);
            Tuple3<A1,A2,A3> ans = this.parse(tl);
            close.parse(tl);
            return ans;
        });
    }

    public <R> Parser<R> primitiveApply(Function3<A1,A2,A3,R> f){
        return tl -> f.primitiveApply(p.a1().parse(tl),
                p.a2().parse(tl), p.a3().parse(tl));
    }

    public <R> Parser<R> apply(Function3<A1,A2,A3,R> f){
        return tl -> f.apply(p.a1().parse(tl),
                p.a2().parse(tl), p.a3().parse(tl));
    }

}
