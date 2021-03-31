package satoru.ordovices.parse;

import satoru.ordovices.parser.*;

import static satoru.ordovices.parser.ParserTuple2.t;
import static satoru.ordovices.parser.LazyParserTuple3_3.t;

public final class Combinators {

    private Combinators(){}


//    @SafeVarargs
//    public static <T> Parser<T> or(Parser<T>... ps){
//        Parser<T> ans = ps[0];
//        if(ps.length==1) return ans;
//
//        for(int i=1; i < ps.length; i++){
//            ans = ans.or(ps[i]);
//        }
//        return ans;
//    }

    public static Parser<ASTNode> infixL(
            Parser<Token> operator,
            Parser<ASTNode> operand
    ){
        return t(
            operand,
            t(operator, operand).apply(
                    (k,l) -> new ASTNode(k, l)
            ).many())
            .apply((t, al) -> {
                if(al.isEmpty()) return t;

                ASTNode ans = al.get(0);
                ans.addChildren(0, t);
                for(int i=1; i < al.size(); i++){
                    ans = al.get(i);
                    ans.addChildren(0, al.get(i - 1));
                }
                return ans;
            }
        );
    }

    public static Parser<ASTNode> infixR(
            Parser<Token> operator,
            Parser<ASTNode> operand
    ){
        return t(operand, operator, () -> infixR(operator, operand)).apply(
                (n1,tk,n2) -> (ASTNode) new ASTNode(tk){{
                    addChildren(n1, n2);
                }}
        ).or(operand);
    }

}


