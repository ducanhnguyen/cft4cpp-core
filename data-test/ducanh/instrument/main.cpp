#include <iostream>
using namespace std;

void simple() {
	int a = 0;
	int b, c;
}

void if_control_block(int a) {
	if (a>1)
		a++;
}

void if_else_control_block(int a) {
	if (a>0) // two branches from condition a>0
		a++;
	else
		a--;
}

void if_else_control_block_2(int a) {
	if (a>0){ // two openning bracket from condition a>0
		a++;
	}else{
		a--;
	}
}
void if_else_control_block_3(int a) {
	if (a>0){  // two identical statements from condition a>0
		a++; 
	}else{
		a++;
	}
}
void for_control_block(int a) {
	for (int i=0; i<a; i++) {
		a++;
	}
}

void for_control_block_2(int a) {
	int i = 0;
	for (; i<a; i++) { // no initialization in for
		a++;
	}
}

void while_control_block(int a) {
	while (a<1) {
		a--;
	}
}

void do_control_block(int a) {
	do {
		a--;
	} while(a<1);

}

void try_catch() {
	try {
		throw 20;
	} catch (int e) {
		cout << "An exception occurred. Exception Nr. " << e << '\n';
	}
}

int main(){
	return 0;
}
