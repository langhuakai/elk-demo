package com.wei.elk.es;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/12/5 04:09:55
 */
/**
 * 1、正则双引号包围的单元格
 * 2、双引号包围的单元格结束符号一定是",和"$
 * @date 2020年3月28日 下午2:35:51
 */
public class JavaTest {

    //双引号包围的单元格中的内容正则表达式：
    //1、双引号一定成对出现("{2})*
    //2、成对双引号前可能会出现非双引号([^"]"{2})* 比如"""123"""""可以看做"【""】【123""】【""】"
    //3、但是，比如"a""""b""""""c"可以看做"【a""】【""】【b""】【""""】c"，最后的c无法匹配，所以最后还要加上可能多于出来的非双引号[^"]*
    //最终单元格中的内容正则表达式为： ([^"]*"{2})*[^"]*
    private static final Pattern p = Pattern.compile("^\"([^\"]*\"{2})*[^\"]*(\",|\"$)");//双引号包围的单元格结束符号一定是",和"$

    public static void main(String[] args) {
        BufferedReader br = null;
        try {
            String path = "c:\\temp\\20221205150327445.txt";
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf8"));
            List<List<String>> list = new ArrayList<>();
            List<String> row = new ArrayList<>();
            String line;
            String str = "";
            while ((line = br.readLine()) != null) {
                line = str + line;
                String nline = line;
                if (!"".equals(line.trim())) {
                    /*if (line.endsWith(",")) {
                        nline = line + ",";
                    }
                    str = getCells(nline, row);*/
                    str = getCells(line, row);
                }
                if ("".equals(str)) {//一条完整的行结束
                    list.add(row);
                    row = new ArrayList<>();
                } else {
                    str += "\r\n";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getCells(String line, List<String> row) {
        String cell;
        if (line.startsWith("\"")) {//有双引号包围的单元格
            Matcher m = p.matcher(line);
            if (m.find()) {
                cell = m.group();
                if (cell.endsWith(",")) {
                    cell = cell.substring(0, cell.length() - 1);
                }
                if (cell.startsWith("\"") && cell.endsWith("\"")) {
                    cell = cell.substring(1, cell.length() - 1);
                    cell = cell.replaceAll("\"\"", "\"");
                }
                row.add(cell);
                line = line.substring(m.end());
                if ("".equals(line)) {//一条完整的行结束
                    return "";
                }
                return getCells(line, row);
            } else {
                return line;
            }
        } else {//无双引号包围的单元格
            if (line.contains(",")) {
                cell = line.substring(0, line.indexOf(","));
                row.add(cell.trim());
                line = line.substring(line.indexOf(",") + 1);
                return getCells(line, row);
            } else {//一条完整的行结束
                row.add(line.trim());
                return "";
            }
        }
    }
}
