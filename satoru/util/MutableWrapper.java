package satoru.util;

public class MutableWrapper<T> {

    public T t;

    public MutableWrapper(T t){
        this.t = t;
    }

    @Override
    public String toString(){
        return t.toString();
    }
}
