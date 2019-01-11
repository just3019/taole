package org.demon.taole.utils;

import org.demon.util.JSONUtil;
import org.junit.Test;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 *
 * @author demon
 * @date 2019-01-11 16:47
 */
public class IkTest {

    @Test
    public void test() throws IOException {
        String keyword = "西门子冰箱";
        StringReader re = new StringReader(keyword);
        IKSegmenter ik = new IKSegmenter(re, true);
        Lexeme lex;
        List<String> list = new ArrayList<>();
        while ((lex = ik.next()) != null) {
            list.add(lex.getLexemeText());
        }
        System.out.println(JSONUtil.obj2Json(list));

    }
}
