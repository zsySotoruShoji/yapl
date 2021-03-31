package satoru.ordovices.parser;

import satoru.ordovices.parse.Combinators;
import satoru.ordovices.parse.TokenList;

import static satoru.ordovices.parser.LazyParserTuple3_2.*;

import java.util.function.Supplier;

public class LazyParser<T> implements Parser<T>{

    private final Supplier<Parser<T>> parser;

    public LazyParser(Supplier<Parser<T>> parser){
        if (parser == null){
            throw new IllegalArgumentException();
        }
        this.parser = parser;
    }

    public static <T> LazyParser<T> t(Supplier<Parser<T>> p){
        return new LazyParser<>(p);
    }

    @Override
    public T primitiveParse(TokenList tl) {
        return parser.get().primitiveParse(tl);
    }

    @Override
    public Parser<T> between(Parser<?> open, Parser<?> close){
        return LazyParserTuple3_2.t(open, parser, close)
                .apply((o,p,c) -> p);
    }
}
