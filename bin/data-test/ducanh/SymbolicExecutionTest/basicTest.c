/*
This file contains atomic functions
*/
#include <iostream>
using namespace std;

int basicTest1(int a, 
	int b){
	if (a>0)
		return a;
	return b;
}

int basicTest2(int a, int b){
	a = a - b;
	if (a>0)
		return a;
	return b;
}

int basicTest3(int a, int b){
	a = a - b;
	if (a>b)
		return a;
	return b;
}

int basicTest4(int a, int b){
	a = a - b;
	a = a - b;
	if (a>b)
		a++;
	return b;
}

int basicTest5(int a, int b){
	while (a!=b){
		if (a>b)
			a = a - b;
		else
			b = b - a;
	}
	return a;
}

int basicTest6(int a){
	a = a - 1;
	a = a*(-1);
	if (a>0)
		return a;
	return 1;
}

int basicTest7(int a){
	a = a - 1;
	a = a*-1;
	if (a>0)
		return a;
	return 1;
}

int basicTest8(int a){
	a = -a;
	a = a + 1;
	if (a>0)
		return a;
	return 1;
}
int basicTest9(int a){
	a = -a;
	a = a + 1;
	if (a>0)
		return a;
	return 1;
}
int basicTest10(int a){
	a = -a+-1;
	a = -a;
	a = a + 1;
	if (a>0)
		return a;
	return 1;
}
int basicTest11(int a){
	a = -a;
	a--;
	a = -a;
	a = a + 1;
	if (a>0)
		return a;
	return 1;
}

/*
In this function, the value of right expression is casted 
into integer type depdending on the type of left variable
*/
int basicTest12(int a){
	a /= 3;
	a *= 2;
	if (a>0)
		return a;
	return 1;
}

/*
In this function, the value of right expression is casted 
into integer type depdending on the type of left variable
*/
int basicTest13(int aBc){
	aBc /= 3;
	aBc *= 2;
	if (aBc>0)
		return aBc;
	return 1;
}

int basicTest14(char sv_name_2,int sv_age){
	char c = sv_name_2;

	if (sv_age > 0){
		if (c == 'a')
			return 0;
		else
			return 1;
	} else
		return 2;
}

int basicTest15(bool a){
	if (a)
		return 1;
	else
		return 0;
}

int basicTest16(bool a){
	if (!a)
		return 1;
	else
		return 0;
}

int basicTest17(int a, int b, int c){
	if (a--> b++ + c--){
		if (c==a)
			return 1;
		else
			return 0;
	}
	else
		return 0;
}
int basicTest18(bool a, int b){
	if (!a && b==1)
		return 1;
	else
		return 0;
}
int basicTest19(bool a, int b){
	if (a && b==2)
		return 1;
	else
		return 0;
}
int basicTest20(bool a, bool b){
	if (a && !b)
		return 1;
	else
		return 0;
}
int basicTest21(bool a, bool b){
	if ((a) && (!b))
		return 1;
	else
		return 0;
}
int basicTest22(int& a){
	a++;
	return a*2;
}


