package test.nartoan.googletest;

import java.util.ArrayList;
import java.util.Vector;

public class VirtualDataInRows extends ArrayList<VirtualDataInRow> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Biến đổi giá trị bảng sang đầu vào theo chuẩn Dương định nghĩa
     *
     * @param vituralDataInRows
     */
    public Vector<Vector<Object>> convertVituralDataToVector() {
        Vector<Vector<Object>> DuongInputFormat = new Vector<>();

        for (VirtualDataInRow vituralDataInRow : this) {
            Vector<Object> vector = new Vector<>();

            String nameExpectedOutput = vituralDataInRow.getNameExpectedOutput();
            String type = vituralDataInRow.getType();
            String attributes = vituralDataInRow.getAttributes();
            String expectedValue = vituralDataInRow.getExpectedValue();

            vector.add(nameExpectedOutput);
            vector.add(type);
            vector.add(attributes);
            vector.add(expectedValue);
            DuongInputFormat.add(vector);
        }
        return DuongInputFormat;
    }
}
