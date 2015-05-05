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
// int n_requests = 1000; //program arguments and defaults
// int w_threads = 15;
// int buff_size = 500;

// int joe_counter = 0; //request count
// int jane_counter = 0;
// int john_counter = 0;

//     vector<int> JoesGraph(100); //histogram
//     vector<int> JanesGraph(100);//100 spots for each possible number
//     vector<int> JohnGraph(100);


// BoundedBuffer* buffer; // basic bounded buffer to hold requests
// BoundedBuffer* joebuff; //statistics buffers
// BoundedBuffer* janebuff;//delay new operator untill complete program arguments
// BoundedBuffer* johnbuff;
// // thread id enumerations used in pthread_create
// int* joe = new int(0);
// int* jane = new int(1);
// int* john = new int(2);
/*--------------------------------------------------------------------------*/
/* FORWARDS */
/*--------------------------------------------------------------------------*/

    /* -- (none) -- */

/*--------------------------------------------------------------------------*/
/* MAIN FUNCTION */
/*--------------------------------------------------------------------------*/


void print_histgram(vector<int> data, string name){
    // vector<int> xval(10); //make the histogram have less bars ie 0-9 10-19 20-29 etc
    // for (int j=0; j<xval.size();++j){
    //     for (int k=0;k<xval.size();++k){
    //         xval[j]+=data[k+j*10];
    //     }
    // }
    // cout<<name<<endl
    // <<"0-9:"<<xval[0] <<endl
    // <<"10-19:"<<xval[1]<<endl
    // <<"20-29:"<<xval[2]<<endl
    // <<"30-39:"<<xval[3]<<endl
    // <<"40-49:"<<xval[4]<<endl
    // <<"50-59:"<<xval[5]<<endl
    // <<"60-69:"<<xval[6]<<endl
    // <<"70-79:"<<xval[7]<<endl
    // <<"80-89:"<<xval[8]<<endl
    // <<"90-99:"<<xval[9]<<endl;
}


void* request_thread(void* person_id) {
  // request thread logic can go in here pthread_create argument
  // int r_id = *((int*)person_id);

  // for( int i = 0 ; i < n_requests; ++i) {
  //   Request* r = new Request(r_id, "test");
  //   if(r_id == 0 ) {
  //     joe_counter++;
  //     r->recieved_data = "data Joe Smith";
  //     r->id = 0;
  //   }
  //   else if(r_id == 1) {
  //     jane_counter++;
  //     r->recieved_data = "data Jane Smith";
  //     r->id = 1;
  //   }
  //   else if(r_id ==2 ){
  //     john_counter++;
  //     r->recieved_data = "data John Smith";
  //     r->id = 2;
  //   }
  //   else{
  //       cout<< "error of some sort occured "<<endl;
  //   }
  //   buffer -> put(*r);
  //   delete r;
  // }



}
void* stat_thread(void* person_id){
 // int req_id = *((int*)person_id);

 //  Request r(-1,"dummy");

 //  for(int i=0; i<n_requests; i++) {
 //    if (req_id==0){
 //        r = joebuff->get();
 //        JoesGraph[atoi(r.recieved_data.c_str())]+=1; //increment corresponding integer of the histogram
 //    }
 //    else if (req_id ==1){
 //             r = janebuff->get();
 //             JanesGraph[atoi(r.recieved_data.c_str())]+=1;
 //    }
 //    else if (req_id==2){
 //                 r = johnbuff->get();
 //                 JohnGraph[atoi(r.recieved_data.c_str())]+=1;

 //      }
 //  }

}
void* thread_manager(void* req_channel) {
  //do some sort of checking used as an argument in pthread_create

    
  // RequestChannel * channel = (RequestChannel*) req_channel;
  // Request r(0,"");
  //   int counter = 0;
  // while(1) {
  
  //    r = buffer->get(); 
  // //   
    
  //   if(r.recieved_data == "kill") {
  //     break; // exit
  //   }
  
  //   string rply = channel -> send_request(r.recieved_data);
  //   r.recieved_data = rply;
  //   if (r.id == 0)
  //       joebuff->put(r);
  //   else if (r.id==1)
  //       janebuff->put(r);
      
  //   else if (r.id==2)
  //       johnbuff->put(r);
    
  // }
  // channel->send_request("quit");



}
//used to create the different thread requests
// void create_thread(pthread_t thread[], void *(*start_routine) (void *) ){
//   pthread_create(&thread[0], NULL, start_routine, (void*)joe);
//   pthread_create(&thread[1], NULL, start_routine, (void*)jane);
//   pthread_create(&thread[2], NULL, start_routine, (void*)john);
    
// } 
// used to simplify the joining of the threads
// the join thread function allows for the threads to wait on one another 
// void join_thread(pthread_t thread[], int numb) {
//   cout<<"Joining request_threads"<<endl;
//   for (int i = 0; i < numb;++i) 
//     pthread_join(thread[i],NULL);
// }

int main(int argc, char * argv[]) {
//     int c=0;
//      while((c = getopt (argc, argv, "n:w:b:"))!=-1 )
//     switch(c) {
//       case 'n':
//         n_requests=atoi(optarg);
//         break;
//       case 'w':
//         w_threads=atoi(optarg);
//         break;
//       case 'b':
//         buff_size=atoi(optarg);
//         break;
//     case '?':
//             cout<<"Sorry will now leave\n\n";
//             abort();
//       default:
//         cout<<"cmd line error\n";
//         cout<<"options -n=# -w=# -b=#\n\n";
//         abort();
//     }
  
//   pthread_t request_threads[3];
//   pthread_t worker_threads[w_threads];
//   pthread_t stat_threads[3];

 
//   buffer = new BoundedBuffer(buff_size);
//   joebuff = new BoundedBuffer(buff_size);
//   janebuff = new BoundedBuffer(buff_size);
//   johnbuff = new BoundedBuffer(buff_size);
// // start the dual processes 

//   int child = fork();
//   if (!child) 
//     execv("dataserver", argv);  // replace child process with "dataserver" call
  

//   cout << "CLIENT STARTED: " << endl;
//   cout << "Establishing control channel... " << flush;
//   RequestChannel chan("control", RequestChannel::CLIENT_SIDE);
//   cout << "done." << endl;



//   cout<<"Creating request threads\n";
//   create_thread(request_threads, request_thread );

//  cout << "Creating worker threads\n";

//   for(int i = 0; i < w_threads; ++i) {
//     string reply = chan.send_request("newthread");
//     RequestChannel* channel = new RequestChannel(reply,
//     RequestChannel::CLIENT_SIDE);
//     pthread_create(&worker_threads[i], NULL, thread_manager, channel);
//   }
//   cout<<"stat threads"<<endl;
//   create_thread(stat_threads, stat_thread);


//   join_thread(request_threads, 3);

//   Request k(-1,"kill"); 
//   cout<<"killing workers."<< endl;
//   for (int i=0; i < w_threads;++i)
//         buffer->put(k);

//     join_thread(worker_threads,w_threads);


//   join_thread(stat_threads,3);


// chan.send_request("quit");
// sleep(1);
// print_histgram(JoesGraph, "Joe Smith");
// print_histgram(JanesGraph, "Jane Smith");
// print_histgram(JohnGraph, "John Smith");
return 0;

}
