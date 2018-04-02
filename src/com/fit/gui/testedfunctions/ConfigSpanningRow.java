package com.fit.gui.testedfunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Class for saving table model
 * <p>
 * table design -------------------------- | ID |Test Path|Call-In |
 * -------------------------- | | | I1.1 | | 1 | P1 |---------| | | | I1.2 |
 * -------------------------- | | | I2.1 | | 2 | P2 |---------| | | | I2.2 |
 * --------------------------
 *
 * @author Duong Td
 */
public class ConfigSpanningRow {

    // All cells in the table
    private List<CellSpan> cellSpans = new ArrayList<>();
    // The max real row of one ID row
    private Vector<Integer> maxRowPerID = new Vector<>();

    public ConfigSpanningRow() {
    }

    public ConfigSpanningRow(CellSpan... cells) {
        for (CellSpan c : cells)
            cellSpans.add(c);
    }

    public ConfigSpanningRow(List<CellSpan> c) {
        cellSpans = c;
    }

    public void add(int x, int y, int length) {
        CellSpan tempCell = new CellSpan(x, y, length);
        cellSpans.add(tempCell);
    }

    public Vector<Integer> getMaxRowPerID() {
        return maxRowPerID;
    }

    public void setMaxRowPerID(Vector<Integer> maxRowPerID) {
        this.maxRowPerID = maxRowPerID;
    }

    /**
     * @param rowReal : real row
     * @return: ID row
     */
    public int getRowID(int rowReal) {
        int index = 0;
        for (int fi = 0; fi < maxRowPerID.size(); fi++) {
            if (rowReal >= index && rowReal < index + maxRowPerID.get(fi))
                return fi;
            index += maxRowPerID.get(fi);
        }
        return -1;
    }

    public int isContain(int x, int y) {
        for (int fi = 0; fi < cellSpans.size(); fi++) {
            CellSpan c = cellSpans.get(fi);
            if (x == c.getX() && y == c.getY())
                return fi;
        }
        return -1;
    }

    public void remove(int x, int y, int length) {
        CellSpan tempCell = new CellSpan(x, y, length);
        cellSpans.remove(tempCell);
    }

    /**
     * @param row
     * @param column
     * @return cell At(row, column) spanning how many cell
     */
    public int span(int row, int column) {
        int tempIndex = isContain(row, column);
        if (tempIndex >= 0)
            return cellSpans.get(tempIndex).getLength();
        else
            return 1;
    }

    /**
     * @param row
     * @param column
     * @return if cell At(row, column) spanning -> return the first row of
     * spanning cell else -> return row
     */
    public int visibleCell(int row, int column) {
        for (int fi = 0; fi < cellSpans.size(); fi++) {
            CellSpan tempCell = cellSpans.get(fi);
            if (tempCell.getY() == column)
                if (row >= tempCell.getX() && row < tempCell.getX() + tempCell.getLength())
                    return tempCell.getX();
        }
        return row;
    }

    /**
     * Model for one cell
     *
     * @author Duong Td
     */
    public class CellSpan {
        // x, y are positions
        // Example cell P1 - (0,1,2); P2 - (1,1,2)

        private int x, y, length;

        public CellSpan(int x, int y, int length) {
            this.x = x;
            this.y = y;
            this.length = length;
        }

        @Override
        public boolean equals(Object ob) {
            if (!(ob instanceof CellSpan))
                return false;
            else {
                CellSpan other = (CellSpan) ob;
                return x == other.x && y == other.y && length == other.length;
            }
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}
