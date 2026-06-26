create database LMSdb;
use LMSdb;
CREATE TABLE Book (
    id 					varchar(36) 			primary key not null,
    title 				varchar(250)			default null,
    author 				varchar(100)			default null,
    isbn				varchar(13)				default null,
    created_date		datetime(6)				default null,
    update_date			datetime(6)				default null,
    available           boolean                 default true,
    publication_year	smallint				default null
)engine = InnoDB;

CREATE TABLE Student (
    id 					varchar(36) 			primary key not null,
    first_name	 		varchar(100)			default null,
    last_name			varchar(100)			default null,
    email 				varchar(100)			default null unique,
    created_date		datetime(6)				default null,
    update_date			datetime(6)				default null
    constraint uc_student_email unique (email)
)engine = InnoDB;

CREATE TABLE IssueBook (
    id 					varchar(36) 			primary key not null,
    book_id 			varchar(36)				not null,
    student_id 			varchar(36)				not null,
    issue_date 			datetime(6)				default null,
    status              varchar(255),
    update_date		 	datetime(6)				default null,
    due_date			date					default null,
    return_date			date					default null,
    constraint fk_book foreign key (book_id)  references Book(id),
    constraint fk_student foreign key (student_id) references Student(id)
)engine = InnoDB;
