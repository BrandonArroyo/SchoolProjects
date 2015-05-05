
all: dataserver simpleclient


networkrequest.o: networkrequest.H networkrequest.C
	g++ -std=c++11 -c -g networkrequest.C

dataserver: dataserver.C networkrequest.o 
	g++ -std=c++11 -g -o dataserver dataserver.C networkrequest.o -lpthread
	
simpleclient: simpleclient.C networkrequest.o
	g++ -std=c++11 -g -o simpleclient simpleclient.C networkrequest.o -lpthread
	
clean:
	rm *.o dataserver simpleclient