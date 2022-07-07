package questionFour;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhiqiang.li
 * @date 2022/7/6
 */
public class WcParse implements ShellParserInterface {
    @Override
    public boolean support(String[] command) {
        if ("wc".equals(command[0]) && (command.length == 2 || command.length == 3)) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> parse(List<String> result, String[] command) {
        List<String> res = new ArrayList<>();
        if (command.length == 2) {
            res.add(String.valueOf(result.size()));
        }
        if (command.length == 3) {
            try {
                result = FileUtils.readLines(new File(command[2]), Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
            res.add(String.valueOf(result.size()));
        }
        return res;
    }
}
