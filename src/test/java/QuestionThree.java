import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhiqiang.li
 * @date 2022/7/1
 */

@Slf4j
public class QuestionThree {

    @Test
    public void test(){
        List<String> list = readFile("sdxl_prop.txt");
        List<String> list2 = readFile("sdxl_template.txt");
        String fileName = "sdxl.txt";
        Map<String, List<String>> orderMap = new HashMap<>();


        orderMap.put("$indexOrder", indexOrder(list));
        orderMap.put("$natureOrder", natureOrder(list));
        orderMap.put("$charOrder", charOrder(list));
        orderMap.put("$charOrderDESC", charOrderDESC(list));

        writeFile(fileName, replaceText(orderMap, list2));
    }

    private List<String> natureOrder(List<String> list){
        return list.stream().filter(t -> !t.isEmpty()).map(t -> t.split("\t")[1]).collect(Collectors.toList());
    }

    private List<String> indexOrder(List<String> list){
        return list.stream().filter(t -> !t.isEmpty()).sorted(Comparator.comparing(t -> Integer.valueOf(t.split("\t")[0]))).map(t -> t.split("\t")[1]).collect(Collectors.toList());
    }

    private List<String> charOrder(List<String> list){
        // Comparator.naturalOrder()
        List<String> sorted2 = list.stream().filter(t -> !t.isEmpty()).map(t -> t.split("\t")[1]).sorted(String::compareTo).collect(Collectors.toList());
        return sorted2;
    }

    private List<String> charOrderDESC(List<String> list){
        return list.stream().filter(t -> !t.isEmpty()).map(t -> t.split("\t")[1]).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    private void writeFile(String fileName, List<String> list){
        list.forEach(t -> {
            try {
                FileUtils.write(new File(fileName), t+"\n", "UTF-8",true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private List<String> replaceText(Map<String, List<String>> orderMap, List<String> list){
        return list.stream().map(s -> {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < s.length()) {
                if (s.charAt(i) == '$') {
                    int start = i;
                    int index = 0;
                    while (i < s.length() && s.charAt(i) != '(') {
                        i++;
                    }

                    int end = i;
                    i++;
                    while (i < s.length() && s.charAt(i) != ')') {
                        index = index * 10 + Character.getNumericValue(s.charAt(i));
                        i++;
                    }
                    String type = s.substring(start, end);
                    String temp = orderMap.get(type).get(index);

                    sb.append(temp);
                } else {
                    sb.append(s.charAt(i));
                }
                i++;
            }
            return sb.toString();
        }).collect(Collectors.toList());
    }

    private List<String> readFile(String fileName){
        try {
            return FileUtils.readLines(new File(fileName), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
