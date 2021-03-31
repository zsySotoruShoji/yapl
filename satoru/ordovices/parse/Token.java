package satoru.ordovices.parse;

public class Token {

    private final String str;
    private final ITag tag;

    public Token(String str, ITag tag){
        this.str = str;
        this.tag = tag;
    }

    public String str(){
        return str;
    }

    public ITag tag(){
        return tag;
    }

    @Override
    public String toString(){
        String ans;
        if(tag == null){
            ans = str;
        }else{
            ans = str + ":" + tag;
        }
        return "<" + ans + ">";
    }

    public ASTNode asAST(){
        return new ASTNode(this);
    }
}
