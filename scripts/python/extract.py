file = open("delete.txt")
lines = file.readlines()
for i in range(1, len(lines), 2):
	f = lines[i].split(' ')
	print "'" + f[0] + "',"
	print "'" + f[1] + "',"