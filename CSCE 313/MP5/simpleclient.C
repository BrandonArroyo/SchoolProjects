/* 
    File: simpleclient.C

    Author: Brandon Arroyo
            Department of Computer Science
            Texas A&M University
    Date  : 2013/01/31

    Simple client main program for MP3 in CSCE 313
*/

/*--------------------------------------------------------------------------*/
/* DEFINES */
/*--------------------------------------------------------------------------*/

    /* -- (none) -- */

/*--------------------------------------------------------------------------*/
/* INCLUDES */
/*--------------------------------------------------------------------------*/
#include <stdlib.h> //system() call

#include <cassert>
#include <cstring>
#include <iostream>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/time.h>

#include <errno.h>
#include <unistd.h>


#include "networkrequest.H"
#include "BoundedBuffer.H"
using namespace std;

/*--------------------------------------------------------------------------*/
/* DATA STRUCTURES */ 
/*--------------------------------------------------------------------------*/

    /* -- (none) -- */

/*--------------------------------------------------------------------------*/
/* CONSTANTS */
/*--------------------------------------------------------------------------*/

    /* -- (none) -- */
int n_requests = 10000; //program arguments and defaults
int w_threads = 30;
int buff_size = 500;
string HostName="localHost";
int port=12250;

int joe_counter = 0; //request count
int jane_counter = 0;
int john_counter = 0;

    vector<int> JoesGraph(100); //histogram
    vector<int> JanesGraph(100);//100 spots for each possible number
    vector<int> JohnGraph(100);


BoundedBuffer* buffer; // basic bounded buffer to hold requests
BoundedBuffer* joebuff; //statistics buffers
BoundedBuffer* janebuff;//delay new operator untill complete program arguments
BoundedBuffer* johnbuff;
// thread id enumerations used in pthread_create
int* joe = new int(0);
int* jane = new int(1);
int* john = new int(2);

vector<networkrequestchannel*> NetworkChannels;
int* person_id;
/*--------------------------------------------------------------------------*/
/* FORWARDS */
/*--------------------------------------------------------------------------*/

    /* -- (none) -- */

/*--------------------------------------------------------------------------*/
/* MAIN FUNCTION */
/*--------------------------------------------------------------------------*/


void print_histgram(vector<int> data, string name){
    vector<int> xval(10); //make the histogram have less bars ie 0-9 10-19 20-29 etc
    for (int j=0; j<xval.size();++j){
        for (int k=0;k<xval.size();++k){
            xval[j]+=data[k+j*10];
        }
    }
    cout<<name<<endl
    <<"0-9:"<<xval[0] <<endl
    <<"10-19:"<<xval[1]<<endl
    <<"20-29:"<<xval[2]<<endl
    <<"30-39:"<<xval[3]<<endl
    <<"40-49:"<<xval[4]<<endl
    <<"50-59:"<<xval[5]<<endl
    <<"60-69:"<<xval[6]<<endl
    <<"70-79:"<<xval[7]<<endl
    <<"80-89:"<<xval[8]<<endl
    <<"90-99:"<<xval[9]<<endl;
}


void* request_thread(void* person_id) {
  // request thread logic can go in here pthread_create argument
  int r_id = *((int*)person_id);

  for( int i = 0 ; i < n_requests; ++i) {
    Request* r = new Request(r_id, "test");
    if(r_id == 0 ) {
      joe_counter++;
      r->recieved_data = "data Joe Smith";
      r->id = 0;
    }
    else if(r_id == 1) {
      jane_counter++;
      r->recieved_data = "data Jane Smith";
      r->id = 1;
    }
    else if(r_id ==2 ){
      john_counter++;
      r->recieved_data = "data John Smith";
      r->id = 2;
    }
    else{
        cout<< "error of some sort occured "<<endl;
    }
    buffer -> put(*r);
    delete r;
  }



}
void* stat_thread(void* person_id){
 int req_id = *((int*)person_id);

  Request r(-1,"dummy");

  for(int i=0; i<n_requests; i++) {
    if (req_id==0){
        r = joebuff->get();
        JoesGraph[atoi(r.recieved_data.c_str())]+=1; //increment corresponding integer of the histogram
    }
    else if (req_id ==1){
             r = janebuff->get();
             JanesGraph[atoi(r.recieved_data.c_str())]+=1;
    }
    else if (req_id==2){
                 r = johnbuff->get();
                 JohnGraph[atoi(r.recieved_data.c_str())]+=1;

      }
  }

}



void* event_thread(void* c){


    person_id=new int[w_threads];

    fd_set readset; //read file descriptors
    int max = 0;
    int select_result;

    Request r= Request(0,"");
    bool finished = false;
    int wcount = 0;
    int rcount = 0;
    struct timeval te = {0,10};


    for(int i=0; i<w_threads; i++){
        networkrequestchannel* channel = new networkrequestchannel(HostName, port);
        NetworkChannels.push_back(channel);
        person_id[i] = -1;//initialize to a - number so that it can not be mistaken for a real request
    }


    // // fill all the channels with requests before trying to read from them
    for(int i=0; i<w_threads; i++){
        r = buffer->get();
        wcount++;
        NetworkChannels[i]->cwrite(r.recieved_data);
        person_id[i] = r.id;
    }



    while(!finished){
        FD_ZERO(&readset);
        for(int i=0; i<w_threads; i++){
            if(NetworkChannels[i]->read_fd() > max)
            {
                max = NetworkChannels[i]->read_fd();
            }
            FD_SET(NetworkChannels[i]->read_fd(), &readset);
        }

        select_result = select(max+1, &readset, NULL, NULL, &te);

        if(select_result){
            for(int i=0; i<w_threads; i++){

                if(FD_ISSET(NetworkChannels[i]->read_fd(), &readset)){
                    // cout << "reading\n";
                    string serv_resp = NetworkChannels[i]->cread();
                    rcount++;
                    switch(person_id[i]){
                    case 0:
                        joebuff->put(Request(0,serv_resp));
                        break;
                    case 1:
                        janebuff->put(Request(1,serv_resp));
                        break;
                    case 2:
                        johnbuff->put(Request(2,serv_resp));
                        break;
                    }

                    if(wcount < n_requests*3)//dont write more than is available might cause deadlock
                    {
                        r = buffer->get();
                        wcount++;
                        // cout << "writing\n";
                        NetworkChannels[i]->cwrite(r.recieved_data);
                        person_id[i] = r.id;
                    }
                }
            }
        }


        if(rcount == n_requests*3) //if all reads complete break
        {
            break;
        }

    }

    // close request channels
    for(int i=0; i<w_threads; i++)
    {
        NetworkChannels[i]->cwrite("quit");
    }
}












//used to create the different thread requests
void create_thread(pthread_t thread[], void *(*start_routine) (void *) ){
  pthread_create(&thread[0], NULL, start_routine, (void*)joe);
  pthread_create(&thread[1], NULL, start_routine, (void*)jane);
  pthread_create(&thread[2], NULL, start_routine, (void*)john);
    
} 
//used to simplify the joining of the threads
//the join thread function allows for the threads to wait on one another 
void join_thread(pthread_t thread[], int numb) {
  cout<<"Joining request_threads"<<endl;
  for (int i = 0; i < numb;++i) 
    pthread_join(thread[i],NULL);
}


int main(int argc, char * argv[]) {
  string temp;
    int c=0;
    while((c = getopt (argc, argv, "n:w:b:"))!=-1 )
        switch(c)
        {
        case 'n':
            n_requests=atoi(optarg);
            break;
        case 'w':
            w_threads=atoi(optarg);
            break;
        case 'b':
            buff_size=atoi(optarg);
            break;
        case 'p':
            port = atoi(optarg);
            break;
        case 'h':
            temp = optarg;
            if(temp != "")
                HostName = temp;
            break;
        default:
            cout<<"cmd line error\n";
            cout<<"options -n=# -w=# -b=#\n\n";
            abort();
        }
  
  pthread_t request_threads[3];
  pthread_t event_handler;
  pthread_t stat_threads[3];

 
  buffer = new BoundedBuffer(buff_size);
  joebuff = new BoundedBuffer(buff_size);
  janebuff = new BoundedBuffer(buff_size);
  johnbuff = new BoundedBuffer(buff_size);



  cout << "CLIENT STARTED: " << endl;


  cout<<"Creating request threads\n";
  create_thread(request_threads, request_thread );

  cout << "Creating event handler\n";
  pthread_create(&event_handler, NULL, event_thread, NULL);

  cout<<"stat threads"<<endl;
  create_thread(stat_threads, stat_thread);

  cout <<"joining threads"<<endl;
  join_thread(request_threads,3);
  pthread_join(event_handler,NULL);
  join_thread(stat_threads,3);

 
sleep(1);
print_histgram(JoesGraph, "Joe Smith");
print_histgram(JanesGraph, "Jane Smith");
print_histgram(JohnGraph, "John Smith");
for (int i=0;i<NetworkChannels.size();++i)
  delete NetworkChannels[i];
return 0;

}
