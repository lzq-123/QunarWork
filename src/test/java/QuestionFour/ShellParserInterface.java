package QuestionFour;

import java.util.List;

/**
 * @author zhiqiang.li
 * @date 2022/7/6
 */
public interface ShellParserInterface {
    boolean support(String[] command);

    List<String> parse(List<String> result, String[] command);
}
