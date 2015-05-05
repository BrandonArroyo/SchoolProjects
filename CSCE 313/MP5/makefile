# # makefile

# all: dataserver simpleclient

# reqchannel.o: reqchannel.H reqchannel.C
# 	g++ -c -g reqchannel.C

# dataserver: dataserver.C reqchannel.o 
# 	g++ -g -o dataserver dataserver.C reqchannel.o -lpthread

# simpleclient: simpleclient.C reqchannel.o
# 	g++ -g -o simpleclient simpleclient.C reqchannel.o -lpthread

# makefile

all: dataserver simpleclient


networkrequest.o: networkrequest.H networkrequest.C
	g++ -std=c++11 -c -g networkrequest.C

dataserver: dataserver.C networkrequest.o 
	g++ -std=c++11 -g -o dataserver dataserver.C networkrequest.o -lpthread
	
simpleclient: simpleclient.C networkrequest.o
	g++ -std=c++11 -g -o simpleclient simpleclient.C networkrequest.o -lpthread
	
clean:
	rm *.o dataserver simpleclient