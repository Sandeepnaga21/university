CREATE TABLE professor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    department VARCHAR(255)
);

CREATE TABLE course (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    credits VARCHAR(255),
    professorId INT,
    FOREIGN KEY(professorId) REFERENCES professor(id)
);

CREATE TABLE student (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255)
);

CREATE TABLE course_student (
    courseId INT,
    studentId INT,
    PRIMARY KEY(courseId, studentId),
    FOREIGN KEY(courseId) REFERENCES course(id),
    FOREIGN KEY(studentId) REFERENCES student(id)
);