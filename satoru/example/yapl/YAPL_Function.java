package satoru.example.yapl;

import java.util.function.BiFunction;
import java.util.function.Function;

import static satoru.example.yapl.YAPL_Associative.LEFT;
import static satoru.example.yapl.YAPL_NotionOfFunction.PREFIX;
import static satoru.example.yapl.YAPL_NotionOfFunction.SUFFIX;

public class YAPL_Function implements YAPL_Object{

    protected final Function<YAPL_Object, YAPL_Object> val;
    protected final YAPL_FunctionType type;

    public YAPL_Function(Function<YAPL_Object, YAPL_Object> val,
                         YAPL_FunctionType type) {
        this.val = val;
        this.type = type;
    }

    public static YAPL_Function intInfixBiFunction(
            BiFunction<YAPL_Int, YAPL_Int, YAPL_Int> fun,
            int priority){
        return new YAPL_Function(
            a1 -> new YAPL_Function(
                a2 -> new YAPL_Int(
                fun.apply(((YAPL_Int) a1), ((YAPL_Int) a2)).val()),
                new YAPL_FunctionType(
                    SUFFIX,
                    priority,
                    LEFT
                )
            ),
            new YAPL_FunctionType(
                PREFIX,
                priority,
                LEFT
            )
        );
    }

    YAPL_Object apply(YAPL_Object arg){
        return val.apply(arg);
    }

    @Override
    public Function<YAPL_Object, YAPL_Object> val(){
        return val;
    }

    @Override
    public IYAPL_Type type() {
        return type;
    }

    @Override
    public String toString(){

        return type.toString();
    }


}
