#include <iostream>
#include "Header.h"
#include "Source.cpp"
//#include "TestExpression.cpp"
#include "C:\Users\DucToan\Documents\GitHub\ava-master\ava\Source code\CppProjectTesting\data-test\ducanh\SeparateFunctionTest\header\DemoCpp.cpp"
using namespace std;

int test(int a) {
	return a + 5;
}


//Ham co ngay trong file
int test1() {
	int a = 5;
	if ( test(a) > 5) {
		return 1;
	} else {
		return 0;
	}
}

//Ham co trong file Header
int test2() {
	int a = 5;
	if (add(a) > 5) {
		return 1;
	} else {
		return 0;
	}
}

//Hàm có trong include duong dan tuong doi
int test3() {
	int a = 5;
	if (sub(a) > 5) {
		return 1;
	} else {
		return 0;
	}
}

//Hàm có trong include duong dan tuyet doi file .cpp
int test4() {
	int a = 10;
	if (mul(a)> 5) {
		return 1;
	} else {
		return 0;
	}
}

int main() {
	return 0;
}
