package satoru.ordovices.tokenizer;

import satoru.ordovices.parse.Token;
import satoru.ordovices.parse.TokenList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static satoru.ordovices.tokenizer.Tag.*;

public class Tokenizer {

    private final String[] keywords;
    private final String[] separator;

    public Tokenizer(String[] keywords, String[] separator){
        if(separator == null || keywords == null){
            throw new IllegalArgumentException();
        }
        Arrays.sort(keywords, (o1, o2) -> {
            int l1 = o1.length();
            int l2 = o2.length();

            return Integer.compare(l1, l2) * (-1);
        });
        Arrays.sort(separator, (o1, o2) -> {
            int l1 = o1.length();
            int l2 = o2.length();

            return Integer.compare(l1, l2) * (-1);
        });
        this.keywords = keywords;
        this.separator = separator;

    }

    private List<String> sep(String src){

        List<String> strList = new ArrayList<>();
        for(String s: separator){
            var a = src.split("\\Q" + s + "\\E");
            strList.addAll(Arrays.asList(a));
        }
        strList = strList.stream().filter(str -> !str.equals(""))
                .collect(Collectors.toList());

        List<String> ans = new ArrayList<>();

        for (String s : strList) {

            int pos = 0;
            for(int i=0; i < s.length(); ){
                for (String keyword : keywords) {
                    if (s.startsWith(keyword, i)) {
                        if (i != pos) {
                            ans.add(s.substring(pos, i));
                        }
                        ans.add(keyword);
                        i += keyword.length() - 1;
                        pos = i + 1;
                        break;
                    }
                }
                i++;
            }
            ans.add(s.substring(pos));
        }
        ans = ans.stream().filter(str -> !str.isEmpty())
                .collect(Collectors.toList());
        return ans;

    }

    private Token classify(String token){

        if(Arrays.asList(keywords).contains(token)){
            return new Token(token, KEYWORD);
        }else if(isDigit(token.charAt(0))){
            try{
                Integer.parseInt(token);
                return new Token(token, INT_LITERAL);
            }catch (NumberFormatException e){
                Double.parseDouble(token);
                return new Token(token, DOUBLE_LITERAL);
            }
        }
        return new Token(token, IDENTIFIER);
    }

    private static boolean isDigit(char c){
        return '0' <= c && c <= '9';
    }

    public TokenList exec(String src){
        var a = sep(src);
        var ans = new TokenList();
        a.forEach(str -> ans.add(classify(str)));
        return ans;
    }

    @Override
    public String toString(){
        return Arrays.toString(keywords) + ", "
                + Arrays.toString(separator);
    }


}
