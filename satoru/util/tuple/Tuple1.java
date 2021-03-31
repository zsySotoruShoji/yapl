package satoru.util.tuple;

import java.io.Serializable;

public class Tuple1<A1> implements Tuple, Serializable {

    private final A1 a1;

    public Tuple1(A1 a1){
        this.a1 = a1;
    }

    public A1 a1() {
        return a1;
    }

    @Override
    public String toString(){
        return "(" + a1 + ")";
    }

    @Override
    public Object[] asArray() {
        return new Object[]{a1};
    }
}
