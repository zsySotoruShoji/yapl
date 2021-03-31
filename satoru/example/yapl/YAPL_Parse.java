package satoru.example.yapl;

import satoru.ordovices.parse.ASTNode;
import satoru.ordovices.parse.TokenList;
import satoru.ordovices.parser.Parser;
import satoru.ordovices.tokenizer.Tokenizer;

import static satoru.example.yapl.YAPL_Enum.*;
import static satoru.ordovices.parser.ParserTuple4.t;
import static satoru.ordovices.parser.ParserTuple3.t;
import static satoru.ordovices.tokenizer.Tag.IDENTIFIER;
import static satoru.ordovices.tokenizer.Tag.INT_LITERAL;
import static satoru.ordovices.parser.Parser.is;


public class YAPL_Parse {

    static Parser<ASTNode> element(){
        return is(INT_LITERAL).or(is(IDENTIFIER)).apply(ASTNode::new)
                .or(() -> expr().between(is("("), is(")")))
                .or(() -> array())
                .or(() -> lambda().between(is("("), is(")")));
    }

    static Parser<ASTNode> expr(){
        return element().many().apply(es ->
                new ASTNode(EXPR.asToken(),
                        es.toArray(new ASTNode[0])));
    }

    static Parser<ASTNode> arrayLitr(){
        return element().many().apply(es ->
            new ASTNode(ARRAY.asToken(),
                    new ASTNode(ARRAY_LITR.asToken(),
                    es.toArray(new ASTNode[0]))
            )
        ).between(is("["), is("]"));
    }

    static Parser<ASTNode> lambda(){
        return t(is("\\"), is(IDENTIFIER), is("->"), expr())
                .apply((l,i,a,e) ->
            new ASTNode(LAMBDA.asToken(), i.asAST(), e)
        );
    }

    static Parser<ASTNode> arrayFunc(){
        return t(lambda(), is(":"), expr()).apply((l,c,e) ->
                new ASTNode(ARRAY.asToken(),
                l,
                new ASTNode(ARRAY_SIZE.asToken(), e)))
                .between(is("["), is("]"));
    }

    static Parser<ASTNode> array(){
        return arrayLitr().or(arrayFunc());
    }

    static Parser<ASTNode> orAssign
            = t(is(IDENTIFIER), is("="), expr())
            .apply((i,eq,ex) ->
                    new ASTNode(ASSIGN.asToken(), i.asAST(), ex))
            .or(expr());

    public static void main(String[] args) {
        String[] k = {"(", ")", "[", "]", "\\", "->", ":", "="};
        String[] s = {" "};
        Tokenizer tokenizer = new Tokenizer(k, s);

        TokenList tl = tokenizer.exec("a = 2");
        System.out.println(tl);
        var ast = orAssign.parse(tl);
        System.out.println(ast);
        System.out.println(tl);
    }





















}
