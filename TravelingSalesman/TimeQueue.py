#Import some of the other custom objects we're using in this project
from CustomQueue import CustomQueue
from TimeTracker import TimeTracker

#Create class TimeQueue, dedicated to calculating a time delta when a package is added to the queue
class TimeQueue:
    #Constructor for the class, storing a starting location and creating some class-level variables
    def __init__(self, starting_location):
        self.a_queue = CustomQueue()
        self.time_tracker = TimeTracker()
        self.last_location = starting_location

    #Add a package to the queue, increment the time with the TimeTracker class, set the new deadline, and set the last location so we know where we're headed from.
    def add_package(self, package):
        self.a_queue.push(package)
        self.time_tracker.add_travel(package.distance_to(self.last_location))
        package.set_deadline(self.time_tracker.get_time())
        self.last_location = package.location

    #Remove a package from the queue, but not the timeline since that's a totally separate process
    def remove_package(self, package):
        for i in range(self.a_queue.length()): #Should iterate n times over the queue, O(n)
            temp = self.a_queue.pop()
            if hash(package) != hash(temp):
                self.a_queue.push(temp) #This should maintain the order

    #Change the delivery times of all packages in the queue;
    def set_time(self, time, home_location):
        self.time_tracker.set_time(time)
        if len(self.a_queue) > 0:
            last_location = home_location
            for i in range(len(self.a_queue)):
                self.time_tracker.add_travel(self.a_queue.dump()[i].distance_to(last_location))
                self.a_queue.dump()[i].set_deadline(self.time_tracker.get_time())

    def get_time(self):
        return self.time_tracker.get_time()
    #Get the total distance traveled by the truck
    def get_distance_traveled(self):
        return self.time_tracker.get_distance_traveled()

    #Dump the queue's array for printing out to console mainly;
    def dump(self):
        return self.a_queue.dump()
