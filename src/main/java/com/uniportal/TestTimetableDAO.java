package com.uniportal;
import com.uniportal.dao.TimetableDAO;
import com.uniportal.model.Timetable;
import java.util.List;

public class TestTimetableDAO {
    public static void main(String[] args) {
        TimetableDAO dao = new TimetableDAO();
        List<Timetable> all = dao.getAllTimetables();
        System.out.println("All timetables count: " + all.size());
        for (Timetable t : all) {
            System.out.println(t.getDeptName() + " | " + t.getDivision() + " | " + t.getDayOfWeek() + " | " + t.getStartTime() + "-" + t.getEndTime());
        }
        
        System.out.println("----- CS Dept, Div A -----");
        List<Timetable> csA = dao.getTimetableForDivision("A", 1);
        System.out.println("CS A timetables count: " + csA.size());
        for (Timetable t : csA) {
            System.out.println(t.getSubjectName() + " | " + t.getDayOfWeek() + " | " + t.getStartTime() + "-" + t.getEndTime());
        }
    }
}
