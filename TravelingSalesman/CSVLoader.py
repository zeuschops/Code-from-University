import csv
from Package import Package
from Location import Location

#Loads in CSV Files
class CSVLoader:
    #Here we take from filename and convert to properly structure CSV data list.
    #We also return the list from the function so we can use that later.
    #During read-in this has O(n) where n is the number of lines in the csv file.
    def load_from_file(self, filename):
        arr = []
        with open(filename, newline='') as csvfile:
            reader = csv.reader(csvfile)
            for row in reader:
                arr.append(row)
        return arr

    #We remove some useless header files, this has an O(n) where n will always be 7
    def remove_headers(self, arr):
        for i in range(7):
            del arr[0]
        return arr

    #Here we convert the file arrays that were returned previously to be used by Main.py
    #This takes O(m * n * o) as we use three different for loops all iterating over every variable in each iterator, so complexity is O(m * n * o) overall complexity.
    def files_to_package_location(self, arr, distance_arr):
        package_arr = []
        location_arr = []
        #O(n), where n is len(arr)
        for line in arr:
            if arr.index(line) > 0:
                in_location_arr = False
                where = -1
                for location in location_arr:
                    if line[1] + ';' + line[2] + ';' + line[3] + ';' + line[4] in str(location):
                        in_location_arr = True
                        where = location_arr.index(location) - 1
                if not in_location_arr:
                    for dist_line in distance_arr:
                        if line[1] in dist_line[1]:
                            location_arr.append(Location(line[1], line[2], line[3], line[4], dist_line[2:]))
                    where = len(location_arr) - 1
                package_arr.append(Package(str(int(line[0])), location_arr[where], line[6], line[5], line[7:]))
        return (location_arr, package_arr)
