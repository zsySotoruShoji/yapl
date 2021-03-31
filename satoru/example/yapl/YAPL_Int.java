package satoru.example.yapl;

import static satoru.example.yapl.YAPL_NonFunctionTypes.INT;

public class YAPL_Int implements YAPL_Object{

    private final int val;

    public YAPL_Int(int val) {
        this.val = val;
    }

    @Override
    public Integer val() {
        return val;
    }

    @Override
    public YAPL_NonFunctionTypes type() {
        return INT;
    }

    @Override
    public String toString(){
        return Integer.toString(val);
    }
}
