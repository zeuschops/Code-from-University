import sqlite3 as sqlite

class SQLiteInterface:
    def __init__(self, filename):
        self.conn = sqlite.connect(filename)
        self.c = self.conn.cursor()

    def execute(self, command):
        self.c.execute(command)
        if "SELECT" in command:
            return self.c.fetchall()
        elif "INSERT" in command:
            self.conn.commit()

    def get_grade_by_id(self, id):
        rows = []
        for row in self.c.execute("SELECT * FROM grades WHERE id='" + str(id) + "';"):
            rows += [row]
        return rows

    def get_grades(self):
        rows = []
        for row in self.c.execute("SELECT * FROM grades;"):
            rows += [row]
        return rows

    def get_grades_by_course(self, id):
        rows = []
        for row in self.c.execute("SELECT * FROM grades WHERE course_id='" + str(id) + "';"):
            rows += [row]
        return rows
