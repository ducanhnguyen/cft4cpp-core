#include <iostream>
#include <fstream>
#include <string>
#include <windows.h>
#include <conio.h>
#include <iomanip>
using namespace std;

int simpleTest0(int x){
     switch (x){
		case 1: 
			int sum=0;
			for (int i = 0; i< 10; i++) 
				sum+=i;
		break;
     }
}

int simpleTest1(int a){
	return a--;
}

int simpleTest2(int a, int b){
	if (a!=b){
		a = a - b;
	}else{
		int x = simpleTest1(a);
		if (a>0){
			a++;
		}
	}
	return a;
}

int simpleTest3(int a, int b){
	if (a==10&&b==10)
		return 0;
	else
		return 1;
}
int simpleTest4(int a, int b){
	if (a==10||b==10)
		return 0;
	else
		return 1;
}
int simpleTest5(int a, int b){
	if (!(a==10||b==10))
		return 0;
	else
		return 1;
}
int simpleTest6(int a, int b, int c){
	if (a==10||b==10||c==10)
		return 0;
	else
		return 1;
}

int simpleTest7(int a, int b, int c){
	if (!(!(a==10)||b==10||c==10))
		return 0;
	else
		return 1;
}
int simpleTest8(int a, int b, int c){
	if (!(!a==10||b==10||c==10))
		return 0;
	else
		return 1;
}
