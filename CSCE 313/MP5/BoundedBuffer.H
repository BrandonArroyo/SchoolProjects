/* 
    File: BoundedBuffer.H

    Author: Brandon Arroyo
            Department of Computer Science
            Texas A&M University
    Date  : 04/16/15

*/
#ifndef _BoundedBuffer_H_
#define _BoundedBuffer_H_

#include <pthread.h>
#include <queue>
#include <string>
#include "semaphore.H"

using namespace std;

class Request{
	public:   
		int id;
		string recieved_data;
	
		Request(int d, string n){
			id = d;
			recieved_data = n;
			
		}

};

class BoundedBuffer {
	/*http://en.wikipedia.org/wiki/Producer%E2%80%93consumer_problem*/

	private:
		int size;
		Semaphore* full; // # items in the buffer already
		Semaphore* empty; // # avaliable items in buffer to be filled
		Semaphore* mutex; // helps with multiple requests which is what we want
   		queue<Request> que;
    	
	public:
		BoundedBuffer(int _size) {
			size = _size;
        	mutex = new Semaphore(1);
        	full = new Semaphore(0);
        	empty = new Semaphore(_size);

		}
		~BoundedBuffer(){
			delete full;
			delete empty;
			delete mutex;
		}

		Request get(){
			

			full->P();
			mutex->P();
			Request temp = que.front();
			que.pop();
			mutex->V();
			empty->V();

			return temp;
		}
		void put(Request temp){

			empty->P();
			mutex->P();
			que.push(temp);
			mutex->V();
			full->V();
			

		}

};



#endif