package QuestionFour;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class QuestionFour {
    private static final List<ShellParserInterface> shellList = new ArrayList<>();

    public static void main(String[] args) {
        shellList.add(new CatParse());
        shellList.add(new GrepParse());
        shellList.add(new WcParse());
        while (true){
            System.out.print("请输入： ");
            getShellFunc();
        }

    }

    public static void getShellFunc(){
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        String[] inputStrs = input.split("\\|");
        List<String> res = new ArrayList<>();
        for (int i = 0; i < inputStrs.length; i++){
            String[] command = Arrays.stream(inputStrs[i].split(" ")).filter(StringUtils::isNotBlank).toArray(String[]::new);
            for (ShellParserInterface shell : shellList){
                if (shell.support(command)){
                    res = shell.parse(res, command);
                    break;
                }
            }
        }

        for(String s : res){
            System.out.println(s);
        }
    }
}
