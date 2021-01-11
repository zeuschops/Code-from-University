##Create a custom hash table object;
class CustomHashTable:
    #Takes no arguments during construction
    def __init__(self):
        self.hash_list = []

    #We can add data to the HashTable using a key-value pairing
    def add(self, key, value):
        self.hash_list.append((key, value))

    #We can get data from the HashTable; O(n) where n = the length of the hash_list
    def get(self, key):
        location = -1
        for i in range(len(self.hash_list)):
            if str(self.hash_list[i][0]) in str(key):
                location = i
        if location < 0:
            return None
        else:
            return self.hash_list[location]

    #Return raw list
    def get_list(self):
        return self.hash_list

    #Returns a string representation of the list used for hashing
    def __str__(self):
        return str(self.hash_list)

    #Gets only the keys from hash_list
    def get_keys(self):
        keys = []
        for part in self.hash_list:
            keys.append(part[0])
        return keys
