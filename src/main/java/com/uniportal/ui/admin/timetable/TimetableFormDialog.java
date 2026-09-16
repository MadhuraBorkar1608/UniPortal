package com.uniportal.ui.admin.timetable;

import com.uniportal.model.Faculty;
import com.uniportal.model.Subject;
import com.uniportal.model.Timetable;
import com.uniportal.service.FacultyService;
import com.uniportal.service.SubjectService;
import com.uniportal.service.TimetableService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.sql.Time;
import java.util.List;

public class TimetableFormDialog extends JDialog {

    private JComboBox<String> dayCombo;
    private JTextField startTimeField;
    private JTextField endTimeField;
    private JComboBox<SubjectItem> subjectCombo;
    private JComboBox<FacultyItem> facultyCombo;
    private JTextField classroomField;
    private JTextField divisionField;
    
    private Timetable timetable;
    private TimetableService service;
    private boolean saved = false;

    public TimetableFormDialog(Window owner, Timetable timetable, TimetableService service) {
        super(owner, timetable == null ? "Add Timetable Entry" : "Edit Timetable Entry", ModalityType.APPLICATION_MODAL);
        this.timetable = timetable;
        this.service = service;
        
        setSize(450, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Division:"), gbc);
        divisionField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(divisionField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Day of Week:"), gbc);
        dayCombo = new JComboBox<>(new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"});
        gbc.gridx = 1; formPanel.add(dayCombo, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Start Time (HH:MM:SS):"), gbc);
        startTimeField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(startTimeField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("End Time (HH:MM:SS):"), gbc);
        endTimeField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(endTimeField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Subject:"), gbc);
        subjectCombo = new JComboBox<>();
        loadSubjects();
        gbc.gridx = 1; formPanel.add(subjectCombo, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Faculty:"), gbc);
        facultyCombo = new JComboBox<>();
        facultyCombo.addItem(new FacultyItem("", "None"));
        loadFaculty();
        gbc.gridx = 1; formPanel.add(facultyCombo, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Classroom:"), gbc);
        classroomField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(classroomField, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel();
        StyledButton saveBtn = new StyledButton("Save");
        StyledButton cancelBtn = new StyledButton("Cancel");
        
        saveBtn.addActionListener(e -> save());
        cancelBtn.addActionListener(e -> dispose());
        
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);
        
        if (timetable != null) {
            divisionField.setText(timetable.getDivision());
            dayCombo.setSelectedItem(timetable.getDayOfWeek());
            startTimeField.setText(timetable.getStartTime().toString());
            endTimeField.setText(timetable.getEndTime().toString());
            classroomField.setText(timetable.getClassroom());
            
            for (int i = 0; i < subjectCombo.getItemCount(); i++) {
                if (subjectCombo.getItemAt(i).id == timetable.getSubjectId()) {
                    subjectCombo.setSelectedIndex(i);
                    break;
                }
            }
            
            if (timetable.getFacultyId() != null) {
                for (int i = 0; i < facultyCombo.getItemCount(); i++) {
                    if (facultyCombo.getItemAt(i).id.equals(timetable.getFacultyId())) {
                        facultyCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }
    
    private void loadSubjects() {
        SubjectService ss = new SubjectService();
        List<Subject> subjects = ss.getAllSubjects();
        for (Subject s : subjects) {
            subjectCombo.addItem(new SubjectItem(s.getId(), s.getSubjectName()));
        }
    }
    
    private void loadFaculty() {
        FacultyService fs = new FacultyService();
        List<Faculty> faculties = fs.getAllFaculty();
        for (Faculty f : faculties) {
            facultyCombo.addItem(new FacultyItem(f.getFacultyId(), f.getFullName()));
        }
    }
    
    private void save() {
        try {
            Timetable t = timetable == null ? new Timetable() : timetable;
            t.setDivision(divisionField.getText().trim());
            t.setDayOfWeek((String) dayCombo.getSelectedItem());
            
            String startTimeText = startTimeField.getText().trim();
            if(startTimeText.length() == 5) startTimeText += ":00";
            String endTimeText = endTimeField.getText().trim();
            if(endTimeText.length() == 5) endTimeText += ":00";
            
            t.setStartTime(Time.valueOf(startTimeText));
            t.setEndTime(Time.valueOf(endTimeText));
            
            SubjectItem selSubj = (SubjectItem) subjectCombo.getSelectedItem();
            if (selSubj != null) t.setSubjectId(selSubj.id);
            
            FacultyItem selFac = (FacultyItem) facultyCombo.getSelectedItem();
            if (selFac != null && !selFac.id.isEmpty()) {
                t.setFacultyId(selFac.id);
            } else {
                t.setFacultyId(null);
            }
            
            t.setClassroom(classroomField.getText().trim());
            
            if (timetable == null) {
                service.addTimetable(t);
            } else {
                service.updateTimetable(t);
            }
            saved = true;
            dispose();
        } catch (IllegalArgumentException ex) {
            UIUtils.showError(this, "Invalid time format. Use HH:MM:SS.");
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }
    
    public boolean isSaved() {
        return saved;
    }
    
    private static class SubjectItem {
        int id;
        String name;
        SubjectItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
        @Override
        public String toString() { return name; }
    }
    
    private static class FacultyItem {
        String id;
        String name;
        FacultyItem(String id, String name) {
            this.id = id;
            this.name = name;
        }
        @Override
        public String toString() { return name; }
    }
}
