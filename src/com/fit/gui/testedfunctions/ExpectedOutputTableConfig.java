package com.fit.gui.testedfunctions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Duong Td
 */
public class ExpectedOutputTableConfig {

    private List<RowConfig> rowConfigs;

    public ExpectedOutputTableConfig() {
        rowConfigs = new ArrayList<>();
    }

    public ExpectedOutputTableConfig(ExpectedOutputTableConfig other) {
        rowConfigs = new ArrayList<>();
        if (other != null && other.rowConfigs != null)
            for (RowConfig rowConfig : other.rowConfigs)
                rowConfigs.add(new RowConfig(rowConfig));
    }

    public ExpectedOutputTableConfig(int totalChildrenRow) {
        rowConfigs = new ArrayList<>();
        for (int fi = 0; fi < totalChildrenRow; fi++)
            rowConfigs.add(new RowConfig(0, 0, false));
    }

    public void addChildrenToRow(int rowIndex, int countRealChildren, int totalChildrenRow, boolean isArr) {
        RowConfig rowConfig = rowConfigs.get(rowIndex);
        rowConfig.setTotalChildrenRow(totalChildrenRow);
        rowConfig.setCountRealChildren(countRealChildren);
        rowConfig.setExpanded(true);
        rowConfig.setArray(isArr);

        for (int fi = rowIndex + 1; fi <= totalChildrenRow + rowIndex; fi++)
            rowConfigs.add(fi, new RowConfig(0, 0, false));
    }

    public List<RowConfig> getRowConfigs() {
        return rowConfigs;
    }

    public void setRowConfigs(List<RowConfig> rowConfigs) {
        this.rowConfigs = rowConfigs;
    }

    public void removeChildrenOfRow(int rowIndex, boolean isSaveData) {
        RowConfig rowConfig = rowConfigs.get(rowIndex);
        for (int fi = rowConfig.getTotalChildrenRow() + rowIndex; fi > rowIndex; fi--)
            rowConfigs.remove(fi);
        rowConfig.setTotalChildrenRow(0);
        rowConfig.setCountRealChildren(0);
        rowConfig.setExpanded(false);
    }

    /**
     * Data of one row in Expected Output Table
     *
     * @author Duong Td
     */
    public class RowConfig {

        private int countRealChildren = 0;

        private int totalChildrenRow = 0;

        private boolean isExpanded, isArray;

        private Object EOAttribute, EOValue;

        // children row of this row
        private List<RowConfig> rowConfigsChildren;

        public RowConfig() {
            countRealChildren = 0;
            totalChildrenRow = 0;
            isExpanded = false;
            rowConfigsChildren = new ArrayList<>();
        }

        public RowConfig(int countRealChildren, int totalChildrenRow, boolean isExpanded) {
            this.countRealChildren = totalChildrenRow;
            this.totalChildrenRow = countRealChildren;
            this.isExpanded = isExpanded;
            rowConfigsChildren = new ArrayList<>();
        }

        public RowConfig(int countRealChildrenren, int totalChildrenRow, Object EOAttribute, Object EOValue) {
            this.totalChildrenRow = totalChildrenRow;
            countRealChildren = countRealChildrenren;
            this.EOAttribute = EOAttribute;
            this.EOValue = EOValue;
        }

        public RowConfig(int countRealChildrenren, int totalChildrenRow, Object EOAttribute, Object EOValue,
                         boolean isArray) {
            this.totalChildrenRow = totalChildrenRow;
            countRealChildren = countRealChildrenren;
            this.EOAttribute = EOAttribute;
            this.EOValue = EOValue;
            this.isArray = isArray;
        }

        public RowConfig(RowConfig other) {
            countRealChildren = other.countRealChildren;
            totalChildrenRow = other.totalChildrenRow;
            isExpanded = other.isExpanded;
            isArray = other.isArray;
            EOAttribute = other.EOAttribute;
            EOValue = other.EOValue;
            rowConfigsChildren = new ArrayList<>(other.rowConfigsChildren);
        }

        public void addOneChildrenRow(Object EOAttribute, Object EOValue, int countRealCh, int totalChi,
                                      boolean isArr) {
            rowConfigsChildren.add(new RowConfig(countRealCh, totalChi, EOAttribute, EOValue, isArr));
        }

        public void addOneChildrenRow(RowConfig rowConfig) {
            rowConfigsChildren.add(rowConfig);
        }

        public int getCountRealChildren() {
            return countRealChildren;
        }

        public void setCountRealChildren(int countRealChildren) {
            this.countRealChildren = countRealChildren;
        }

        public Object getEOAttribute() {
            return EOAttribute;
        }

        public void setEOAttribute(Object eOAttribute) {
            EOAttribute = eOAttribute;
        }

        public Object getEOValue() {
            return EOValue;
        }

        public void setEOValue(Object eOValue) {
            EOValue = eOValue;
        }

        public List<RowConfig> getRowConfigsChildren() {
            return rowConfigsChildren;
        }

        public void setRowConfigsChildren(List<RowConfig> rowConfigsChildren) {
            this.rowConfigsChildren = rowConfigsChildren;
        }

        public int getTotalChildrenRow() {
            return totalChildrenRow;
        }

        public void setTotalChildrenRow(int totalChildrenRow) {
            this.totalChildrenRow = totalChildrenRow;
        }

        public boolean isArray() {
            return isArray;
        }

        public void setArray(boolean isArray) {
            this.isArray = isArray;
        }

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setExpanded(boolean _isExpanded) {
            isExpanded = _isExpanded;
        }

        public boolean isSaveBefore() {
            return rowConfigsChildren.size() > 0;
        }

        public void removeAllChildrenData() {
            rowConfigsChildren = new ArrayList<>();
        }

        public void removeOneChildrenRow() {
            rowConfigsChildren.remove(rowConfigsChildren.size() - 1);
        }

        public void removeRows(int countChildren) {
            for (int fi = 0; fi < countChildren; fi++)
                removeOneChildrenRow();
        }

        public void setFirstChildren(Object EOAttribute, Object EOValue, int countRealCh, int totalChi, boolean isArr) {
            if (!isSaveBefore())
                this.addOneChildrenRow(EOAttribute, EOValue, countRealCh, totalChi, isArr);
            else {
                rowConfigsChildren.get(0).setEOAttribute(EOAttribute);
                rowConfigsChildren.get(0).setEOValue(EOValue);
                rowConfigsChildren.get(0).setCountRealChildren(countRealCh);
                rowConfigsChildren.get(0).setTotalChildrenRow(totalChi);
                rowConfigsChildren.get(0).setArray(isArr);
            }
        }

        public void setFirstChildren(RowConfig rowConfig) {
            if (!isSaveBefore())
                this.addOneChildrenRow(rowConfig);
            else
                rowConfigsChildren.set(0, rowConfig);
        }

        @Override
        public String toString() {
            return "countRealChildren: " + countRealChildren + "\t\t" + "totalChildrenRow: " + totalChildrenRow + "\t\t"
                    + "Is expanded: " + isExpanded;
        }
    }
}
