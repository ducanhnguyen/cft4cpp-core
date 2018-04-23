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

int Fibonacci(int n){
	if ( n == 0 )
		return 0;
	else if ( n == 1 )
		return 1;
	else
		return ( Fibonacci(n-1) + Fibonacci(n-2) );
} 
int add_digits(int n) {
	static int sum = 0;
	
	if (n == 0) {
		return 0;
	}	
	sum = n%10 + add_digits(n/10);

	return sum;
}

int compare_string(char *first, char *second){
	while(*first==*second){
		if ( *first == '\0' || *second == '\0' )
			break;
		
		first++;
		second++;
	}
	if( *first == '\0' && *second == '\0' )
		return 0;
	else
		return -1;
}

void mergeTwoArray(int a[], int m, int b[], int n, int sorted[]) {
	int i, j, k, t;

	t = j = k = 0;

	for (i = 0; i < m + n; t++) {
		if (j < m && k < n) {
			if (a[j] < b[k]) {
				sorted[i] = a[j];
				j++;
			} else {
				sorted[i] = b[k];
				k++;
			}
			i++;
		} else if (j == m) {
			for (int t = 0; i < m + n;t++) {
				sorted[i] = b[k];
				k++;
				i++;
			}
		} else {
			for (int t = 0; i < m + n;t++) {
				sorted[i] = a[j];
				j++;
				i++;
			}
		}
	}
}
int main(){
	return 0;
}
