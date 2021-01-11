from Location import Location
import datetime

#Package object class
class Package:
    #Stores data related to a package
    def __init__(self, id, location, mass, deadline, notes):
        self.id = id
        self.location = location
        self.mass = mass
        if type(deadline) is str:
            now = datetime.datetime.now()
            if "EOD" in deadline:
                self.deadline = datetime.datetime(now.year, now.month, now.day, 20, 0, 0)
            elif ":" in deadline:
                dead_spl = deadline.split(" ")
                offset = 12 if "PM" in dead_spl[1] else 0
                time_spl = dead_spl[0].split(":")
                self.deadline = datetime.datetime(now.year, now.month, now.day, int(time_spl[0]) + offset, int(time_spl[1]), 0)
            else:
                self.deadline = deadline
        else:
            self.deadline = deadline
        self.original_deadline = self.deadline
        self.notes = notes
        self.is_delivered = False

    #Pulls the distance to another package or location, or else returns -1.0 if there's an error somewhere in the code
    def distance_to(self, other):
        if type(other) is Package:
            return self.location.distance_to(other.location)
        elif type(other) is Location:
            return self.location.distance_to(other)
        else:
            return -1.0

    #Returns the notes from the package data
    def get_notes(self):
        return self.notes

    #Sets notes for the package
    def set_notes(self, notes):
        self.notes = notes

    #Returns the deadline from the package data
    def get_deadline(self):
        return self.deadline

    #Returns the original deadline, assigned in the constructor
    def get_original_deadline(self):
        return self.original_deadline

    #Returns the weight of an object, defined in the constructor
    def get_mass(self):
        return self.mass

    #Returns a location object, defined in the constructor and set later with set_location(location)
    def get_location(self):
        return self.location

    #Sets the location, but verifies the type doesn't change since we need to guarantee that the location's type will stay as the Location object
    def set_location(self, location):
        if type(self.location) is type(location):
            self.location = location
        else:
            self.location = self.location

    #Returns the distances array from the location object associated with this object in memory
    def get_distances(self):
        return self.location.get_distances()

    #Sets a new deadline for when the package should be delivered by
    def set_deadline(self, deadline):
        self.deadline = deadline

    #Sets a delivered state to the package object
    def set_delivered(self, delivered):
        self.delivered = delivered

    #Gets a delivered boolean from the package object
    def get_delivered(self):
        return self.is_delivered

    #Defines the hash function to be the id % 41
    def __hash__(self):
        return int(self.id) % 41

    #Defines the string conversion for this object
    def __str__(self):
        return str(self.id) + ';' + str(self.location) + ';' + str(self.deadline) + ';' + str(self.original_deadline) + ';' + str(self.notes)
