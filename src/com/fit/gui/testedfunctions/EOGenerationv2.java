package com.fit.gui.testedfunctions;

import com.fit.testdatagen.testdatainit.VariableTypes;

public class EOGenerationv2 {

    public EOGenerationv2() {
    }

    public static void main(String[] args) throws Exception {
        new EOGenerationv2();

    }

    private static String normalizeNameVar(String nameVar) {
        return nameVar.replace(".", "_").replace("[", "_").replace("]", "_");
    }

    public String normalize(String name, String type, String attributes, String value) throws Exception {
        name = EOGenerationv2.normalizeNameVar(name);
        String output = "-";

        if (VariableTypes.isBasic(type))
            output = String.format("%s %s = %s;", type, name, value);
        else if (VariableTypes.isChOneLevel(type)) {
            String coreType = VariableTypes.deleteStorageClasses(type).replace("*", "");

            if (value.equals(ExpectedOutputPanel.NULL))
                output = String.format("%s %s = NULL;", type, name, coreType);
            else
                output = String.format("%s %s = %s;", type, name, value);

        } else if (VariableTypes.isNumOneLevel(type)) {
            String coreType = VariableTypes.deleteStorageClasses(type).replace("*", "");

            if (value.equals(ExpectedOutputPanel.NULL))
                output = String.format("%s %s = NULL;", type, name, coreType);
            else {
                int size = value.split(",").length;
                output = String.format("%s %s = new %s[%s];", type, name, coreType, size);
                /**
                 *
                 */
                int id = 0;
                for (String item : value.split(",")) {
                    id++;
                    item = item.replace("{", "").replace("}", "");
                    output += String.format("%s[%s] = %s;", name, id, item);
                }
            }

        } else if (VariableTypes.isStructureOneLevel(type)) {
            String coreType = VariableTypes.deleteStorageClasses(type).replace("*", "");
            if (value.equals(ExpectedOutputPanel.NULL))
                output = String.format("%s %s = NULL;", type, name, coreType);
            else {
                int size = value.split(",").length;
                output = String.format("%s %s = new %s[%s];", type, name, coreType, size);
            }

        } else if (VariableTypes.isNumOneDimension(type)) {
            String coreType = VariableTypes.deleteStorageClasses(type).replace("*", "");
            coreType = coreType.replaceAll("\\[[0-9]*\\]", "");
            output = String.format("%s %s[] = %s;", coreType, name, value);
        }

        return output;
    }
}
