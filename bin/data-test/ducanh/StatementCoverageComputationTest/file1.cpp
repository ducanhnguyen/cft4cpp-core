#include <iostream>
#include <fstream>
#include <string>
#include <windows.h>
#include <conio.h>
#include <iomanip>

using namespace std;

int test(int a){
	return a--;
}
//------------------------
int test1(int a){
	int sum = 0;
	for (int i = 0; i < a; i++){
		sum++;
		sum += i;
	}
	return sum;
}
//------------------------
int test2(int a, int b){
	if (a!=b){
		a = a - b;
	}else{
		int x = test(a);
		if (a>0){
			a++;
		}
	}
	return a;
}

//------------------------
class People{
	private:
		int age;
		char* name;
	public:
		int getAge(){
			return age;
		}
		char* getName(){
			return name;
		}
};


int test3(People p){
	if (p.getAge() > 0) 
		return 0;
	else
		return 1;
}
int main(){
	return 0;
}

