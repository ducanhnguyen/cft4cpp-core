package test.nartoan.googletest;

public class VirtualDataInRow {

    private String nameExpectedOutput = "";
    private String type = "";
    private String attributes = "";
    private String expectedValue = "";

    public VirtualDataInRow(String nameExpectedOutput, String type, String attributes, String expectedValue) {
        super();
        this.nameExpectedOutput = nameExpectedOutput;
        this.type = type;
        this.attributes = attributes;
        this.expectedValue = expectedValue;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    public String getNameExpectedOutput() {
        return nameExpectedOutput;
    }

    public void setNameExpectedOutput(String nameExpectedOutput) {
        this.nameExpectedOutput = nameExpectedOutput;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
