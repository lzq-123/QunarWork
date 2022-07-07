import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Slf4j
public class QuestionTwo {

    @Test
    public void test(){
        List<String> list = readFile("StringUtils.java");
        // 统计有效代码行数
        calculate(list);
    }

    public void calculate(List<String> file){
        int count = (int) file.stream().map(String::trim).filter(trim -> StringUtils.isNotBlank(trim) && '*' != trim.charAt(0) && '/' != trim.charAt(0)).count();
        writeFile("validLineCount", String.valueOf(count));
    }

    private void writeFile(String fileName, String str){
        try {
            FileUtils.write(new File(fileName + ".txt"), str, Charset.defaultCharset());
            log.info("该文件 {} 已写入", fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
