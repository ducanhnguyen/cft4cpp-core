/*
This file contains atomic functions relating to switch case
*/
#include <iostream>
#include <stdlib.h> 
using namespace std;
void switchCase1(char grade);
/*
Throwing exception fire error window that needed to be hidden actually.
*/
double switchCase0(int a, int b){
   if(b == 0 ){
      throw "Division by zero condition!";
   }else{
   		switchCase1('A');
   		if (b==1){}   			
   	}
   return (a/b);
}
/*
The case does not contain default statement
*/
void switchCase1(char grade){
   switch(grade){
		case 'A' :
		  cout << "Excellent!" << endl;  
		  break;
   }
   cout << "Your grade is " << grade << endl;
}
/*
The function contains continous cases in a switch-case
*/
void switchCase2(char grade){
   switch(grade){
		case 'A' :
		case 'F':
		case 'U':{
		  cout << "Excellent!" << endl; 
		  break;
		}
		case 'B' :
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

/*
The function contains multiple switch-cases
*/
void switchCase3(char grade){
   switch(grade){
		case 'A' :
		case 'B' :
		  cout << "Well done" << endl;
		  return;
		default :
		  cout << "Invalid grade" << endl;
   }
   	
   switch(grade){
		case 'A' :
		case 'F':
		case 'U':{
		  cout << "Excellent!" << endl; 
		  break;
		}
		case 'B' :
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

/*
The function contains switch-case in a switch-case
*/
void switchCase4(char grade, int x){
   switch(grade){
		case 'A' :
		case 'B' :{
			/*Inner switch-case*/
			switch (x){
				case 1: 
					x++;
					break;
				case -1: 
					x--;
					break;
			}
			break;
		}
		default :
		  cout << "Invalid grade" << endl;
   }
}


