package satoru.ordovices.parser;

import satoru.ordovices.function.Function1;
import satoru.ordovices.parse.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface Parser<T> {


    T primitiveParse(TokenList tl);

    default T parse(TokenList tl){
        if(tl.isFailed()){
            return null;
        }
        return primitiveParse(tl);
    }

    default <R> Parser<R> primitiveApply(Function1<T,R> f){
        return tl -> f.primitiveApply(parse(tl));
    }

    default <R> Parser<R> apply(Function1<T,R> f){
        return tl -> f.apply(parse(tl));
    }

    default Parser<T> between(Parser<?> open, Parser<?> close){
        return ParserTuple3.t(open, this, close).apply((o, p, c) -> p);
    }

    default Parser<List<T>> many(){
        return tl -> {
            List<T> ans = new ArrayList<>();
            int pos = tl.pos();

            T e = parse(tl);
            while (! tl.isFailed()){
                ans.add(e);
                pos = tl.pos();
                e = parse(tl);
            }
            tl.setFailed(null);
            tl.setPos(pos);
            return ans;
        };
    }

    default Parser<List<T>> many1(){
        return ParserTuple2.t(this, many()).apply((t, lt) -> {
            lt.add(0, t);
            return lt;
        });
    }

    default Parser<T> or(Parser<T> p){
        return tl -> {
            int pos = tl.pos();
            T ans = parse(tl);
            if(tl.isFailed()) {
                tl.setFailed(null);
                tl.setPos(pos);
                ans = p.parse(tl);
            }
            return ans;
        };    }

    default Parser<T> or(Supplier<Parser<T>> p){
        return tl -> {
            int pos = tl.pos();
            T ans = parse(tl);
            if(tl.isFailed()) {
                tl.setFailed(null);
                tl.setPos(pos);
                ans = p.get().parse(tl);
            }
            return ans;
        };
    }

    default <U> ParserTuple2<T,U> then(Parser<U> p){
        return new ParserTuple2<>(this, p);
    }

    default <U> LazyParserTuple2<T,U> then(Supplier<Parser<U>> p){
        return new LazyParserTuple2<>(this, p);
    }

    default <U> Parser<U> second(Parser<U> p){
        return new ParserTuple2<>(this, p).apply((t,u) -> u);
    }

    default <U> Parser<U> second(Supplier<Parser<U>> p){
        return new LazyParserTuple2<>(this, p).apply((t,u) -> u);
    }

    default <U> Parser<T> first(Parser<U> p){
        return new ParserTuple2<>(this, p).apply((t,u) -> t);
    }

    default <U> Parser<T> first(Supplier<Parser<U>> p){
        return new LazyParserTuple2<>(this, p).apply((t,u) -> t);
    }

    default <U> ParserTuple2<T,U> option(Parser<U> p){
        return then(p.or(success()));
    }

    Parser<Token> any = TokenList::next;

    static <T> Parser<T> eof(){
        return tl -> {
            if(!tl.hasNext()){
                tl.setFailed("eof expected");
            }
            return null;
        };
    }

    static <T> Parser<T> fail(String errMsg){
        return tl -> {
            tl.setFailed(errMsg);
            return null;
        };
    }

    static <T> Parser<T> success(){
        return tl -> null;
    }

    static Parser<Token> satisfy(Predicate<Token> tk, String errMsg){
        return tl -> {
            Token ans = any.parse(tl);
            if(tl.isFailed()) return null;
            if(tk.test(ans)){
                return ans;
            }else{
                tl.setFailed(errMsg);
                return null;
            }
        };
    }

    static Parser<Token> is(ITag tag){
        return satisfy(tk -> tk.tag().equals(tag), tag + " is expected");
    }

    static Parser<Token> is(String str){
        return satisfy(tk -> tk.str().equals(str), str + " is expected");
    }
}
