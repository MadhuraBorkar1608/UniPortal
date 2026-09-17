-- College Management System Database Schema and Demo Data




-- --------------------------------------------------------
-- Users (Authentication Data)
-- Roles: 'STUDENT', 'ADMIN'
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('STUDENT', 'ADMIN') NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------------------
-- Admins Profile
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS admins (
    admin_id VARCHAR(20) PRIMARY KEY,
    user_id INT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    designation VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Departments
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dept_code VARCHAR(20) UNIQUE NOT NULL,
    dept_name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------------------
-- Courses
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    course_name VARCHAR(100) NOT NULL,
    dept_id INT NOT NULL,
    duration_years INT NOT NULL,
    FOREIGN KEY (dept_id) REFERENCES departments(id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Students Profile
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(20) PRIMARY KEY,
    user_id INT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    dept_id INT,
    course_id INT,
    current_semester INT,
    division VARCHAR(10),
    address TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (dept_id) REFERENCES departments(id) ON DELETE SET NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE SET NULL
);

-- --------------------------------------------------------
-- Faculty Profile
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS faculty (
    faculty_id VARCHAR(20) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    dept_id INT,
    designation VARCHAR(50),
    FOREIGN KEY (dept_id) REFERENCES departments(id) ON DELETE SET NULL
);

-- --------------------------------------------------------
-- Subjects
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS subjects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject_code VARCHAR(20) UNIQUE NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    course_id INT NOT NULL,
    semester INT NOT NULL,
    credits INT NOT NULL,
    faculty_id VARCHAR(20),
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id) ON DELETE SET NULL
);

-- --------------------------------------------------------
-- Notices
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS notices (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    dept_id INT, -- NULL means for all departments
    publish_date DATE NOT NULL,
    status ENUM('DRAFT', 'PUBLISHED') DEFAULT 'DRAFT',
    FOREIGN KEY (dept_id) REFERENCES departments(id) ON DELETE SET NULL
);

-- --------------------------------------------------------
-- Notice Attachments
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS notice_attachments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    notice_id INT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    FOREIGN KEY (notice_id) REFERENCES notices(id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Exam Timetable
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS exam_timetable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject_id INT NOT NULL,
    exam_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    venue VARCHAR(100),
    exam_type VARCHAR(50),
    division VARCHAR(10),
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Study Material
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS study_material (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    subject_id INT NOT NULL,
    description TEXT,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- College Timetable
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS college_timetable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    day_of_week ENUM('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY') NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    subject_id INT NOT NULL,
    faculty_id VARCHAR(20),
    classroom VARCHAR(50),
    division VARCHAR(10),
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id) ON DELETE SET NULL
);

-- --------------------------------------------------------
-- Attendance
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS attendance_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    subject_id INT NOT NULL,
    attendance_date DATE NOT NULL,
    status ENUM('PRESENT', 'ABSENT') NOT NULL,
    UNIQUE KEY (student_id, subject_id, attendance_date),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Assignments
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS assignments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    subject_id INT NOT NULL,
    description TEXT,
    deadline DATETIME NOT NULL,
    attachment_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Assignment Submissions
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS assignment_submissions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    assignment_id INT NOT NULL,
    student_id VARCHAR(20) NOT NULL,
    submission_path VARCHAR(255) NOT NULL,
    submission_time DATETIME NOT NULL,
    status ENUM('PENDING', 'SUBMITTED', 'LATE', 'GRADED') DEFAULT 'SUBMITTED',
    grade VARCHAR(20),
    feedback TEXT,
    UNIQUE KEY (assignment_id, student_id),
    FOREIGN KEY (assignment_id) REFERENCES assignments(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Events
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(200) NOT NULL,
    description TEXT,
    event_date DATE NOT NULL,
    event_time TIME,
    venue VARCHAR(100),
    organizer VARCHAR(100),
    dept_id INT, -- NULL if college-wide
    registration_deadline DATETIME,
    capacity INT,
    status ENUM('DRAFT', 'PUBLISHED', 'COMPLETED', 'CANCELLED') DEFAULT 'PUBLISHED',
    FOREIGN KEY (dept_id) REFERENCES departments(id) ON DELETE SET NULL
);

-- --------------------------------------------------------
-- Academic Calendar Unified
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS academic_calendar (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200),
    description TEXT,
    event_date DATE NOT NULL,
    event_time TIME,
    event_type ENUM('THEORY_EXAM', 'PRACTICAL_EXAM', 'UNIT_TEST', 'HOLIDAY', 'OTHER') DEFAULT 'OTHER',
    duration_minutes INT,
    subject_id INT,
    dept_id INT, -- NULL if college-wide
    status ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED') DEFAULT 'SCHEDULED',
    FOREIGN KEY (dept_id) REFERENCES departments(id) ON DELETE SET NULL,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE SET NULL
);

-- --------------------------------------------------------
-- Event Registrations
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS event_registrations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    student_id VARCHAR(20) NOT NULL,
    registration_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('REGISTERED', 'CANCELLED') DEFAULT 'REGISTERED',
    UNIQUE KEY (event_id, student_id),
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);



-- --------------------------------------------------------
-- Helpdesk Tickets
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS helpdesk_tickets (
    ticket_id VARCHAR(20) PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    category VARCHAR(50) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    attachment_path VARCHAR(255),
    status ENUM('OPEN', 'IN PROGRESS', 'RESOLVED', 'CLOSED') DEFAULT 'OPEN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Ticket Replies
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS ticket_replies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id VARCHAR(20) NOT NULL,
    reply_text TEXT NOT NULL,
    replied_by_user_id INT NOT NULL, -- references users(id) to know if admin or student replied
    reply_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_id) REFERENCES helpdesk_tickets(ticket_id) ON DELETE CASCADE,
    FOREIGN KEY (replied_by_user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ========================================================
-- DEMO DATA
-- ========================================================

-- Users (passwords are 'password' hashed with jBCrypt: $2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGLcZEiGDMVr5yUP1KUOYTa)
INSERT INTO users (username, password_hash, role) VALUES 
('admin', '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGLcZEiGDMVr5yUP1KUOYTa', 'ADMIN'),
('STU001', '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGLcZEiGDMVr5yUP1KUOYTa', 'STUDENT'),
('STU002', '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGLcZEiGDMVr5yUP1KUOYTa', 'STUDENT');

-- Admins
INSERT INTO admins (admin_id, user_id, full_name, email, phone, designation) VALUES 
('ADM001', 1, 'System Administrator', 'admin@college.edu', '1234567890', 'Super Admin');

-- Departments
INSERT INTO departments (id, dept_code, dept_name, description) VALUES 
(1, 'COMP', 'Computer Engineering', 'Department of Computer Engineering'),
(2, 'IT', 'Information Technology', 'Department of Information Technology');

-- Courses
INSERT INTO courses (id, course_code, course_name, dept_id, duration_years) VALUES 
(1, 'BTECH-CE', 'B.Tech in Computer Engineering', 1, 4),
(2, 'BTECH-IT', 'B.Tech in Information Technology', 2, 4);

-- Students
INSERT INTO students (student_id, user_id, full_name, email, phone, dept_id, course_id, current_semester, division, address) VALUES 
('STU001', 2, 'Rahul Sharma', 'rahul.sharma@email.com', '9876543210', 1, 1, 5, 'A', '123 Green Park, Pune'),
('STU002', 3, 'Priya Singh', 'priya.singh@email.com', '9876543211', 2, 2, 3, 'B', '456 Blue Street, Mumbai');

-- Faculty
INSERT INTO faculty (faculty_id, full_name, email, phone, dept_id, designation) VALUES 
('FAC001', 'Dr. Arvind Patel', 'arvind.patel@college.edu', '1122334455', 1, 'Professor'),
('FAC002', 'Prof. Sunita Rao', 'sunita.rao@college.edu', '2233445566', 2, 'Assistant Professor');

-- Subjects
INSERT INTO subjects (id, subject_code, subject_name, course_id, semester, credits, faculty_id) VALUES 
(1, 'CS301', 'Java Programming', 1, 5, 4, 'FAC001'),
(2, 'CS302', 'DBMS', 1, 5, 4, 'FAC002'),
(3, 'IT201', 'Data Structures', 2, 3, 4, 'FAC001');

-- Notices
INSERT INTO notices (title, description, category, publish_date, status) VALUES 
('Internal Examination Schedule', 'The internal examination will be conducted from 12 Sept.', 'Academic', '2026-09-05', 'PUBLISHED'),
('Hackathon Registration Open', 'Register for the coding hackathon before 12 Sept.', 'Event', '2026-09-04', 'PUBLISHED');

-- Events
INSERT INTO events (event_name, description, event_date, event_time, venue, organizer, capacity, status) VALUES
('Coding Hackathon', 'Annual coding hackathon', '2026-09-15', '09:00:00', 'Auditorium', 'Computer Dept', 100, 'PUBLISHED'),
('Tech Seminar', 'Seminar on AI and ML', '2026-09-20', '10:00:00', 'Seminar Hall', 'IT Dept', 80, 'PUBLISHED');

-- Event Registrations
INSERT INTO event_registrations (event_id, student_id) VALUES
(1, 'STU001');

-- Helpdesk
INSERT INTO helpdesk_tickets (ticket_id, student_id, category, subject, description, status) VALUES
('HD001', 'STU001', 'Academic', 'Exam Issue', 'Not able to view exam schedule', 'IN PROGRESS'),
('HD002', 'STU002', 'General', 'ID Card Issue', 'Lost my ID card', 'RESOLVED');

-- Assignments
INSERT INTO assignments (title, subject_id, description, deadline) VALUES
('Java Programming Assignment 3', 1, 'Implement OOP concepts.', '2026-09-10 23:59:00');

-- Timetable
INSERT INTO college_timetable (day_of_week, start_time, end_time, subject_id, faculty_id, classroom, division) VALUES
('MONDAY', '09:00:00', '10:00:00', 1, 'FAC001', 'Room 201', 'A'),
('MONDAY', '10:00:00', '11:00:00', 2, 'FAC002', 'Room 202', 'A');

-- Academic Calendar
INSERT INTO academic_calendar (event_date, event_type, description) VALUES
('2026-09-12', 'THEORY_EXAM', 'Mid-term exams start');

-- Attendance
INSERT INTO attendance_records (student_id, subject_id, attendance_date, status) VALUES
('STU001', 1, '2026-09-01', 'PRESENT'),
('STU001', 2, '2026-09-01', 'PRESENT'),
('STU001', 1, '2026-09-02', 'PRESENT'),
('STU001', 2, '2026-09-02', 'ABSENT');

