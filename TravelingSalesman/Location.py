#Here we have ourselves a stored location variable
class Location:
    #Constructor; stores content as part of the object
    def __init__(self, address, city, state, zip, distances):
        self.address = address
        self.city = city
        self.state = state
        self.zip = zip
        self.distances = distances
        self.zero_distance = '0.0'

    def get_address(self):
        return self.address

    #Return a stored set of arrays in this class file; no/minimal time complexity
    def get_distances(self):
        return self.distances

    #Returns a known distance from the current location indicated by this class object
    #O(n * m) for each .index call since index searches linearly through the array of elements to determine where something is.. possibly the least efficient method of handling this
    def distance_to(self, other):
        if self.distances.index(self.zero_distance) > other.distances.index(self.zero_distance):
            return self.distances[other.distances.index(self.zero_distance)]
        elif self.distances.index(self.zero_distance) < other.distances.index(self.zero_distance):
            return other.distances[self.distances.index(self.zero_distance)]
        else:
            return self.zero_distance

    def __hash__(self):
        return self.distances.index(self.zero_distance) ^ 128

    def __str__(self):
        return self.address + ';' + self.city + ';' + self.state + ';'+ self.zip
