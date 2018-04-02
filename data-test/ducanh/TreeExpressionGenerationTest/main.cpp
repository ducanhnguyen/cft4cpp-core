#include <iostream>
// Không du?c thay d?i th? t? di?u ki?n
int test(int a, int b, int c){
	if (1)// condition 0
		return 0;
	if ((1))// condition 1
		return 0;
	if (1>0)// condition 2
		return 0;
	if (a>0 && b > 0)// condition 3
		return 0;
	if (a>0 && b > 0 && c>0)// condition 4
		return 0;
	if (a>0 && b > 0 && ((c>0)))// condition 5
		return 0;
	if (a>0 || (b > 0 && b<9))// condition 6
		return 0;
	if (a>0 || (b > 0 && (b<9 || b>1)))// condition 7
		return 0;
	return 1;
}
int main(){
	return 0;	
}

