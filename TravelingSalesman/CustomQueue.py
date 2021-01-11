#Here we define a CustomQueue solely for the purpose of I needed something to be functionally available, but convertable to a length in some cases.
class CustomQueue:
    def __init__(self):
        self.list = []

    def push(self, object):
        self.list.append(object)

    #O(1), we always grab the first index
    def pop(self):
        temp = self.list[0]
        del self.list[0]
        return temp

    #Return the list representation of the queue
    def dump(self):
        return self.list

    #Gets the length/size of the list
    def length(self):
        return len(self.list)

    #Returns the length of the CustomQueue
    def __len__(self):
        return self.length()
