package satoru.example.yapl;

import static satoru.example.yapl.YAPL_NonFunctionTypes.SPECIAL;

public enum YAPL_SpecialValue implements YAPL_Object{
    NOTHING
    ;

    @Override
    public Object val() {
        return this;
    }

    @Override
    public IYAPL_Type type() {
        return SPECIAL;
    }
}
