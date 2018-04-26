#include "stdio.h"

// Simple
int testSimple(int a) {
	a = 0;
}

// If
void testIf(int a) {
	if (a>0)
		a++;
}

// If-Compound Clause
void testIf_CompoundBody(int a) {
	if (a>0){
		a++;
	}
}

// If-Else
void testIfElse(int a) {
	if (a>0)
		a++;
	else
		a--;
}

// If-Else-If
void testIfElseIf(int a) {
	if (a>0)
		a++;
	else if (a<0)
		a--;
}

// Do
void testDo(int a) {
	do {
		a++;
	} while (a>0);
}

// While
void testWhile(int a) {
	while (a>0)
		a++;
}

// While - compound body
void testWhile_CompoundBody(int a) {
	while (a>0){
		a++;
	}
}
// For
void testFor(int a) {
	for (int i = 0; i < 10; i++)
		a++;
}

// For – compound body
void testFor_CompoundBody(int a) {
	for (int i = 0; i < 10; i++){
		a++;
	}
}

// Break
void testBreak(int a) {
	do {
		if (a==2)
			break;
	} while (a++>0);
	a--;
}
// Break – compound body
void testBreak_CompoundBody(int a) {
	do {
		if (a==2){
			break;
		}
	} while (a++>0);
	a--;
}

// Continue 
void testContinue(int a) {
	do {
		if (a==2)
			continue;
	} while (a++>0);
	a--;
}

// Continue – compound body
void testContinue_CompoundBody(int a) {
	do {
		if (a==2){
			continue;
		}
	} while (a++>0);
	a--;
}

