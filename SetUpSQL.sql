CREATE TABLE Books (
    BookID INT PRIMARY KEY,
    Title VARCHAR(255),
    Author VARCHAR(255),
    IsAvailable BOOLEAN DEFAULT TRUE
);

CREATE TABLE Students (
    StudentID INT PRIMARY KEY,
    StudentName VARCHAR(255),
    Email VARCHAR(255)
);

CREATE TABLE IssueBook (
    IssueID INT PRIMARY KEY AUTO_INCREMENT,
    BookID INT,
    StudentID INT,
    IssueDate DATE,
    DueDate DATE,
    FOREIGN KEY (BookID) REFERENCES Books(BookID),
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID)
);