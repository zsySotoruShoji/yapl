package satoru.example.yapl;

import java.util.function.Function;

import static satoru.example.yapl.YAPL_SpecialValue.NOTHING;

public class YAPL_Array extends YAPL_Function{

    protected final int length;

    public YAPL_Array(
            Function<YAPL_Object, YAPL_Object> val,
            int length,
            YAPL_FunctionType type) {
        super(val, type);
        this.length = length;
    }

    public int length(){
        return length;
    }

    @Override
    public YAPL_Object apply(YAPL_Object obj){
        int idx = ((YAPL_Int) obj).val();
        if(0 <= idx && idx < length){
            return super.apply(obj);
        }
        return NOTHING;
    }

    @Override
    public String toString(){
        if(length <= 0){
            return "[]";
        }
        var ans = new StringBuilder();
        ans.append('[');
        for(int i=0; i<length; i++){
            ans.append(apply(new YAPL_Int(i)));
            ans.append(' ');
        }
        ans.delete(ans.length()-1, ans.length());
        ans.append(']');
        return ans.toString();
    }
}
