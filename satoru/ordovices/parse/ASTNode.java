package satoru.ordovices.parse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ASTNode {


    private final Token self;
    private final List<ASTNode> children = new ArrayList<>();

    public ASTNode(Token self, ASTNode... children){
        this.self = self;
        this.children.addAll(Arrays.asList(children));
    }

    public Token self(){
        return self;
    }

    public List<ASTNode> children(){
        return children;
    }

    public ASTNode child(int index){
        return children.get(index);
    }

    public void addChildren(ASTNode... nodes){
        this.children.addAll(Arrays.asList(nodes));
    }

    public void addChildren(int index, ASTNode... nodes){
        this.children.addAll(index, Arrays.asList(nodes));
    }

    @Override
    public String toString(){
        String ans = self.toString();
        return children.isEmpty() ? ans : ans + children;
    }
}
