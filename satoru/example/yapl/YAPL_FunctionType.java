package satoru.example.yapl;

import static satoru.example.yapl.YAPL_Associative.LEFT;
import static satoru.example.yapl.YAPL_NonFunctionTypes.INT;
import static satoru.example.yapl.YAPL_NotionOfFunction.*;

public class YAPL_FunctionType implements IYAPL_Type{

    final YAPL_NotionOfFunction notion;
    final YAPL_Priority priority;
    final YAPL_Associative assoc;

    public YAPL_FunctionType(YAPL_NotionOfFunction notion,
                             int priority,
                             YAPL_Associative assoc){
        this.notion = notion;
        this.priority = new YAPL_Priority(priority);
        this.assoc = assoc;
    }

    @Override
    public boolean isFunction() {
        return true;
    }

    @Override
    public String toString(){
     return "Function";
    }


}
