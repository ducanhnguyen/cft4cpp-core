package testdatagen.se.solver;

class Bien {

    protected static final int DOUBLE = 0;
    protected static final int INT = 1;

    protected static final int INT_ARRAY_ONE_DIMENSION = 2;

    protected static final int DOUBLE_ARRAY_ONE_DIMENSION = 3;

    protected static final int INT_ARRAY_TWO_DIMENSION = 4;

    protected static final int DOUBLE_ARRAY_TWO_DIMENSION = 5;

    private String name;

    private int type;

    protected Bien(String name, int type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean equals(Object b) {
        Bien tmp = (Bien) b;
        if (tmp.name.equals(name) && tmp.type == type)
            return true;
        return false;
    }

    protected String getName() {
        return name;
    }

    protected int getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + "," + type;
    }
}
