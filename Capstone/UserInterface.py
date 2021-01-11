from SQLiteInterface import SQLiteInterface
from matplotlib import pyplot as plt
from LinearRegression import LinearRegression

class UserInterface:
    def __init__(self, filename):
        self.sqli = SQLiteInterface(filename)

    def get_course_id(self):
        try:
            stud_id = int(input("What is the Student ID of the student?"))
            course = input("What is the course name that you would like to view?")
            course_id = self.sqli.execute("SELECT course_id FROM courses WHERE course_name='" + course + "';")
            keep_checking = True
            if type(course_id) is type([]):
                while len(course_id) == 0 and keep_checking:
                    print("Course cannot be found, would you like to try again?")
                    inp = input("(Y\\n): ")
                    if inp.lower().startswith("n"):
                        keep_checking = False
                    else:
                        course_id = self.sqli.execute("SELECT course_id FROM courses WHERE course_name='" + input("What is the name of the course you would like to view for student (" + str(stud_id) + ")? ") + "'")
                    course_id = course_id[0][2]
            return stud_id, course_id
        except Error as err:
            print(err)
        return -1

    def predict(self):
        stud_id, course_id = self.get_course_id()
        if type(course_id) is type([]):
            course_id = course_id[0][0]
        grades = self.sqli.execute("SELECT grade, max_grade FROM grades WHERE student_id=" + str(stud_id) + " AND course_id=" + str(course_id))
        grds = []
        for i in range(len(grades)):
            if len(grades[i]) > 1:
                grds += [grades[i][0] / grades[i][1]]
        if len(grds) > 1:
            li = LinearRegression(grds)
            slope = li.slope()
            intercept = li.intercept()
            assignment_count = int(input("How many assignments will there be in the course?"))
            end_score = intercept + (slope * assignment_count)
            print("\nThe end score for the course will be about:", round(end_score * 100, 1), "% - at the student's current pace.\n")
        else:
            print("\nThere is not enough information to predict the student's final grade, please wait for more assignment scores to be entered before making a prediction attempt.\n\tCurrent scores:")
            for i in grds:
                if type(i) is type(()) or type(i) is type([]):
                    print("\t\t", round(i * 100,1))
                else:
                    print("\t\t", i)
            if len(grds) == 0:
                print("\tNo scores available at this time.")
            print("")


    def assign_input(self):
        course_name = input("What is the name of the course you are looking to input scores for? ").lower()
        ret = self.sqli.execute("SELECT course_id FROM courses WHERE course_name='" + course_name + "'")
        if len(ret) == 0:
            course_id = int(input("What is the course ID for " + course_name + "?"))
            self.sqli.execute("INSERT INTO courses(course_id, course_name) VALUES (" + str(course_id) + ", '" + course_name.lower() + "')")
        scores = int(input("How many students' scores would you like to enter? "))
        score = float(input("Please enter the maximum score possible for the assignment: "))
        for i in range(scores):
            prefix = "(" + str(i) + "/" + str(scores) + ")"
            student_id = int(input(prefix + " Please enter the student's ID: "))
            student_score = float(input(prefix + " Please enter the student's score:"))

    def average_prediction(self):
        courses = self.sqli.execute('SELECT * FROM courses')
        course_ids = []
        course_list = {}
        for row in courses:
            if row[1] not in course_ids:
                course_ids.append(row[1])
            if row[1] not in list(course_list):
                course_list.update({str(row[1]):row[2]})
        grades = []
        for course_id in course_ids:
            grades += self.sqli.get_grades_by_course(course_id)
        grade_avg = []
        all_grades = {}

        for row in grades:
            if str(row[2]) in list(all_grades):
                all_grades[str(row[2])].append((row[-2]/row[-1]))
            else:
                all_grades.update({str(row[2]):[(row[-2]/row[-1])]})
        print("All grades:", all_grades)
        print("How many assignments are there?")
        assignment_count = int(input("(1/2/3...) >> "))
        current = 0
        for key in list(all_grades):
            x_s = []
            y_s = []
            li = LinearRegression(all_grades[key])
            slope = li.slope()
            intercept = li.intercept()
            for i in range(assignment_count):
                x_s.append(i + 1)
                y_s.append(intercept + (i * slope))
            plt.plot(x_s, y_s, label=course_list[str(key)])
        plt.xlim(x_s[0],x_s[-1])
        plt.ylim(0, 1.0)
        plt.xlabel('assignment number')
        plt.ylabel('grade (in % as decimal)')
        plt.title('Average Grade Prediction')
        plt.legend()
        plt.show()

    def final_grade_prediction(self):
        #TODO: Use Linear regression to identify what a student's grade may look like on a graph based on current projections
        print("What is the student's ID number?")
        student_id = input(">> ")
        print("How many assignments are there for this student in total?")
        assignments = int(input("(1/2/3..) >> "))
        sql_resp = self.sqli.execute("SELECT * FROM grades WHERE student_id=" + str(student_id))
        courses = self.sqli.execute("SELECT * FROM courses")
        course_names = {}
        for row in courses:
            course_names.update({str(row[1]):row[2]})
        if len(sql_resp) == 0:
            print("Sorry, the student ID is not in the table. Please try again.")
            return
        sort_by_course = {}
        for row in sql_resp:
            if str(row[2]) in list(sort_by_course):
                sort_by_course[str(row[2])].append((row[-2]/row[-1]))
            else:
                sort_by_course.update({str(row[2]):[(round(row[-2]/row[-1], 2))]})
        avg = 0
        for course in list(sort_by_course):
            if len(sort_by_course[course]) < 2:
                print("Not enough information for a course. Cancelling operation...")
                return
            li = LinearRegression(sort_by_course[course])
            x_s = []
            y_s = []
            for i in range(assignments+1):
                x_s.append(i)
                y_s.append(li.intercept() + (i * li.slope()))
            plt.plot(x_s, y_s, label=course_names[str(course)])
        plt.xlim(0, assignments)
        plt.ylim(0.0, 1.0)
        plt.xlabel('assignment number')
        plt.ylabel('grade (in % as decimal)')
        plt.title('Final Grade Prediction')
        plt.legend()
        plt.show()

    def sqlite_csv_dump(self):
        courses = self.sqli.execute("SELECT * FROM courses")
        grades = self.sqli.execute("SELECT * FROM grades")
        courses_str = "id,course_id,course_name\n"
        for row in courses:
            for part in row:
                courses_str += str(part) + ","
            courses_str = courses_str[:-1]
            courses_str += '\n'
        grades_str = "id,student_id,course_id,school_id,assignment_number,grade,max_grade\n"
        for row in grades:
            for part in row:
                if type(part) is type(''):
                    grades_str += '\"' + part + '",'
                else:
                    grades_str += str(part) + ','
            grades_str = grades_str[:-1]
            grades_str += '\n'
        f = open('courses.csv','w')
        f.write(courses_str)
        f.close()
        f = open('grades.csv','w')
        f.write(grades_str)
        f.close()

    def main_chungus(self):
        option = -1
        while option < 1:
            if option == -1:
                print("What would you like to do?")
                print("1. Predict student grades based on number of assignments and current assignment completion")
                print("2. Average current grade predictions by course")
                print("3. Student's final grade point average predictions")
                print("4. Input assignment score for course")
                print("5. Dump to CSV")
                print("6. exit")
            option = int(input("(1/2/3/4/5) >> ")[0])
            if option == 1:
                self.predict()
            elif option == 2:
                self.average_prediction()
            elif option == 3:
                self.final_grade_prediction()
            elif option == 4:
                self.assign_input()
            elif option == 5:
                self.sqlite_csv_dump()
            else:
                exit()
            option = -1 if "y" in input("Continue to menu? (Y/n): ") else option
