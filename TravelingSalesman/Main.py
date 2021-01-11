from CSVLoader import CSVLoader
from TimeQueue import TimeQueue
from Package import Package
from Location import Location
from Solution import Solution
import datetime

#Custom CSV Loading that assists with conversions;
# Load in data according to pre-created files;
csvl = CSVLoader()
read_in = csvl.load_from_file('CSV Files/WGUPS Package File.csv')
read_in = csvl.remove_headers(read_in)

read_in_2 = csvl.load_from_file('CSV Files/WGUPS Distance Table.csv')
read_in_2 = csvl.remove_headers(read_in_2)

#Build a general location array and a general package array, both are associated together
(location_arr, package_arr) = csvl.files_to_package_location(read_in, read_in_2)

#Build a solution and sort the routes
solution = Solution()
solution.sort_packages(package_arr)
wgu_addr = read_in_2[1][0].split(",")
home_location = Location(wgu_addr[0][wgu_addr[0].index('4001'):], wgu_addr[1], wgu_addr[2].split(" ")[0], wgu_addr[2].split(' ')[1], read_in_2[1][2:])
(path_one, path_two) = solution.sort_distances_three(package_arr, home_location)

#Print out the routes
solution.user_interface(path_one, path_two)
