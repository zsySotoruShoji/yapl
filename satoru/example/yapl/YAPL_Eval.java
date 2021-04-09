package satoru.example.yapl;

import satoru.ordovices.parse.ASTNode;
import satoru.ordovices.parse.ITag;
import satoru.ordovices.parse.TokenList;
import satoru.ordovices.tokenizer.Tokenizer;
import satoru.util.MutableWrapper;

import java.util.*;

import static satoru.example.yapl.YAPL_Associative.LEFT;
import static satoru.example.yapl.YAPL_Enum.*;
import static satoru.example.yapl.YAPL_NotionOfFunction.PREFIX;
import static satoru.example.yapl.YAPL_NotionOfFunction.SUFFIX;
import static satoru.ordovices.tokenizer.Tag.IDENTIFIER;
import static satoru.ordovices.tokenizer.Tag.INT_LITERAL;

public class YAPL_Eval {

    final Map<String, YAPL_Object> binds = new HashMap<>();

    public YAPL_Eval(){}

    public YAPL_Eval(Map<String, YAPL_Object> binds){
        this.binds.putAll(binds);
    }

    public YAPL_Eval(YAPL_Eval def, String name, YAPL_Object obj){
        this.binds.putAll(def.binds);
        this.binds.put(name, obj);
    }

    public YAPL_Object eval(ASTNode ast){

        ITag tag = ast.self().tag();
        if(tag == INT_LITERAL){
            return new YAPL_Int(Integer.parseInt(ast.self().str()));
        }
        if(tag == IDENTIFIER){
            if(! binds.containsKey(ast.self().str())){
                throw new IllegalArgumentException(ast.self().str() +
                        " is not defined");
            }
            return binds.get(ast.self().str());
        }
        if(tag == LAMBDA){
            return new YAPL_Function(
                    arg -> {
                        var a = new YAPL_Eval(
                                this,
                                ast.child(0).self().str(),
                                arg);
                        return a.eval(ast.child(1));
                    },
                    new YAPL_FunctionType(PREFIX, 1, LEFT)
            );
        }
        if(tag == ARRAY){
            if(ast.child(0).self().tag() == ARRAY_LITR){
                return new YAPL_Array(
                    a -> eval(ast.child(0).children().get(((YAPL_Int) a).val())),
                    ast.child(0).children().size(),
                    new YAPL_FunctionType(PREFIX, 1, LEFT)
                );
            }
            return new YAPL_Array(
                ((YAPL_Function) eval(ast.child(0))).val,
                ((YAPL_Int) eval(ast.child(1).child(0))).val(),
                new YAPL_FunctionType(PREFIX, 1, LEFT)
            );
        }
        if(tag == ASSIGN){
            var e = eval(ast.child(1));
            binds.put(ast.child(0).self().str(), e);
            return e;
        }
        if(tag == IF){
            var cond = eval(ast.child(0).child(0));
            if(cond == YAPL_SpecialValue.NOTHING){
                return eval(ast.child(2).child(0));
            }
            if(((YAPL_Int) cond).val().equals(0)){
                return eval(ast.child(2).child(0));
            }
            return eval(ast.child(1).child(0));
        }

        if(ast.self().tag() != EXPR){
            throw new IllegalArgumentException();
        }
        List<YAPL_Object> list = new ArrayList<>();
        ast.children().forEach(node -> list.add(eval(node)));

        if(list.size() == 1){
            return list.get(0);
        }

        int maxPriority
                = list.stream().filter(obj -> obj instanceof YAPL_Function)
                .map(fun -> ((YAPL_FunctionType) fun.type()).priority.priority)
                .max(Integer::compare).orElse(0);

        int minPriority
                = list.stream().filter(obj -> obj instanceof YAPL_Function)
                .map(fun -> ((YAPL_FunctionType) fun.type()).priority.priority)
                .min(Integer::compare).orElse(0);

        for(int j=minPriority; j <= maxPriority; j++)
        for(int i=0; i < list.size(); i++){

            var elem = list.get(i);

            if (elem.type() instanceof YAPL_FunctionType
            && ((YAPL_FunctionType) elem.type()).priority.priority == j){
                var notion
                        = ((YAPL_FunctionType) elem.type()).notion;

                if(notion == PREFIX){
                    if(i + 1 >= list.size()){
                        continue;
                    }
                    var ret =
                            ((YAPL_Function)elem).apply(list.get(i + 1));
                    list.set(i, ret);
                    list.remove(i + 1);
                    i--;
                }
                else if(notion == SUFFIX){
                    if(i - 1 < 0){
                        continue;
                    }
                    var ret =
                            ((YAPL_Function)elem).apply(list.get(i - 1));
                    list.set(i, ret);
                    list.remove(i - 1);
                    i--;
                }
                else throw new IllegalArgumentException();
            }

        }
        if (list.size() != 1){
            throw new IllegalArgumentException("Syntax error");
        }
        return list.get(0);
    }

    public static void main(String[] args) {
        System.out.println("Init Ok");
        t2();
    }

    static void t2(){
        String[] k = {"(", ")", "[", "]", "\\", "->", ":", "=", "?"};
        String[] s = {" "};
        Tokenizer tokenizer = new Tokenizer(k, s);

        TokenList tl = tokenizer.exec(
                "0 ? 1 : a"
        );
        System.out.println(tl);
        var ast = YAPL_Parse.expr().parse(tl);
        System.out.println(ast);

        var add
            = YAPL_Function.intInfixBiFunction(
                (a, b) -> new YAPL_Int(a.val() + b.val()),
                3
            );
        var mul
            = YAPL_Function.intInfixBiFunction(
                (a, b) -> new YAPL_Int(a.val() * b.val()),
                2
            );
        var sub
                = YAPL_Function.intInfixBiFunction(
                (a, b) -> new YAPL_Int(b.val() - a.val()),
                3
        );


        var eval = new YAPL_Eval();
        eval.binds.put("+", add);
        eval.binds.put("*", mul);
        eval.binds.put("-", sub);
        eval.binds.put(".", dot);

        var ans = eval.eval(ast);
        System.out.println(ans);
    }

    static YAPL_Function dot
        = new YAPL_Function(
            a1 -> new YAPL_Function(
                a2 -> {
                    if(a1 instanceof YAPL_Array){
                        return new YAPL_Array(
                            a3 -> {
                                var tmp
                                        = ((YAPL_Array) a1).apply(a3);
                                return ((YAPL_Function) a2).apply(tmp);
                            },
                            ((YAPL_Array) a1).length,
                            new YAPL_FunctionType(PREFIX, 1, LEFT)
                        );
                    }
                    return new YAPL_Function(
                        a3 -> {
                            var tmp
                                    = ((YAPL_Function) a1).apply(a3);
                            return ((YAPL_Function) a2).apply(tmp);
                        },
                        new YAPL_FunctionType(PREFIX, 1, LEFT)
                    );
                },
                new YAPL_FunctionType(SUFFIX, 0, LEFT)
            ),
            new YAPL_FunctionType(PREFIX, 0, LEFT)
        );


}
