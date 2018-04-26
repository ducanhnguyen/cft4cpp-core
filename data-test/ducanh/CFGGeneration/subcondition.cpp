#include "stdio.h"

// Simple &&
int testSubcondition0(int a, int b) {
	if (a>1&&b>1)
		return 0;
	else
		return 1;
}

// Simple ||
int testSubcondition1(int a, int b) {
	if (a>1||b>1)
		return 0;
	else
		return 1;
}

// Complex ||
int testSubcondition2(int a, int b, int c) {
	if (a>1||b>1||c>1)
		return 0;
	else
		return 1;
}

// Complex &&
int testSubcondition3(int a, int b, int c) {
	if (a>1&&b>1&&c>1)
		return 0;
	else
		return 1;
}

// Complex && and ||
int testSubcondition4(int a, int b, int c) {
	if (a>1&&(b>1||c>1))
		return 0;
	else
		return 1;
}

// Complex && and ||
int testSubcondition5(int a, int b, int c) {
	if (a>1||(b>1&&c>1))
		return 0;
	else
		return 1;
}

int main(){
	return 0;
}
