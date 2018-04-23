#include <iostream>

using namespace std;

//Bình thường, ko default
int test1(int x) {
	int a = 0;
	switch (x) {
		case 1 : a++; break;
		case 2 : a--; break;
	}
}

//Bình thường có default ở cuối
int test2(int x) {
	int a = 0;
	switch (x) {
		case 1 : a++; break;
		case 2 : a--; break;
		default : a += 2; break;
	}
}

//Default ở giữa có break
int test3(int x) {
	int a = 0;
	switch (x) {
		case 1 : a++; break;
		default : a += 2; break;
		case 2 : a--; break;
	}
}

//Default ở giữa không có break
int test4(int x) {
	int a = 0;
	switch (x) {
		case 1 : a++; break;
		default : a += 2;
		case 2 : a--; break;
	}
}

//Default ở đầu có break
int test5(int x) {
	int a = 0;
	switch (x) {
		default : a += 2; break;
		case 1 : a++; break;
		case 2 : a--; break;
	}
}

//Default ở đầu không có break
int test6(int x) {
	int a = 0;
	switch (x) {
		default : a += 2;
		case 1 : a++; break;
		case 2 : a--; break;
	}
}

//case liền nhau
int test7(int x) {
	int a = 0;
	switch (x) {
		case 1 :
		case 2 : a--; break;
		// if (x == 1 || x == 2) {a--;break}
	}
}

int main() {
}

//===========================================
//Trường hợp :
// - Có break, hoặc không
// - vi trí Default : không có, đầu,giữa ,cuối
// - case liền nhau không có thân
// - 
