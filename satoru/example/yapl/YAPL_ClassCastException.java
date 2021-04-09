package satoru.example.yapl;

public class YAPL_ClassCastException extends ClassCastException{

    protected final String from;
    protected final String to;

    public YAPL_ClassCastException(String from, String to){
        this.from = from;
        this.to = to;
    }

    @Override
    public String getMessage() {
        return from +
                " cannot converted to " +
                to;
    }

}
