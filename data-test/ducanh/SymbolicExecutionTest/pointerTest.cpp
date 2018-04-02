/*
This file contains atomic functions relating to pointers
*/
#include <iostream>

using namespace std;


int pointerTest0(const int* a){
	if (a[  0]  <  10)
		return 1;
	else
		return 0;
}
int pointerTest1(const int* a,int b){
	if (a[b]==0)
		return 1;
	else
		return 0;
}

int pointerTest2(const int* a,int b){
	if (a[b]==a  [   b-1])
		return 1;
	else
		return 0;
}

int pointerTest3(int* p1, int* p2,int a){
	if (p1[0] == p2[1+a])
		return 0;
	return 1;
}

int pointerTest4(int* p1,const int& a){
	p1[0] = a;
	if (p1[0] + p1[1]== 2*a+1)
		return 0;
	return 1;
}


int pointerTest5(int* p,int* p2){
	p = p2;
	if (p[0]==p2[1])
		return 1;
	return 0;
}

int pointerTest6(int* p1,int* p2){
	if (*(p1+0) == *(p2+1))
		return 1;
	return 0;
}

int pointerTest7(int* p1,int* p2){
	if (*(p1) == *(p2+1)){
		p1 = p2;
		if (*(p1) == *(p2+1))
			return 1;
	}
	return 0;
}

int pointerTest8(int* p1,int* p2){
	p1 = p2+1;
	if (*(p1) == *(p2+2))
		return 1;
	return 0;
}

int pointerTest9(int* p1,int* p2){
	p1 = p2;
	
	if (*p1 - 10000 > *(p2+1))
		return 1;
	return 0;
}

int pointerTest10(int* p1,int* p2){
	p1 = p2;
	
	if (*p1 - 10000 > *(p2+1 + (1+1)))
		return 1;
	return 0;
}
int pointerTest11(int* p1,int& a){
	p1= &a;

	if (*p1==2)
		return 1;
	return 0;
}

int pointerTest12(int* p1,int& a){
	a = *(p1+4);

	if (*p1==a)
		return 1;
	return 0;
}

int pointerTest13(int numbers[5], int * p){
    p = numbers+3;  
    if (p[0] == 3)
       return 1;
    return 0;
}

int pointerTest14( int numbers[5], int * p){
    p = &numbers[3];  
    if (p[0] == 3 && p[1] == 50)
       return 1;
    return 0;
}
int pointerTest15(char* eeee){
	char* s;
	s = eeee;

	if (s[0] == 'a')
		return 0;
	else
		return 1;
}
int pointerTest16(char* eeee){
	char* s = eeee;

	if (s[0] == 'a')
		return 0;
	else
		return 1;
}
int pointerTest17(int* a){
	while (a==NULL){
		a = new int[3];
	}
	return 1;
}
/*
In condition a!=NULL, there are two solutions:
1. a==NULL. However, it not suitable because of the assignment a[0]=1
2. a!=NULL
*/
int pointerTest18(int* a){
	a[0]=1;
	while (a!=NULL){
		a = NULL;
	}
	return 1;
}

int pointerTest19(int* p1){
	(p1)++;// p1 points to next element
	if (*(p1) ==2)
		return 1;
	else
		return 0;
}

int pointerTest20(int* p1){
	p1++;// p1 points to next element
	p1++;// p1 points to next element
	p1--;
	if (*(p1) ==2)
		return 1;
	else
		return 0;
}

int pointerTest21(int* p1){
	int a = (*p1)++;
	if (a ==2)
		return 1;
	else
		return 0;
}

int pointerTest22(int* p1){
	int a = ++(*p1);
	if (a ==2)
		return 1;
	else
		return 0;
}

int pointerTest23(int** p1){
	p1[0][0]++;
	if (p1[0][0] ==2)
		return 1;
	else
		return 0;
}

int pointerTest24(const bool* a){
	if (a[  0] && a[1] && !a[2])
		return 1;
	else
		return 0;
}

void pointerTest25(char* a){
	if (a[0]=='1')
		a[0]='a';
}
