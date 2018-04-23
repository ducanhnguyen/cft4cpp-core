#include <iostream>
#include <exception>
using namespace std;

int control_block1(int a, int b){
	while (a!=b){
		if (a>b)
			a = a - b;
		else
			b = b - a;
	}
	return a;
}

int control_block2(){
   int a = 10;

   while( a < 20 ){
       cout << "value of a: " << a << endl;
       a++;
   } 
   return 0;
}

int control_block3(){
	for( int a = 10; a < 20; a = a + 1 ){
		cout << "value of a: " << a << endl;
	} 
	return 0;
}

int control_block4(){
	int a = 10;
	do{
		cout << "value of a: " << a << endl;
		a = a + 1;
	}while( a < 20 );
	
	return 0;
}

int control_block5(){
	int i, j;
	
	for(i=2; i<100; i++) {
		for(j=2; j <= (i/j); j++)
			if(!(i%j)) 
				break; // if factor found, not prime
				
		if(j > (i/j)) 
			cout << i << " is prime\n";
	}
	return 0;
}
