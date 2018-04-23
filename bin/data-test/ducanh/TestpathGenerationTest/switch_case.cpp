#include <iostream>
using namespace std;

void switch_case1(char grade){
   switch(grade){
		case 'A' :
		  cout << "Excellent!" << endl; 
		  break;
   }
   cout << "Your grade is " << grade << endl;
}

void switch_case2(char grade){
   switch(grade){
		case 'A' :
		  cout << "Excellent!" << endl; 
		  break;
		default :
		  cout << "Invalid grade" << endl;
   }
   cout << "Your grade is " << grade << endl;
}

void switch_case3(char grade){
   switch(grade){
		case 'A' :
		  cout << "Excellent!" << endl; 
		  break;
		case 'B' :
		case 'C' :
		  cout << "Well done" << endl;
		  return;
		case 'D' :
		  cout << "You passed" << endl;
		  break;
		default :
		  cout << "Invalid grade" << endl;
   }
   cout << "Your grade is " << grade << endl;
}
