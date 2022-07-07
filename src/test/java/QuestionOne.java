import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class QuestionOne {
    @Test
    public void test(){
        String path = "G:\\java学习\\mystudy\\access.log";
        List<String> list = readFile(path);
        // 1. 请求总数
//        int requestCount = getRequestCount(list);

        // 2. 获取最频繁访问的前十个网站
//        getHttpRequest(list);

        // 3. 获取get， post的count
//        getRequestWayNum("get", list);
//        getRequestWayNum("post", list);

        // 4. URI 格式均为 /AAA/BBB 或者 /AAA/BBB/CCC 格式，按 AAA 分类，输出各个类别下 URI 都有哪些.
        getURI(list);

    }

    private Integer getRequestCount(List<String> list){
        return (int) list.stream().filter(StringUtils::isNotBlank).count();
    }

    private Map<String, Integer> getHttpRequest(List<String> list){
        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> res = new HashMap<>();
        Set<String> set = new HashSet<>();

        for (String str : list){
            String s = str.split(" ")[1];
            map.put(s, map.getOrDefault(s, 0) + 1);
            set.add(s);
        }
        List<String> list1 = new ArrayList<>(set);
        list1.sort((o1, o2) -> map.getOrDefault(o2, 0) - map.getOrDefault(o1, 0));
        for (int i = 0; i < 10; i++){
            res.put(list1.get(i), map.getOrDefault(list1.get(i), 0));
        }
        return res;
    }

    private int getRequestWayNum(String way, List<String> list){
        return (int) list.stream().filter(t -> StringUtils.equalsIgnoreCase(way, t.split(" ")[0])).count();
    }

    private void getURI(List<String> list){
        Map<String, List<String>> mapURI = new HashMap<>();
        list.forEach(t -> {
            String header = "/" + t.split(" ")[1].split("/")[1];
            List<String> orDefault = mapURI.getOrDefault(header, new ArrayList<>());
            orDefault.add(t);
            mapURI.put(header, orDefault);
        });
        System.out.println(JSONObject.toJSONString(mapURI));
    }

    private Set<String> getURIHeader(List<String> list){
        return list.stream().map(t -> t.split(" ")[1].split("/")[0]).collect(Collectors.toSet());
    }

    private List<String> readFile(String filePath){
        try {
            return  FileUtils.readLines(new File(filePath), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
