from CustomHashTable import CustomHashTable
from CSVLoader import CSVLoader
from Location import Location

class CoordinateSystem:
    #Constructor
    def __init__(self):
        self.location_file = "CSV Files/WGUPS Location File.csv"
        self.location_lookup_file = "CSV Files/WGUPS Location Lookup.csv"
        self.csv_loader = CSVLoader()
        self.line_data = []
        self.lookup_data = []
        self.hash_table = CustomHashTable()

    #Automatically load in relevant files and store your own data
    def load_from_files(self):
        with open(self.location_file, 'r') as f:
            self.line_data = f.read().split("\n")
            for i in range(len(self.line_data)):
                self.line_data[i] = self.line_data[i].split(",")
            f.close()
        with open(self.location_lookup_file, 'r') as f:
            self.lookup_data = f.read().split('\n')
            for i in range(len(self.lookup_data)):
                self.lookup_data[i] = self.lookup_data[i].split(',')
            f.close()
        del self.line_data[-1]
        for y in range(len(self.line_data)):
            for x in range(len(self.line_data[y])):
                if int(self.line_data[y][x]) == 1:
                    self.hash_table.add(self.lookup_data[y][x], (x + 1, y + 1))

    #Returns the coordinates for a given location
    def lookup_coordinates(self, location):
        loc_str = location.get_address()
        return self.hash_table.get(loc_str)
