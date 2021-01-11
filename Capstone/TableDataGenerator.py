import sqlite3 as sqlite
import random
import os

cwd = os.getcwd()
if "tests" in cwd:
    cwds = cwd.split('/' if '/' in cwd else "\\")
    cwds = cwds[0:-1]
    cwd = '/'.join(cwds)

conn = sqlite.connect(cwd + ('/' if '/' not in cwd[-1] else '') + 'test.db')
c = conn.cursor()

#Insert some fake grades for some fake students here...
def insert_class(course_id, course_name, start_at):
    c.execute("INSERT INTO courses(course_id, course_name) VALUES (" + str(course_id) + ",'" + course_name.lower() + "')")
    for i in range(50):
        for j in range(6):
            c.execute("INSERT INTO grades(student_id, course_id, school_id, assignment_number, grade, max_grade) VALUES (" + str(i + start_at) + "," + str(course_id) + ",0," + str(j) + "," + str(round(random.uniform(0, 20),1)) + ",20)")

insert_class(0, "UNDERWATER BASKET WEAVING", 0)
insert_class(1, "PE", 0)
insert_class(2, "English", 0)
insert_class(3, "French", 0)
insert_class(4, "Calculus", 0)
insert_class(5, "Robotics", 0)


conn.commit()
