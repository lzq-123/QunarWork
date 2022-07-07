package QuestionFour;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author zhiqiang.li
 * @date 2022/7/6
 */
public class CatParse implements ShellParserInterface {
    @Override
    public boolean support(String[] command) {
        if ("cat".equals(command[0]) && (command.length == 2 || command.length == 1)) {
            return true;

        }
        return false;
    }

    @Override
    public List<String> parse(List<String> result, String[] command) {
        if (command.length == 1) {
            return result;
        }
        if (command.length == 2) {
            try {
                return FileUtils.readLines(new File(command[1]), Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
