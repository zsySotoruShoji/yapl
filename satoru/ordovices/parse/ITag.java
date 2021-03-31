package satoru.ordovices.parse;

public interface ITag {

    default Token asToken(){
        return new Token("", this);
    }

}
