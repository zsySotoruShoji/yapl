package satoru.example.yapl;

public enum YAPL_NonFunctionTypes implements IYAPL_Type{

    INT,
    SPECIAL
    ;


    @Override
    public boolean isFunction() {
        return false;
    }
}
