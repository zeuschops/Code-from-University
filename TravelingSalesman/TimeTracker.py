#Import critical data structure element for time, I'd rather not reinvent the wheel for this at least;
import datetime

#Class TimeTracker; tracks time based on travel distances given an average speed
class TimeTracker:
    #Define some class-level variables and a current time to work off of
    def __init__(self):
        current_time = datetime.datetime.now()
        self.starting_datetime = datetime.datetime(current_time.year, current_time.month, current_time.day, 8, 0, 0)
        self.distance_traveled = 0

    #Add travel time based on distance in miles
    def add_travel(self, distance_in_miles):
        avg_speed = float(18)
        add_minute = (1/(avg_speed/60)) * float(distance_in_miles)
        new_time_in_seconds = (self.starting_datetime.hour * 3600) + (self.starting_datetime.minute * 60) + self.starting_datetime.second + round(add_minute * 60)
        hour = int(new_time_in_seconds / 3600)
        minute = int(new_time_in_seconds / 60) - (hour * 60)
        second = int(new_time_in_seconds) - (minute * 60) - (hour * 3600)
        self.starting_datetime = datetime.datetime(self.starting_datetime.year, self.starting_datetime.month, self.starting_datetime.day, hour, minute, second)
        self.distance_traveled += float(distance_in_miles)

    #Get the current time according to TimeTracker, which should be the same time as when the last distance was added
    def get_time(self):
        return self.starting_datetime

    #Set the current time, useful for the third truck in this scenario
    def set_time(self, time):
        self.starting_datetime = time

    #Get the total distance traveled
    def get_distance_traveled(self):
        return round(self.distance_traveled, 2)
