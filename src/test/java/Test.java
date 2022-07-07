import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author zhiqiang.li
 * @date 2022/7/1
 */
public class Test {
    public static void main(String[] args) {
        String a = "你是一个大帅逼(555),帅逼哈哈哈帅逼哈";

        String b = "帅逼";
        String t = "  grep  txt  ";
        String[] s = Arrays.stream(t.split(" ")).filter(StringUtils::isNotBlank).toArray(String[]::new);
        System.out.println(s);
    }

    public static String getFindStr(String input){
        // 统计总字符数(包括标点符号)、汉字数、
        //英文字符数、标点符号数
        int len = input.length();
        int chiNum = 0, enlNum = 0, pointNum, numCount = 0;
        String replace = input.replaceAll("\\p{P}", "");
        pointNum = len - replace.length();
        for (char c : replace.toCharArray()){
            if (String.valueOf(c).matches("[\u4e00-\u9fa5]")){
                chiNum++;
                continue;
            }
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')){
                enlNum++;
                continue;
            }

            if (c >= '0' && c < '9'){
                numCount++;
            }
        }

        return "总字符 : " + len + " 汉字数 : " + chiNum + " 英文字符数 : " + enlNum + " 标点符号数 : " + pointNum + "数字数 ：" + numCount;
    }
}
