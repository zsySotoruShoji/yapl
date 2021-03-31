package satoru.example.yapl;

public class YAPL_Priority {

    final int priority;

    public YAPL_Priority(int priority){
        this.priority = priority;
    }

    @Override
    public String toString(){
        var ans = new StringBuilder();
        ans.append("Priority=");
        ans.append(priority);
        return ans.toString();
    }
}
