package com.fit.gui.testedfunctions;

/**
 * Represent expected output that user fills in the EO view
 *
 * @author ducanhnguyen
 */
public class ExpectedOutputItem {
    private String nameVar;
    private String typeVar;
    private String valueVar;

    public ExpectedOutputItem(String nameVar, String typeVar, String valueVar) {
        this.nameVar = nameVar;
        this.typeVar = typeVar;
        this.valueVar = valueVar;
    }

    public String getNameVar() {
        return nameVar;
    }

    public void setNameVar(String nameVar) {
        this.nameVar = nameVar;
    }

    public String getTypeVar() {
        return typeVar;
    }

    public void setTypeVar(String typeVar) {
        this.typeVar = typeVar;
    }

    public String getValueVar() {
        return valueVar;
    }

    public void setValueVar(String valueVar) {
        this.valueVar = valueVar;
    }

    @Override
    public String toString() {
        return nameVar + "=" + valueVar + ";";
    }
}
