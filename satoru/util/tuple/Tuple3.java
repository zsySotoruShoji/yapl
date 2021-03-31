package satoru.util.tuple;

import java.io.Serializable;

public class Tuple3<A1,A2,A3> implements Tuple, Serializable {

    private final A1 a1;
    private final A2 a2;
    private final A3 a3;

    public Tuple3(A1 a1, A2 a2, A3 a3){
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
    }

    public A1 a1() {
        return a1;
    }
    public A2 a2() {
        return a2;
    }
    public A3 a3() {
        return a3;
    }

    @Override
    public String toString(){
        return "("
                + a1 + ", "
                + a2 + ", "
                + a3
                + ")";
    }

    @Override
    public Object[] asArray() {
        return new Object[]{a1, a2, a3};
    }
}
