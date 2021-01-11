class LinearRegression:
    def __init__(self, arr):
        self.arr = arr

    def slope(self):
        sum = 0
        for i in range(len(self.arr)-1):
            sum += self.arr[i+1] - self.arr[i]
        sum /= (len(self.arr) - 1)
        return sum

    def intercept(self):
        return self.arr[0]
