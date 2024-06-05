package objects;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TestTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L; 

	private final List<Test> tests;
    private final String[] columnNames = {"TestID", "Test Type", "Billing"};

    public TestTableModel(List<Test> tests) {
        this.tests = tests;
    }

    @Override
    public int getRowCount() {
        return tests.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Test test = tests.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return test.getId();
            case 1:
                return test.getDescription();
            case 2:
                return test.getBilling();
            default:
                return null;
        }
    }
}