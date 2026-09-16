package com.uniportal.service;

import com.uniportal.dao.StudyMaterialDAO;
import com.uniportal.model.StudyMaterial;
import java.util.List;

public class StudyMaterialService {
    
    private StudyMaterialDAO dao;

    public StudyMaterialService() {
        this.dao = new StudyMaterialDAO();
    }

    public List<StudyMaterial> getStudentMaterials(int courseId, int semester) {
        return dao.getMaterialsByCourseAndSemester(courseId, semester);
    }

    public List<StudyMaterial> getAllStudyMaterials() {
        return dao.getAllStudyMaterials();
    }

    public void addStudyMaterial(StudyMaterial m) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(m.getTitle(), "Title");
        if (m.getSubjectId() <= 0) {
            throw new IllegalArgumentException("Subject is required.");
        }
        if (!dao.addStudyMaterial(m)) {
            throw new RuntimeException("Failed to add study material.");
        }
    }

    public void updateStudyMaterial(StudyMaterial m) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(m.getTitle(), "Title");
        if (m.getSubjectId() <= 0) {
            throw new IllegalArgumentException("Subject is required.");
        }
        if (!dao.updateStudyMaterial(m)) {
            throw new RuntimeException("Failed to update study material.");
        }
    }

    public void deleteStudyMaterial(int id) {
        if (!dao.deleteStudyMaterial(id)) {
            throw new RuntimeException("Failed to delete study material.");
        }
    }
}
