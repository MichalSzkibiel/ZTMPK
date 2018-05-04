import threading

stop_dict = {}

class UnderStop(threading.Thread):
    def __init__(self, lines):
        threading.Thread.__init__(self)
        self.lines = lines
    def run(self):
        split_arr = self.lines[0].strip().split()
        self.id = split_arr[0][-2:]
        try:
            self.lat = float(split_arr[-3])
            self.lon = float(split_arr[-1])
        except:
            print ("Nie ma pozycji")
            self.lat = None
            self.lon = None
        lines_on_stop = [[],[],[],[],[]]
        idx = 0
        start = 0
        for line in self.lines[1:]:
            if line.find("sta") != -1:
                idx = 0
                start = 4
            elif line.find("wsiadaj") != -1:
                idx = 1
                start = 5
            elif line.find("wysiadaj") != -1:
                idx = 2
                start = 5
            elif line.find("kra") != -1:
                idx = 3
                start = 4
            elif line.find("danie") != -1:
                idx = 4
                start = 5
            to_check = line.split()[start:]
            for el in to_check:
                if el.find("^") == -1:
                    lines_on_stop[idx].append(el)
        self.object = {"lat":self.lat, "lon":self.lon, "constant":lines_on_stop[0], "sit_in":lines_on_stop[1], "sit_out":lines_on_stop[2], "end":lines_on_stop[3], "request":lines_on_stop[4]}

class SuperStop(threading.Thread):
    def __init__(self, lines):
        threading.Thread.__init__(self)
        self.lines = lines
    def run(self):
        split_arr = self.lines[0].split("  ")
        print(str(split_arr))
        id = split_arr[1].strip()
        name = split_arr[2].strip()
        borough = split_arr[-1].strip()
        threads = []
        i = 3
        begStop = 2
        while i < len(self.lines):
            if (self.lines[i].strip()[0] != "L"):
                thread = UnderStop(self.lines[begStop:i])
                thread.start()
                threads.append(thread)
                begStop = i
            i = i + 1
        thread = UnderStop(self.lines[begStop:i])
        thread.start()
        threads.append(thread)
        self.object = {"name":name, "borough":borough, "under_stops":{}}

        sum_lat = 0.0
        sum_lon = 0.0
        sum_num = 0

        for t in threads:
            t.join()
        for t in threads:
            self.object["under_stops"][t.id] = t.object
            try:
                sum_lat = sum_lat + t.lat
                sum_lon = sum_lon + t.lon
                sum_num = sum_num + 1
            except:
                pass
        try:
            lat = sum_lat/float(sum_num)
            lon = sum_lon/float(sum_num)
        except:
            lat = None
            lon = None
        self.object["lat"] = lat
        self.object["lon"] = lon
        stop_dict[id] = self.object
            
        

file_in = open("RA180512.txt", "r", encoding = "UTF-8")
lines = file_in.readlines()

threads = []

begStop = 0

for i in range(len(lines[3000:])):
    if lines[i].find("#ZP") != -1:
        break
    elif lines[i].find("*ZP") != -1:
        begStop = i+1
    elif lines[i].find("#PR") != -1:
        thread = SuperStop(lines[begStop:i])
        thread.start()
        threads.append(thread)
        begStop = i + 1

for t in threads:
    t.join()
file_out = open("stop_dict.txt", "w")
file_out.write(str(stop_dict))
