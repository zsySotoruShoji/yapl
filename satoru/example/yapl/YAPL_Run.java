package satoru.example.yapl;

import satoru.ordovices.parse.ASTNode;
import satoru.ordovices.parse.TokenList;
import satoru.ordovices.tokenizer.Tokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.Scanner;

import static satoru.example.yapl.YAPL_Associative.LEFT;
import static satoru.example.yapl.YAPL_NotionOfFunction.PREFIX;
import static satoru.example.yapl.YAPL_NotionOfFunction.SUFFIX;
import static satoru.example.yapl.YAPL_SpecialValue.NOTHING;

public class YAPL_Run {

    public static void main(String[] args) {

        final String[] k = {"(", ")", "[", "]", "\\", "->", ":", "?"};
        final String[] s = {" "};
        Tokenizer tokenizer = new Tokenizer(k, s);

        Scanner sc = new Scanner(System.in);

        var eval = new YAPL_Eval();
        eval.binds.put("+", add);
        eval.binds.put("*", mul);
        eval.binds.put("-", sub);
        eval.binds.put("/", div);
        eval.binds.put(".", dot);
        eval.binds.put("or", or);
        eval.binds.put("len", len);
        eval.binds.put("prefix", prefix);
        eval.binds.put("postfix", postfix);

        if(args.length != 0){
            long start = System.currentTimeMillis();

            File file = new File(args[0]);
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line = br.readLine()) != null){
                    var tl = tokenizer.exec(line);
                    var ast = YAPL_Parse.orAssign.parse(tl);
                    eval.eval(ast);
                }
                System.out.print(file);
                System.out.println(" loaded");
            } catch (IOException e) {
                System.err.println("file not found");
            }
            long end = System.currentTimeMillis();
            System.out.println(end - start + " ms");
        }

        while (true){
            sleep(50);
            System.out.print('>');
            TokenList tl;
            ASTNode ast;
            try{
                tl = tokenizer.exec(sc.nextLine());
                ast = YAPL_Parse.orAssign.parse(tl);
            }catch (NumberFormatException e){
                System.err.println("Syntax error");
                System.err.println(
                        "whitespace is needed between operators");
                continue;
            }

            try{
                var ans = eval.eval(ast);
                System.out.println(ans);
            }catch (ClassCastException e){
                System.err.println("Type error");
            }catch (IllegalArgumentException e){
                System.err.println(e.getMessage());
            }
        }

    }

    static final YAPL_Function add
            = YAPL_Function.intInfixBiFunction(
            (a, b) -> new YAPL_Int(a.val() + b.val()),
            3
    );
    static final YAPL_Function mul
            = YAPL_Function.intInfixBiFunction(
            (a, b) -> new YAPL_Int(a.val() * b.val()),
            2
    );
    static final YAPL_Function sub
            = YAPL_Function.intInfixBiFunction(
            (b, a) -> new YAPL_Int(a.val() - b.val()),
            3
    );
    static final YAPL_Function div
            = YAPL_Function.intInfixBiFunction(
            (b, a) -> new YAPL_Int(a.val() / b.val()),
            2
    );

    static final YAPL_Function dot = new YAPL_Function(
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

    static final YAPL_Function or = new YAPL_Function(
        a1 -> new YAPL_Function(
            a2 -> {
                if(a2 == NOTHING){
                    return a1;
                }
                return a2;
            },
            new YAPL_FunctionType(SUFFIX, 4, LEFT)
        ),
        new YAPL_FunctionType(PREFIX, 4, LEFT)
    );

    static final YAPL_Function len = new YAPL_Function(
            a1 -> new YAPL_Int(
                    ((YAPL_Array) a1).length
            ),
            new YAPL_FunctionType(PREFIX, 1, LEFT)
    );

    static final YAPL_Function prefix = new YAPL_Function(
        right -> new YAPL_Function(
            left -> new YAPL_Function(
                ((YAPL_Function) left)::apply,
                new YAPL_FunctionType(PREFIX, ((YAPL_Int) right).val(), LEFT)
            ),
            new YAPL_FunctionType(SUFFIX, -1, LEFT)
        ),
        new YAPL_FunctionType(PREFIX, -1, LEFT)
    );

    static final YAPL_Function postfix = new YAPL_Function(
        right -> new YAPL_Function(
            left -> new YAPL_Function(
                ((YAPL_Function) left)::apply,
                new YAPL_FunctionType(SUFFIX, ((YAPL_Int) right).val(), LEFT)
            ),
            new YAPL_FunctionType(SUFFIX, -1, LEFT)
        ),
        new YAPL_FunctionType(PREFIX, -1, LEFT)
    );

    static void sleep(int millis){
        try {
            Thread.sleep(50);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

}
