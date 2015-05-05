

#include <stdio.h>
#include <stdlib.h>
#include <cassert>
#include <cstring>
#include <string.h>
#include <sstream>
#include <iostream>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <netdb.h>
#include <errno.h>
#include <arpa/inet.h>
#include "networkrequest.H"

using namespace std;


//client
 networkrequestchannel::networkrequestchannel(const string _server_host_name, const unsigned short _port_no) {
	stringstream ss;
	ss << _port_no;
	string port = ss.str();

	const char* portN = port.c_str();
	const char* host = _server_host_name.c_str();

	struct sockaddr_in sockIn;
	memset(&sockIn, 0, sizeof(sockIn));
	sockIn.sin_family = AF_INET;

	if(struct servent * pse = getservbyname(portN, "tcp"))//make port
		sockIn.sin_port = pse->s_port;

	else if ((sockIn.sin_port = htons((unsigned short)atoi(portN))) == 0)
		cerr << "can't connect port\n";

	if(struct hostent * hn = gethostbyname(host))
		memcpy(&sockIn.sin_addr, hn->h_addr, hn->h_length);

	else if((sockIn.sin_addr.s_addr = inet_addr(host)) == INADDR_NONE)
		cerr << "can't determine host <" << host << ">\n";

	int s = socket(AF_INET, SOCK_STREAM, 0);
	if(s < 0)
        cerr << "can't create socket\n";

	if(connect(s, (struct sockaddr *)&sockIn, sizeof(sockIn)) < 0)
		cerr << "can't connect to " << host << ":" << portN;

	fd = s;

}

//server
networkrequestchannel::networkrequestchannel(const unsigned short _port_no, void * (*connection_handler) (void *) , int backlog) {
	// stringstream ss;
	// ss << _port_no;
	// string port = ss.str();
	// char * svc = port.c_str();
	
	// memset(&serverIn, 0, sizeof(serverIn));
	// serverIn.sin_family = AF_INET;
	// serverIn.sin_addr.s_addr = INADDR_ANY;

	// if(struct servent * pse = getservbyname(svc, "tcp"))
	// 	serverIn.sin_port = pse->s_port;

	// else if((serverIn.sin_port = htons((unsigned short)atoi(svc))) == 0)
	// 	cerr << "can't get port\n";


	// int socknum  = socket(AF_INET, SOCK_STREAM, 0);

	// if(socknum < 0)
	// 	cerr << "can't create socket \n";

	// if(bind(socknum, (struct sockaddr *)&serverIn, sizeof(serverIn)) < 0)
	// 	cerr << "can't bind...\n";

	// listen(socknum,backlog);//check for connections


	// int serverSize = sizeof(serverIn);

	// int master = socknum;

	// while(true) {
	// 	int * slave = new int;

	// 	pthread_t thread;
	// 	pthread_attr_t attr;
	// 	pthread_attr_init(&attr);


	// 	*slave = accept(master,(struct sockaddr*)&serverIn, (socklen_t*)&serverSize);

	// 	if(slave < 0) {
	// 		delete slave;

	// 		if(errno == EINTR)
 //                continue;//retry
	// 		else cerr << "unknown error in accept()\n";
	// 	}

	// 	pthread_create(&thread, &attr, connection_handler, (void*)slave);


	// }
	// cout << "Connection complete.\n";
}
//-----------------------------------------------------------------------------------------------------------------------------

networkrequestchannel::~networkrequestchannel(){
close(fd);

}
//-----------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------
const int MAX_MSG = 255;
string networkrequestchannel::send_request(string _request){
	cwrite(_request);
	string s = cread();
	return s;
}
//-----------------------------------------------------------------------------------------------------------------------------

string networkrequestchannel::cread(){

	char buf[MAX_MSG];

	if (read(fd, buf, MAX_MSG) < 0)
		cerr<<"Error reading\n";

	string s = buf;

	return s;
}
//-----------------------------------------------------------------------------------------------------------------------------

int networkrequestchannel::cwrite(string _msg){

	if (_msg.length() >= MAX_MSG) {
		cerr << "Message too long for Channel!\n";
		return -1;
	}

	const char * s = _msg.c_str();

	if (write(fd, s, strlen(s)+1) < 0)
		cerr<<"Error writing\n";


}
//-----------------------------------------------------------------------------------------------------------------------------

