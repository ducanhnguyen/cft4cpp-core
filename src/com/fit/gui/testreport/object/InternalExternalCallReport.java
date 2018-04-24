package com.fit.gui.testreport.object;

import com.fit.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Đại diện cột <b>Internal/External calls</b> trong cột Expected Output hoặc
 * Actual Output
 *
 * @author DucAnh
 */
public class InternalExternalCallReport {

    protected static final String DATA_DELIMITER = "###";
    /**
     * Tên hàm
     */
    protected String name = "undefined";
    /**
     * Bộ giá trị truyền vào hàm
     */
    protected List<String> in = new ArrayList<>();
    /**
     * Bộ giá trị truyền vào hàm sau khi thực thi hàm
     */
    protected List<String> out = new ArrayList<>();
    /**
     * Giá trị trả về của hàm (có thể là expected output hoặc actual output)
     */
    protected String returnValue = "undefined";

    protected InternalExternalCallReport(String name, List<String> in, List<String> out, String returnValue) {
        this.name = name;
        this.in = in;
        this.out = out;
        this.returnValue = returnValue;
    }

    public InternalExternalCallReport(String name, String in, String out, String returnValue) {
        this.name = name;

        if (in.contains(InternalExternalCallReport.DATA_DELIMITER))
            this.in = Utils.split(in, InternalExternalCallReport.DATA_DELIMITER);
        else
            this.in.add(in);
        //
        if (out.contains(InternalExternalCallReport.DATA_DELIMITER))
            this.out = Utils.split(out, InternalExternalCallReport.DATA_DELIMITER);
        else
            this.out.add(out);

        this.returnValue = returnValue;
    }

    public InternalExternalCallReport(String name, String[] in, String[] out, String returnValue) {
        this.name = name;
        for (String item : in)
            this.in.add(item);
        for (String item : out)
            this.out.add(item);
        this.returnValue = returnValue;
    }

    public static String getDataDelimiter() {
        return InternalExternalCallReport.DATA_DELIMITER;
    }

    public List<String> getIn() {
        return in;
    }

    public String getName() {
        return name;
    }

    public List<String> getOut() {
        return out;
    }

    public String getReturnValue() {
        return returnValue;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n" + "In: " + in + "\n" + "Out: " + out + "\n" + "Return Value: " + returnValue
                + "\n";
    }
}
