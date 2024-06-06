package objects;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class OperationTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L; 

	private final List<Operation> ops;
    private final String[] columnNames = {"Operation ID", "Doctor Name", "Doctor Surname", "Operation Name", "Billing"};

    public OperationTableModel(List<Operation> ops) {
        this.ops = ops;
    }

    @Override
    public int getRowCount() {
        return ops.size();
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
        Operation op = ops.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return op.getId();
            case 1:
                return op.getDoctor_name();
            case 2:
                return op.getDoctor_sname();
            case 3:
            	return op.getDescript();
            case 4:
            	return op.getBilling();
            default:
                return null;
        }
    }
}