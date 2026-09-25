package com.uniportal.ui.common;

import com.uniportal.model.Timetable;
import com.uniportal.util.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TimetableMatrixPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Timetable> entries;
    
    // Day order as columns
    private static final String[] DAYS = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"};
    
    // Time slots from 10am to 6pm
    private static final String[] TIME_SLOTS = {
        "10:00:00 - 11:00:00",
        "11:00:00 - 12:00:00",
        "12:00:00 - 13:00:00",
        "13:00:00 - 14:00:00",
        "14:00:00 - 15:00:00",
        "15:00:00 - 16:00:00",
        "16:00:00 - 17:00:00",
        "17:00:00 - 18:00:00"
    };

    public TimetableMatrixPanel(List<Timetable> entries) {
        this.entries = entries;
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add panel padding
        
        buildTable();
    }
    
    private void buildTable() {
        List<String> columns = new ArrayList<>();
        columns.add("Time Slot");
        columns.addAll(Arrays.asList(DAYS));
        
        tableModel = new DefaultTableModel(columns.toArray(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Build rows
        for (String slot : TIME_SLOTS) {
            Object[] rowData = new Object[columns.size()];
            rowData[0] = slot; // First column is the time slot
            
            for (int i = 1; i < columns.size(); i++) {
                String day = columns.get(i);
                
                if (slot.equals("13:00:00 - 14:00:00")) {
                    rowData[i] = "<html><div style='text-align: center; color: #888888; font-style: italic;'>Lunch Break</div></html>";
                    continue;
                }
                
                // Find timetable entry for this day and slot
                Timetable match = null;
                for (Timetable t : entries) {
                    if (t.getDayOfWeek().equalsIgnoreCase(day)) {
                        String tSlot = t.getStartTime().toString() + " - " + t.getEndTime().toString();
                        if (tSlot.equals(slot)) {
                            match = t;
                            break;
                        }
                    }
                }
                rowData[i] = match; // Can be null
            }
            tableModel.addRow(rowData);
        }
        
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        table.setRowHeight(80); // Taller rows for padding and content
        table.setIntercellSpacing(new Dimension(10, 10)); // Add intercell spacing for less congestion
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultRenderer(Object.class, new TimetableCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public Timetable getSelectedTimetable() {
        int row = table.getSelectedRow();
        int col = table.getSelectedColumn();
        if (row != -1 && col > 0) { // Column 0 is Time Slot
            Object val = tableModel.getValueAt(row, col);
            if (val instanceof Timetable) {
                return (Timetable) val;
            }
        }
        return null;
    }
    
    class TimetableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Cell padding
            
            if (value instanceof Timetable) {
                Timetable t = (Timetable) value;
                String html = "<html><div style='text-align: center; padding: 5px;'>"
                        + "<b>" + t.getSubjectName() + "</b><br/>"
                        + "<span style='font-size: 0.9em; color: #555555;'>" + (t.getFacultyName() != null ? t.getFacultyName() : "N/A") + "</span>"
                        + "</div></html>";
                setText(html);
            } else if (value == null) {
                setText("");
            } else {
                setText(value.toString());
                setHorizontalAlignment(SwingConstants.CENTER);
            }
            
            return this;
        }
    }
}
