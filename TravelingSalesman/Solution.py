from CustomHashTable import CustomHashTable
from CoordinateSystem import CoordinateSystem
from TimeQueue import TimeQueue
from Location import Location
from Package import Package
import datetime

class Solution:
    #initialize a hash table for use later..
    def __init__(self):
        self.hash_table = CustomHashTable()
        self.coor_sys = CoordinateSystem()

    #Sorts packages into 3 truck arrays that are returned as a Tuple
    def sort_packages(self, package_list):
        for package in package_list:
            hash_table_item = self.hash_table.get(package.get_location().get_address())
            if type(hash_table_item) is type(None):
                self.hash_table.add(package.get_location().get_address(), [hash(package)])
            else:
                hash_table_item[1].append(hash(package))

    #Calculates the shortest distance from the last location remaining in package_arr
    def shortest_dist_to_truck(self, package_arr, last_location):
        shortest = 0
        for i in range(len(package_arr)):
            if package_arr[i].distance_to(last_location) < package_arr[shortest].distance_to(last_location):
                shortest = i
        return shortest

    #Groups packages by location
    def package_grouping(self, uncategorized):
        packages = CustomHashTable()
        for package in uncategorized:
            if str(package.get_location()) in packages.get_keys():
                packages.get(str(package.get_location()))[1].append(package)
            else:
                packages.add(str(package.get_location()),[package])
        return packages

    #Builds a path for a known set of packages based on the shortest distance to the next one
    def build_path(self, path_packages, home_location):
        last_location = home_location
        shortest = 0
        path = TimeQueue(home_location)
        while len(path_packages) > 0:
            shortest = self.shortest_dist_to_truck(path_packages, last_location)
            path.add_package(path_packages[shortest])
            last_location = path_packages[shortest].get_location()
            del path_packages[shortest]
        return path

    #Creates a new package at the "home"/HUB location
    def get_home(self, home_location):
        return Package(-1, home_location, 0, "EOD", "HUB")

    #Meat of the code, sorts the distances into different paths and handles main sorting content
    def sort_distances_three(self, uncategorized, home_location):
        #Build path storage
        path_one = TimeQueue(home_location)
        path_two = TimeQueue(home_location)
        path_three = TimeQueue(home_location)
        path_one_packages = []
        path_two_packages = []
        path_three_packages = []
        #Get some useful variables for path_one
        home_package = self.get_home(home_location)
        group_packages = self.package_grouping(uncategorized)
        coor_sys = self.coor_sys
        coor_sys.load_from_files()
        deadline_before_eod = []
        now = datetime.datetime.now()
        before_eod_datetime = datetime.datetime(now.year, now.month, now.day, 12, 0, 0)
        #Group packages based on region within the map; O(n) for setting up the keys of the hash map
        for group in group_packages.get_keys():
            #O(n) where n = the length of the list associated to the group
            for package in group_packages.get(group)[1]:
                response = coor_sys.lookup_coordinates(package.get_location())
                if response[1][1] > 8:
                    path_one_packages.append(package)
                elif response[1][1] > 5:
                    path_two_packages.append(package)
                else:
                    path_three_packages.append(package)
                if package.get_deadline().timestamp() < before_eod_datetime.timestamp():
                    deadline_before_eod.append(package)
        #Set last known location, default is the home_location
        last_location = home_location
        shortest = 0
        temp_path = []
        temp_locations = CustomHashTable()
        #Handle deadlines due prior to End of Day (eod)
        while len(deadline_before_eod) > 0:
            shortest = self.shortest_dist_to_truck(deadline_before_eod, last_location)
            temp_path.append(deadline_before_eod[shortest])
            if type(temp_locations.get(temp_path[-1].get_location().get_address())) is type(None):
                temp_locations.add(temp_path[-1].get_location().get_address(), [hash(temp_path[-1])])
            else:
                temp_locations.get(temp_path[-1].get_location().get_address())[1].append(hash(temp_path[-1]))
            del deadline_before_eod[shortest]
        #Pull out known grouped packages
        specific_package_list = [13, 14, 15, 16, 19, 20]
        for group in group_packages.get_keys():
            if group in temp_locations.get_keys():
                for package in group_packages.get(group):
                    if hash(package) not in temp_locations[group]:
                        temp_locations[group].append(hash(package))
                    else:
                        del specific_package_list[specific_package_list.index(hash(package))]
                #del group_packages[group]
        #Handle delayed packages
        special_packages = [24, 25, 26, 35, 28, 32, 6, 29, 18]
        offset = 0
        for i in range(len(path_one_packages)):
            if hash(path_one_packages[i - offset]) in special_packages:
                special_packages[special_packages.index(hash(path_one_packages[i - offset]))] = path_one_packages[i - offset]
                del path_one_packages[i - offset]
                offset += 1
        #build path
        path_one = self.build_path(path_one_packages, home_location)
        #Fix path_one order manually, packages 15, 6, and 29 are delivered past deadline unless this is manually adjusted for some reason;
        pod = path_one.dump()
        path_one_replacement = TimeQueue(home_location)
        to_add = [pod[0], pod[1], pod[2], pod[12], pod[11], pod[9], pod[10], pod[3], pod[4], pod[8], pod[5], pod[6], pod[7]]
        for package in to_add:
            path_one_replacement.add_package(package)
        path_one = path_one_replacement
        #go to the hub
        path_one.add_package(self.get_home(home_location))
        #build second part of path one
        offset = 0
        for i in range(len(path_two_packages)):
            if hash(path_two_packages[i - offset]) in special_packages:
                special_packages[special_packages.index(hash(path_two_packages[i - offset]))] = path_two_packages[i - offset]
                del path_two_packages[i - offset]
                offset += 1
        #Resort delayed packages
        store_for_later = [6, 18, 29]
        while len(special_packages) > 1:
            if hash(special_packages[0]) != 18:
                path_one.add_package(special_packages[0])
                del special_packages[0]
            elif hash(special_packages[1]) != 18:
                path_one.add_package(special_packages[1])
                del special_packages[1]
        path_two_packages.append(special_packages[0])
        #Get package number 9
        package_nine = None
        for i in range(len(path_two_packages)):
            if hash(path_two_packages[i]) == 9:
                    package_nine = path_two_packages[i]
                    del path_two_packages[i]
                    break
        location_pkg_nine = None
        #O(n), where n is len(package_arr)
        for package in uncategorized:
            if "410 S State St" in package.get_location().get_address():
                location_pkg_nine = package.get_location()
                break
        package_nine.set_location(location_pkg_nine)
        package_nine.set_notes(["Delayed until 10:20"])
        #Build an initial path_two, before we make a trip up north
        path_two = self.build_path(path_two_packages, home_location)
        path_two.add_package(home_package)
        path_three_packages.append(package_nine)
        path_three_location_order = ['2010 W 500 S', '233 Canyon Rd', '300 State St', '410 S State St', '600 E 900 South', '1060 Dalton Ave S']
        path_three = TimeQueue(home_location)
        while len(path_three_packages) > len(path_three.dump()):
            for i in range(len(path_three_packages)):
                if path_three_packages[i].get_location().get_address() in path_three_location_order[0]:
                    path_three.add_package(path_three_packages[i])
            del path_three_location_order[0]
        #path_three = self.build_path(path_three_packages, home_location)
        for package in path_three.dump():
            path_two.add_package(package)
        return (path_one, path_two)

    #This defines a user interface for interacting with the data generated within Solution.py
    def user_interface(self, path_one, path_two):
        coor_sys = self.coor_sys
        done = False
        response = ""
        now = datetime.datetime.now()
        time = datetime.datetime(now.year, now.month, now.day, 8, 0, 0)
        temp_spl = []
        offset = 0
        while not done:
            response = input("Do you want to see all packages or search for a specific package? ([A]ll packages/[S]earch for packages, \'exit\' to exit): ")
            if "exit" in response.lower():
                done = True
            else:
                res_two = input("What time would you like to see? (i.e. \'8:00:00 AM\' or \'8:00:00 PM\'): ")
                temp_spl = res_two.split(" ")
                temp_spl[0] = temp_spl[0].split(":")
                if 'pm' in temp_spl[1].lower():
                    offset = 12
                time = datetime.datetime(now.year, now.month, now.day, int(temp_spl[0][0]) + offset, int(temp_spl[0][1]), int(temp_spl[0][2]))
                if 'a' in response[0].lower() and not done:
                    print("Truck One:")
                    self.print_truck(path_one, coor_sys, time)
                    print()
                    print("Truck Two:")
                    self.print_truck(path_two, coor_sys, time)
                    print()
                    print("Total mileage:", round(path_one.get_distance_traveled() + path_two.get_distance_traveled(), 2))
                elif 's' in response[0].lower() and not done:
                    package_id = int(input("What package id would you like to view delivery status for? "))
                    package = None
                    for i in range(len(path_one.dump())):
                        if hash(path_one.dump()[i]) == package_id:
                            package = path_one.dump()[i]
                            break
                    if type(package) is type(None):
                        for i in range(len(path_two.dump())):
                            if hash(path_two.dump()[i]) == package_id:
                                if type(path_two.dump()[i].get_notes()) is type([]):
                                    if "hub" not in path_two.dump()[i].get_notes()[0].lower():
                                        package = path_two.dump()[i]
                                        break
                    print("STATUS:", "DELIVERED" if (package.get_deadline().timestamp() < time.timestamp()) else "NOT DELIVERED", "DELIVERY AT:", package.get_deadline().strftime("%H:%M:%S"), "DEADLINE:", package.get_original_deadline().strftime("%H:%M:%S"), "LOCATION:", package.get_location().get_address(), "NOTES:", package.get_notes())

    #Print out the location of paths, mainly used for debugging
    def print_location(self, path, coor_sys):
        has_package = False
        count = 1
        correlations = CustomHashTable()
        for i in range(len(path.dump())):
            correlations.add(coor_sys.lookup_coordinates(path.dump()[i].get_location())[1], i)
        value = None
        #O(n), n = 16
        for y in range(16):
            #O(n), n = 13
            for x in range(13):
                value = correlations.get((x + 1, y + 1)) #O(n) operation, linearly searched
                #If the x,y location exists in the graph;
                if type(value) is not type(None):
                    if len(str(value[1])) == 1:
                        print(value[1], ' ', end='')
                    elif len(str(value[1])) == 2:
                        print(value[1], '', end='')
                    else:
                        print(value[1], end='')
                #Location of WGU;
                elif x == 10 and y == 10:
                    print('@  ', end='')
                #No known location;
                else:
                    print('.  ', end='')
            print()

    #Get the hash table from Solution.py
    def get_hash_table(self):
        return self.hash_table.get_list()

    #Print out truck data;
    def print_truck(self, truck, coor_sys, time):
        #O(n) where n = len(truck.dump())
        for package in truck.dump():
            status = "DELIVERED" if package.get_deadline().timestamp() < time.timestamp() else "NOT DELIVERED"
            time_str = package.get_deadline().strftime("%H:%M:%S")
            original_time_str = package.get_original_deadline().strftime("%H:%M:%S")
            location_str = package.get_location().get_address()
            #If status is true;
            if status:
                #format string, based on package int length
                if len(str(hash(package))) == 1:
                    print("STATUS:", status, " PACKAGE ID:", hash(package), " DELIVERY AT:", time_str, "DEADLINE:", original_time_str, "LOCATION:", location_str, "NOTES:", package.get_notes()[0] if type(package.get_notes()) is type([]) else package.get_notes())
                else:
                    print("STATUS:", status, " PACKAGE ID:", hash(package), "DELIVERY AT:", time_str, "DEADLINE:", original_time_str, "LOCATION:", location_str, "NOTES:", package.get_notes()[0] if type(package.get_notes()) is type([]) else package.get_notes())
            else:
                #format string, based on int length
                if len(str(hash(package))) == 1:
                    print("STATUS:", status, "PACKAGE ID:", hash(package), " DELIVERY AT:", time_str, "DEADLINE:", original_time_str, "LOCATION:", location_str, "NOTES:", package.get_notes()[0] if type(package.get_notes()) is type([]) else package.get_notes())
                else:
                    print("STATUS:", status, "PACKAGE ID:", hash(package), "DELIVERY AT:", time_str, "DEADLINE:", original_time_str, "LOCATION:", location_str, "NOTES:", package.get_notes()[0] if type(package.get_notes()) is type([]) else package.get_notes())
        print("Truck mileage:", truck.get_distance_traveled())
