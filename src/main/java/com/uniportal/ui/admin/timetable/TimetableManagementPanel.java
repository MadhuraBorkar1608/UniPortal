package com.uniportal.ui.admin.timetable;

import com.uniportal.model.Department;
import com.uniportal.model.Timetable;
import com.uniportal.service.DepartmentService;
import com.uniportal.service.TimetableService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.ui.common.TimetableMatrixPanel;
import com.uniportal.util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TimetableManagementPanel extends JPanel {

    private TimetableService service;
    private DepartmentService deptService;
    private JComboBox<String> deptComboBox;
    private JTabbedPane tabbedPane;
    private List<Timetable> allTimetables;

    public TimetableManagementPanel() {
        service = new TimetableService();
        deptService = new DepartmentService();
        
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        
        // Department Combo Box
        deptComboBox = new JComboBox<>();
        deptComboBox.addActionListener(e -> renderTimetables());
        topPanel.add(new JLabel("Department: "));
        topPanel.add(deptComboBox);
        
        StyledButton addBtn = new StyledButton("Add Timetable Entry");
        addBtn.addActionListener(e -> showFormDialog(null));
        topPanel.add(addBtn);
        add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        StyledButton editBtn = new StyledButton("Edit");
        StyledButton deleteBtn = new StyledButton("Delete");

        editBtn.addActionListener(e -> {
            Timetable t = getSelectedTimetableFromActiveTab();
            if (t != null) {
                showFormDialog(t);
            } else {
                UIUtils.showError(this, "Please select an entry in the grid to edit.");
            }
        });

        deleteBtn.addActionListener(e -> {
            Timetable t = getSelectedTimetableFromActiveTab();
            if (t != null) {
                if (UIUtils.confirm(this, "Are you sure you want to delete this entry?")) {
                    try {
                        service.deleteTimetable(t.getId());
                        UIUtils.showSuccess(this, "Deleted successfully.");
                        loadData();
                    } catch (Exception ex) {
                        UIUtils.showError(this, ex.getMessage());
                    }
                }
            } else {
                UIUtils.showError(this, "Please select an entry in the grid to delete.");
            }
        });

        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);
        
        loadDepartments();
    }
    
    private void loadDepartments() {
        deptComboBox.removeAllItems();
        List<Department> depts = deptService.getAllDepartments();
        for (Department d : depts) {
            deptComboBox.addItem(d.getDeptName());
        }
    }

    public void loadData() {
        allTimetables = service.getAllTimetables();
        renderTimetables();
    }
    
    private void renderTimetables() {
        if (allTimetables == null) return;
        
        String selectedDept = (String) deptComboBox.getSelectedItem();
        if (selectedDept == null) return;
        
        tabbedPane.removeAll();
        
        List<Timetable> deptTimetables = allTimetables.stream()
                .filter(t -> selectedDept.equals(t.getDeptName()))
                .collect(Collectors.toList());
                
        // Group by division
        Map<String, List<Timetable>> byDivision = deptTimetables.stream()
                .collect(Collectors.groupingBy(Timetable::getDivision));
                
        // Sort divisions alphabetically
        List<String> divisions = new ArrayList<>(byDivision.keySet());
        divisions.sort(String::compareTo);
        
        for (String div : divisions) {
            TimetableMatrixPanel matrixPanel = new TimetableMatrixPanel(byDivision.get(div));
            tabbedPane.addTab("Division " + div, matrixPanel);
        }
    }

    private Timetable getSelectedTimetableFromActiveTab() {
        Component selectedTab = tabbedPane.getSelectedComponent();
        if (selectedTab instanceof TimetableMatrixPanel) {
            return ((TimetableMatrixPanel) selectedTab).getSelectedTimetable();
        }
        return null;
    }

    private void showFormDialog(Timetable t) {
        TimetableFormDialog dialog = new TimetableFormDialog(SwingUtilities.getWindowAncestor(this), t, service);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }
}
