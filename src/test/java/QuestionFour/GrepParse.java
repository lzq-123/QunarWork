package QuestionFour;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhiqiang.li
 * @date 2022/7/6
 */
public class GrepParse implements ShellParserInterface {
    @Override
    public boolean support(String[] command) {
        if ("grep".equals(command[0]) && (command.length == 2 || command.length == 3)){
            return true;
        }
        return false;
    }

    @Override
    public List<String> parse(List<String> result, String[] command) {
        if (command.length == 2){
            return result.stream().filter(t -> t.contains(command[1])).collect(Collectors.toList());
        }

        if (command.length == 3){
            try {
                return FileUtils.readLines(new File(command[2]), Charset.defaultCharset()).stream().filter(t -> t.contains(command[1])).collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
