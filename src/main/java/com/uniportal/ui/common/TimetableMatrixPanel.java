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
    
    // Day order
    private static final String[] DAYS = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};

    public TimetableMatrixPanel(List<Timetable> entries) {
        this.entries = entries;
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        
        buildTable();
    }
    
    private void buildTable() {
        // Find unique time slots (e.g. "10:00:00 - 11:00:00")
        Set<String> timeSlots = new TreeSet<>();
        for (Timetable t : entries) {
            timeSlots.add(t.getStartTime().toString() + " - " + t.getEndTime().toString());
        }
        
        List<String> columns = new ArrayList<>();
        columns.add("Day");
        columns.addAll(timeSlots);
        
        tableModel = new DefaultTableModel(columns.toArray(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Build rows
        for (String day : DAYS) {
            Object[] rowData = new Object[columns.size()];
            rowData[0] = day; // First column is the day
            
            for (int i = 1; i < columns.size(); i++) {
                String slot = columns.get(i);
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
        table.setRowHeight(60); // Taller rows for multiple lines
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultRenderer(Object.class, new TimetableCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public Timetable getSelectedTimetable() {
        int row = table.getSelectedRow();
        int col = table.getSelectedColumn();
        if (row != -1 && col > 0) { // Column 0 is Day name
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
            
            if (value instanceof Timetable) {
                Timetable t = (Timetable) value;
                String html = "<html><div style='text-align: center;'>"
                        + t.getSubjectName() + "<br/>"
                        + t.getClassroom() + "<br/>"
                        + "<span style='font-size: 0.9em; color: gray;'>(" + (t.getFacultyName() != null ? t.getFacultyName() : "N/A") + ")</span>"
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
