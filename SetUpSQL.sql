CREATE TABLE Books (
    BookID INT PRIMARY KEY,
    Title VARCHAR(36),
    Author VARCHAR(36),
);

CREATE TABLE Students (
    StudentID INT PRIMARY KEY,
    StudentName VARCHAR(36),
    Email VARCHAR(36)
);

CREATE TABLE IssueBook (
    IssueID VARCHAR(36) PRIMARY KEY AUTO_INCREMENT,
    BookID VARCHAR(36),
    StudentID VARCHAR(36),
    IssueDate DATE,
    DueDate DATE,
    FOREIGN KEY (BookID) REFERENCES Books(BookID),
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID)
);
