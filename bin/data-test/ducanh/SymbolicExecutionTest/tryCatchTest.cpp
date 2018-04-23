/*
This file contains atomic functions relating to try-catch
*/
#include <iostream>
#include <exception>
using namespace std;
/*
The function only contains one catch statement.
*/
struct DivisionByZero : public exception
{
  const char * what () const throw ()
  {
    return "Division by zero exception";
  }
};

int tryCatch0(int a, int b){
	float x = 0;
	try{
		if (b==0)
			throw DivisionByZero();
		x = a/b;
	}catch (exception& e){
		cout << "Standard exception: " << e.what() << endl;
		x = -1;
	}
	return x;
}

/*
The function contains several catch statements, except for the default statement
*/
int tryCatch1(int a, int b){
	try{
		if (a>0)
			throw 1;
		else if (a<-4)
			throw 'a';
		b = a;
	}catch(int param){
		b = -1;
	}catch(char param){
		b = -2;
	}
	int result = a * b;
	return result;
}

/*
The function contains several catch statements including the default statement
*/
int tryCatch2(int a, int b){
	try{
		if (a>0)
			throw 1;
		else if (a<-4)
			throw 'a';
		b = a;
	}catch(int param){
		b = -1;
	}catch(char param){
		b = -2;
	}catch(...){
		// default catch
		b = -3;
	}
	int result = a * b;
	return result;
}

/*
In below function, exception is thrown in the called function. 
In this case, the exception will be fired to its callee function
*/
double throw0(int a, int b);

int tryCatch3(int a, int b){
	int result = 0;
	
	try{
		result = throw0(a, b);
		int a = 0; 
	}catch (...){	
		cout << "default exception" << endl; 
	}
	return result;
}

double throw0(int a, int b){
   if(b == 0 ){
     	throw "Division by zero condition!";
   }else{		
   		return (a/b);
	}
}

/*
Try-catch block contains another try-catch blocks
*/
int tryCatch4(int a, int b){
	int ERROR = -9999;
	int result = 0;
	try{
		if (a>0){
			try{
				if (b<=0)
					throw "All parameters must be positive!";
			}catch(const char* param){
				cout << param << endl;
				result = ERROR;
			}
		}else 
			result = a * b + 1;		
	}catch(int param){
		result = ERROR;
	}catch(char param){
		result = ERROR;
	}
	return result;
}
/*
Continuous try-catch blocks
*/
void tryCatch5(int a, int b){
	// try-catch block 1
	try{
		a--;
		b--;
	}catch (...){	
		cout << "default exception" << endl; 
	}
	// try-catch block 1
	try{
		a++;
		b++;
	}catch (...){	
		cout << "default exception" << endl; 
	}
	
	cout << "Done";
}
