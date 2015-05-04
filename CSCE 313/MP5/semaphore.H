/* 
    File: semaphore.H
    Author: R. Bettati
            Department of Computer Science
            Texas A&M University
    Date  : 08/02/11
*/

#ifndef _semaphore_H_                   // include file only once
#define _semaphore_H_

/*--------------------------------------------------------------------------*/
/* DEFINES */
/*--------------------------------------------------------------------------*/

/* -- (none) -- */

/*--------------------------------------------------------------------------*/
/* INCLUDES */
/*--------------------------------------------------------------------------*/

#include <pthread.h>

/*--------------------------------------------------------------------------*/
/* DATA STRUCTURES */ 
/*--------------------------------------------------------------------------*/

/* -- (none) -- */

/*--------------------------------------------------------------------------*/
/* FORWARDS */ 
/*--------------------------------------------------------------------------*/

/* -- (none) -- */

/*--------------------------------------------------------------------------*/
/* CLASS   S e m a p h o r e  */
/*--------------------------------------------------------------------------*/

class Semaphore {
private:
  /* -- INTERNAL DATA STRUCTURES
     You may need to change them to fit your implementation. */

  int value;
  pthread_mutex_t m;
  pthread_cond_t  c;

public:

  /* -- CONSTRUCTOR/DESTRUCTOR */
  Semaphore(){}
  Semaphore(int _val) {
    /*Do stuff in here */
    // returns zero if successful
    pthread_mutex_init(&m, NULL); // initialization of a mutex object 
    pthread_cond_init(&c,NULL);  // initializes the condition the mutex follows
    value = _val;
  }

  ~Semaphore() {
    /*NOTES: http://linux.die.net/man/3/ */
      pthread_mutex_destroy(&m); // uninitilizes the mutex object 
      pthread_cond_destroy(&c); //  uninitilizes the cond object


  }

  /* -- SEMAPHORE OPERATIONS */
  /*NOTE: had help from tanzir for this section, drew stuff on whiteboard*/
  int P() { // lock
    //error check
    int error = 0;
    if( error = pthread_mutex_lock(&m) != 0)
      return error; // makes use of the built in error codes 
    while(value <= 0 )
      if(error = pthread_cond_wait(&c,&m) != 0 ) // check the condition as well
        return error; 
    --value;
    if(error = pthread_mutex_unlock(&m) != 0 ) // must unlock to allow continuation of program
        return error;
    return 0;
  }
  int V(){ // unlock
    /*same process as the P() just need to increment */
    int error = 0;
    if( error = pthread_mutex_lock(&m) != 0)
      return error; // makes use of the built in error codes 
      ++value;
    if (errno=pthread_cond_broadcast(&c)!=0)
       return error;
    if(error = pthread_mutex_unlock(&m) != 0 ) // must unlock to allow continuation of program
        return error;
     return 0;
  }
 void set(int _val) {
    _val = value;
 }

};


#endif


