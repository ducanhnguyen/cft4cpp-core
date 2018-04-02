package test.nartoan.googletest;

import com.fit.gui.testreport.object.INameRule;
import com.fit.testdatagen.testdatainit.VariableTypes;
import com.fit.tree.object.INode;
import com.fit.tree.object.IVariableNode;
import com.fit.tree.object.StructNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Create virtual data
 *
 * @author DucAnh
 */
public class ToanClass {

    private final int MIN_BOUND = 32;
    private final int MAX_BOUND = 126;
    private final int SIZE_OF_ARRAY = 4;

    private final int TYPE_BOOL = 0;
    private final int TYPE_CHAR = 1;
    private final int TYPE_INTEGER = 2;
    private final int TYPE_REAL = 3;
    private List<IVariableNode> expectedNodeTypes;

    public ToanClass(List<IVariableNode> expectedNodeTypes) {
        this.expectedNodeTypes = expectedNodeTypes;
    }

    private void createArray(VirtualDataInRows virtualDataInRows, INode node, int typeOfArray) {
        List<String> elements = new ArrayList<>();
        for (int i = 0; i < SIZE_OF_ARRAY; i++)
            switch (typeOfArray) {
                case TYPE_BOOL:
                    elements.add(generateBool());
                    break;
                case TYPE_CHAR:
                    elements.add(generateChar());
                    break;
                case TYPE_INTEGER:
                    elements.add(generateBool());
                    break;
                case TYPE_REAL:
                    elements.add(generateReal());
                    break;
            }

        String temp = "";
        if (typeOfArray != TYPE_CHAR) {
            temp += "{";
            for (String ele : elements)
                temp += ele + ",";
            temp += "}";
        } else {
            temp += "\"";
            for (String ele : elements)
                temp += ele;
            temp += "\"";
        }

        VirtualDataInRow row = new VirtualDataInRow(INameRule.EXPECTED_OUTPUT_PREFIX + node.getNewType(),
                ((IVariableNode) node).getRawType().replaceAll("\\[\\]", "[" + SIZE_OF_ARRAY + "]"),
                "size=" + SIZE_OF_ARRAY, temp);
        virtualDataInRows.add(row);

        for (int i = 0; i < SIZE_OF_ARRAY; i++)
            if (typeOfArray != TYPE_CHAR) {
                row = new VirtualDataInRow(INameRule.EXPECTED_OUTPUT_PREFIX + node.getNewType() + "[" + i + "]",
                        ((IVariableNode) node).getRawType().replaceAll("\\[.*\\]", ""), "", elements.get(i));
                virtualDataInRows.add(row);
            } else {
                row = new VirtualDataInRow(INameRule.EXPECTED_OUTPUT_PREFIX + node.getNewType() + "[" + i + "]",
                        ((IVariableNode) node).getRawType().replaceAll("\\[.*\\]", ""), "",
                        "'" + elements.get(i) + "'");
                virtualDataInRows.add(row);
            }
        return;
    }

    private void dataNormalize(INode node, VirtualDataInRows virtualDataInRows) {
        String type = ((IVariableNode) node).getRawType();

        if (VariableTypes.isChBasic(type)) {
            VirtualDataInRow row = new VirtualDataInRow(INameRule.EXPECTED_OUTPUT_PREFIX + node.getNewType(),
                    ((IVariableNode) node).getRawType(), "", generateChar());
            virtualDataInRows.add(row);

        } else if (VariableTypes.isChOneDimension(type) || VariableTypes.isChOneLevel(type))
            createArray(virtualDataInRows, node, TYPE_CHAR);
        else if (VariableTypes.isNumBasicFloat(type)) {
            VirtualDataInRow row = new VirtualDataInRow(INameRule.EXPECTED_OUTPUT_PREFIX + node.getNewType(),
                    ((IVariableNode) node).getRawType(), "", generateReal());
            virtualDataInRows.add(row);

        } else if (VariableTypes.isNumOneDimensionFloat(type) || VariableTypes.isNumOneLevelFloat(type))
            createArray(virtualDataInRows, node, TYPE_REAL);
        else if (VariableTypes.isVoid(type)) {
            VirtualDataInRow row = new VirtualDataInRow(INameRule.EXPECTED_OUTPUT_PREFIX + node.getNewType(),
                    ((IVariableNode) node).getRawType(), "", "");
            virtualDataInRows.add(row);

        } else if (VariableTypes.isNumBasic(type))
            switch (VariableTypes.getType(type)) {
                /**
                 * Bool type
                 */
                case VariableTypes.BASIC.NUMBER.INTEGER.BOOL:
                case VariableTypes.BASIC.NUMBER.INTEGER.BOOL + VariableTypes.REFERENCE: {
                    VirtualDataInRow row = new VirtualDataInRow(INameRule.EXPECTED_OUTPUT_PREFIX + node.getNewType(),
                            ((IVariableNode) node).getRawType(), "", generateBool());
                    virtualDataInRows.add(row);
                    break;
                }

                default:
                    /**
                     * Integer type
                     */
                    VirtualDataInRow row = new VirtualDataInRow(INameRule.EXPECTED_OUTPUT_PREFIX + node.getNewType(),
                            ((IVariableNode) node).getRawType(), "", generateInt());
                    virtualDataInRows.add(row);
            }
        else if (VariableTypes.isOneDimension(type))
            switch (VariableTypes.getType(type)) {

                case VariableTypes.BASIC.NUMBER.INTEGER.BOOL + VariableTypes.ONE_DIMENSION:
                    createArray(virtualDataInRows, node, TYPE_BOOL);
                    break;

                default:
                    createArray(virtualDataInRows, node, TYPE_INTEGER);
            }
        else if (VariableTypes.isOneLevel(type))
            switch (VariableTypes.getType(type)) {

                case VariableTypes.BASIC.NUMBER.INTEGER.BOOL + VariableTypes.ONE_LEVEL:
                    createArray(virtualDataInRows, node, TYPE_BOOL);

                default:
                    createArray(virtualDataInRows, node, TYPE_INTEGER);
                    return;
            }
        else {
            if (((IVariableNode) node).getRawType().replaceAll(VariableTypes.SPECICAL_TYPE.SIMPLE_LIST_REGEX, "")
                    .length() == 0)
                return;

            if (((IVariableNode) node).getRawType().replaceAll(VariableTypes.SPECICAL_TYPE.SIMPLE_VECTOR_REGEX, "")
                    .length() == 0)
                return;

            if (((IVariableNode) node).getRawType().replaceAll(VariableTypes.STRUCTURE.ONE_LEVEL_STRUCTURE_REGEX, "")
                    .length() == 0) {
                VirtualDataInRow row = new VirtualDataInRow(INameRule.EXPECTED_OUTPUT_PREFIX + node.getNewType(),
                        ((IVariableNode) node).getRawType(), "size=" + SIZE_OF_ARRAY, "");
                virtualDataInRows.add(row);
                for (int i = 0; i < SIZE_OF_ARRAY; i++) {
                    row = new VirtualDataInRow(INameRule.EXPECTED_OUTPUT_PREFIX + node.getNewType() + "[" + i + "]",
                            ((IVariableNode) node).getRawType(), "", "");
                    virtualDataInRows.add(row);
                    List<IVariableNode> tempNode = ((StructNode) node).getAttributes();
                    for (IVariableNode temp : tempNode)
                        dataNormalize(temp, virtualDataInRows);
                }
            }

            if (((IVariableNode) node).getRawType().replaceAll(VariableTypes.STRUCTURE.SIMPLE_STRUCTURE_REGEX, "")
                    .length() == 0) {
                VirtualDataInRow row = new VirtualDataInRow(INameRule.EXPECTED_OUTPUT_PREFIX + node.getNewType(),
                        ((IVariableNode) node).getRawType(), "", "");
                virtualDataInRows.add(row);
                List<IVariableNode> tempNode = ((StructNode) node).getAttributes();

                for (IVariableNode temp : tempNode)
                    dataNormalize(temp, virtualDataInRows);
            }

            /**
             * Phần này hiển thị đầu có thể sai :/
             */
            if (((IVariableNode) node).getRawType()
                    .replaceAll(VariableTypes.STRUCTURE.ONE_DIMENSION_STRUCTURE_REGEX, "").length() == 0) {
                VirtualDataInRow row = new VirtualDataInRow(INameRule.EXPECTED_OUTPUT_PREFIX + node.getNewType(),
                        ((IVariableNode) node).getRawType(), "size=" + SIZE_OF_ARRAY, "");
                virtualDataInRows.add(row);

                for (int i = 0; i < SIZE_OF_ARRAY; i++) {
                    row = new VirtualDataInRow(INameRule.EXPECTED_OUTPUT_PREFIX + node.getNewType() + "[" + i + "]",
                            ((IVariableNode) node).getRawType(), "", "");
                    virtualDataInRows.add(row);
                    List<IVariableNode> tempNode = ((StructNode) node).getAttributes();

                    for (IVariableNode temp : tempNode)
                        dataNormalize(temp, virtualDataInRows);
                }
            }
        }
    }

    private String generateBool() {
        Random rand = new Random();
        int randomNum = rand.nextInt(1);
        return randomNum + "";
    }

    private String generateChar() {
        Random rand = new Random();
        int randomNum = MIN_BOUND + rand.nextInt(MAX_BOUND - MIN_BOUND);
        return (char) randomNum + "";
    }

    private String generateInt() {
        Random rand = new Random();
        int randomNum = MIN_BOUND + rand.nextInt(MAX_BOUND - MIN_BOUND);
        return randomNum + "";
    }

    private String generateReal() {
        Random rand = new Random();
        int randomNum = MIN_BOUND + rand.nextInt(MAX_BOUND - MIN_BOUND);
        int decimal = rand.nextInt(100);

        return randomNum + "." + decimal;
    }

    /**
     * @return
     * @Toan: code here
     */
    public VirtualDataInRows generateVirtualData() {
        VirtualDataInRows virtualDataInRows = new VirtualDataInRows();
        /**
         * Create virtual data
         */
        for (INode node : expectedNodeTypes)
            dataNormalize(node, virtualDataInRows);

        return virtualDataInRows;
    }

}
