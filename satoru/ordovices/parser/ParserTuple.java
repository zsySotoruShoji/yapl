package satoru.ordovices.parser;

import satoru.ordovices.function.Function;
import satoru.ordovices.parse.TokenList;
import satoru.util.tuple.Tuple;

public abstract class ParserTuple {

    public abstract Tuple primitiveParse(TokenList tl);

    public abstract ParserTuple between(Parser<?> open, Parser<?> close);


}
