#include <iostream>
#include <exception>
using namespace std;
double try_catch1(int a, int b){
   if( b == 0 ){
      throw "Division by zero condition!";
   }
   return (a/b);
}

//-------------------------
struct MyException : public exception{
	const char * what () const throw ()
	{
	return "C++ Exception";
	}
};
 
void try_catch2(){
	try{
		throw MyException();
	}catch(MyException& e){
		std::cout << "MyException caught" << std::endl;
		std::cout << e.what() << std::endl;
	}catch(std::exception& e){
		//Other errors
	}
}
