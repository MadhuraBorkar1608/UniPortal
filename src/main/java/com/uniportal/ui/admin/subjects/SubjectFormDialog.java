package com.uniportal.ui.admin.subjects;

import com.uniportal.model.Course;
import com.uniportal.model.Faculty;
import com.uniportal.model.Subject;
import com.uniportal.service.CourseService;
import com.uniportal.service.FacultyService;
import com.uniportal.service.SubjectService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SubjectFormDialog extends JDialog {

    private JTextField codeField;
    private JTextField nameField;
    private JComboBox<Course> courseCombo;
    private JTextField semesterField;
    private JTextField creditsField;
    private JComboBox<Faculty> facultyCombo;
    private boolean saved = false;
    private Subject subject;
    private SubjectService service;

    public SubjectFormDialog(Window parent, Subject s, SubjectService service) {
        super(parent, s == null ? "Add Subject" : "Edit Subject", ModalityType.APPLICATION_MODAL);
        this.subject = s;
        this.service = service;

        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Subject Code:"));
        codeField = new JTextField();
        formPanel.add(codeField);

        formPanel.add(new JLabel("Subject Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Course:"));
        courseCombo = new JComboBox<>();
        loadCourses();
        formPanel.add(courseCombo);

        formPanel.add(new JLabel("Semester:"));
        semesterField = new JTextField();
        formPanel.add(semesterField);

        formPanel.add(new JLabel("Credits:"));
        creditsField = new JTextField();
        formPanel.add(creditsField);

        formPanel.add(new JLabel("Faculty:"));
        facultyCombo = new JComboBox<>();
        loadFaculty();
        formPanel.add(facultyCombo);

        if (s != null) {
            codeField.setText(s.getSubjectCode());
            nameField.setText(s.getSubjectName());
            semesterField.setText(String.valueOf(s.getSemester()));
            creditsField.setText(String.valueOf(s.getCredits()));
            setCourseCombo(s.getCourseId());
            setFacultyCombo(s.getFacultyId());
        }

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        StyledButton saveBtn = new StyledButton("Save");
        saveBtn.addActionListener(e -> save());
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadCourses() {
        CourseService cs = new CourseService();
        List<Course> courses = cs.getAllCourses();
        for (Course c : courses) {
            courseCombo.addItem(c);
        }
    }

    private void loadFaculty() {
        FacultyService fs = new FacultyService();
        List<Faculty> faculties = fs.getAllFaculty();
        facultyCombo.addItem(new Faculty()); // Blank option if no faculty assigned
        for (Faculty f : faculties) {
            facultyCombo.addItem(f);
        }
    }
    
    private void setCourseCombo(int courseId) {
        for (int i = 0; i < courseCombo.getItemCount(); i++) {
            if (courseCombo.getItemAt(i).getId() == courseId) {
                courseCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void setFacultyCombo(String facultyId) {
        if (facultyId == null) return;
        for (int i = 1; i < facultyCombo.getItemCount(); i++) {
            if (facultyId.equals(facultyCombo.getItemAt(i).getFacultyId())) {
                facultyCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void save() {
        try {
            Course selCourse = (Course) courseCombo.getSelectedItem();
            if (selCourse == null) throw new Exception("Please select a course.");
            
            Faculty selFaculty = (Faculty) facultyCombo.getSelectedItem();
            String fid = null;
            if (selFaculty != null && selFaculty.getFacultyId() != null) {
                fid = selFaculty.getFacultyId();
            }

            int sem = Integer.parseInt(semesterField.getText());
            int cred = Integer.parseInt(creditsField.getText());
            
            if (subject == null) {
                subject = new Subject();
                subject.setSubjectCode(codeField.getText());
                subject.setSubjectName(nameField.getText());
                subject.setCourseId(selCourse.getId());
                subject.setSemester(sem);
                subject.setCredits(cred);
                subject.setFacultyId(fid);
                service.addSubject(subject);
            } else {
                subject.setSubjectCode(codeField.getText());
                subject.setSubjectName(nameField.getText());
                subject.setCourseId(selCourse.getId());
                subject.setSemester(sem);
                subject.setCredits(cred);
                subject.setFacultyId(fid);
                service.updateSubject(subject);
            }
            saved = true;
            UIUtils.showSuccess(this, "Saved successfully.");
            dispose();
        } catch (NumberFormatException ex) {
            UIUtils.showError(this, "Semester and Credits must be numbers.");
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }

    public boolean isSaved() {
        return saved;
    }
}
