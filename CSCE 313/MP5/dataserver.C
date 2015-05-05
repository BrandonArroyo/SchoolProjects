/* 
    File: dataserver.C

    Author: R. Bettati
            Department of Computer Science
            Texas A&M University
    Date  : 2012/07/16

    Dataserver main program for MP3 in CSCE 313
*/

/*--------------------------------------------------------------------------*/
/* DEFINES */
/*--------------------------------------------------------------------------*/

    /* -- (none) -- */

/*--------------------------------------------------------------------------*/
/* INCLUDES */
/*--------------------------------------------------------------------------*/

#include <cassert>
#include <cstring>
#include <sstream>
#include <iostream>
#include <sys/types.h>
#include <sys/stat.h>

#include <pthread.h>
#include <errno.h>
#include <unistd.h>
#include <stdlib.h>

#include "networkrequest.H"

using namespace std;

int MAX_MSG = 255;
/*--------------------------------------------------------------------------*/
/* DATA STRUCTURES */ 
/*--------------------------------------------------------------------------*/

    /* -- (none) -- */

/*--------------------------------------------------------------------------*/
/* CONSTANTS */
/*--------------------------------------------------------------------------*/

    /* -- (none) -- */

/*--------------------------------------------------------------------------*/
/* VARIABLES */
/*--------------------------------------------------------------------------*/

static int nthreads = 0;

/*--------------------------------------------------------------------------*/
/* FORWARDS */
/*--------------------------------------------------------------------------*/


/*--------------------------------------------------------------------------*/
/* LOCAL FUNCTIONS -- SUPPORT FUNCTIONS */
/*--------------------------------------------------------------------------*/

string int2string(int number) {
   stringstream ss;//create a stringstream
   ss << number;//add number to the stream
   return ss.str();//return a string with the contents of the stream
}

/*--------------------------------------------------------------------------*/
/* LOCAL FUNCTIONS -- THREAD FUNCTIONS */
/*--------------------------------------------------------------------------*/



/*--------------------------------------------------------------------------*/
/* LOCAL FUNCTIONS -- INDIVIDUAL REQUESTS */
/*--------------------------------------------------------------------------*/

int cwrite(int fd, string _msg) {

  if (_msg.length() >= MAX_MSG) {
    cerr << "Message too long for Channel!\n";
    return -1;
  }

  const char * s = _msg.c_str();

  if (write(fd, s, strlen(s)+1) < 0) 
    cerr << "Error writing \n";       // changed the cerr statement
  return -1;
}

string cread(int fd) {   // changed the cerr statement

  char buf[MAX_MSG];

  if (read(fd, buf, MAX_MSG) < 0) 
    cerr << "Error reading \n";

  string s = buf;


  return s;
  

}
void process_hello(int * fd, const string & _request) {

   cwrite(*fd,"hello to you too");
}



void process_data(int* fd, const string &  _request) {
  usleep(1000 + (rand() % 5000));
  cwrite(*fd, int2string(rand() % 100));
}


/*--------------------------------------------------------------------------*/
/* LOCAL FUNCTIONS -- THE PROCESS REQUEST LOOP */
/*--------------------------------------------------------------------------*/
void process_request(int * fd, const string & _request){

  if (_request.compare(0, 5, "hello") == 0)
    process_hello(fd, _request);

  else if (_request.compare(0, 4, "data") == 0)
    process_data(fd, _request);


}


void* connection_handler(void* arg) {

  int* fd = (int*)arg;
  for(;;) {

//    cout << "Reading next request from channel (" << _channel.name() << ") ..." << flush;
    string request = cread(*fd);
//    cout << " done (" << _channel.name() << ")." << endl;
//    cout << "New request is " << request << endl;

    if (request.compare("quit") == 0) {
      cwrite(*fd,"bye");
      usleep(10000);          // give the other end a bit of time.
//    cout << "channel deleted\n";
      break;                  // break out of the loop;
    }

    process_request(fd, request);
  }
  
}

/*--------------------------------------------------------------------------*/
/* MAIN FUNCTION */
/*--------------------------------------------------------------------------*/

int main(int argc, char * argv[]) {

  int backLog = 100;
  unsigned short portN= 12250;
  unsigned short pN = 0;
  int bLog = 0;

    int c = 0;
  while((c = getopt(argc, argv, "p:b:")) != -1){
    switch(c){
      case 'p':
        pN = atoi(optarg);
        break;
      case 'b':
        bLog = atoi(optarg);
        break;
    }
  }

  if(pN != 0)
     portN=pN;

  if(bLog != 0)
    backLog = bLog;


  cout << "SERVER STARTED: " << portN << endl;

  networkrequestchannel server(portN, connection_handler, backLog);

  server.~networkrequestchannel();

return 0;
}

