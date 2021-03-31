package satoru.ordovices.parse;

import java.util.ArrayList;
import java.util.List;

public class TokenList extends ArrayList<Token> implements List<Token> {

    private int pos = 0;
    private String errMsg = null;

    public Token next(){
        if(pos == size()){
            setFailed("EOF reached");
            return null;
        }
        return get(pos++);
    }

    public int pos(){
        return pos;
    }

    public void setPos(int pos){
        if(0 <= pos && pos <= size()) {
            this.pos = pos;
        }else{
            throw new IllegalArgumentException();
        }
    }

    public void setFailed(String errMsg){
        this.errMsg = errMsg;
    }

    public boolean isFailed(){
        return errMsg != null;
    }

    public boolean hasNext(){
        return pos() != size();
    }

    @Override
    public String toString(){
        if(isFailed()) return "failed: " + errMsg;

        var ans = new StringBuilder();
        for(int i=0; i < size(); i++){
            if(i == pos){
                ans.append('|');
            }
            ans.append(get(i));
        }
        if(pos == size()) ans.append('|');
        return ans.toString();
    }
}
