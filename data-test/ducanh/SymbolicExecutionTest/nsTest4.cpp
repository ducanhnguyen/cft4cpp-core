/*
Function of a name space is declared in header files, but defined outside.
The function may be defined in two ways:
- Approach 1: Use keyword ::
- Approach 2: Define directly in the name space
*/
#include <iostream>
#include "nsTest4.h"
using namespace std;

int nsTest4::func1(int a){
	if (a>0)
		return 1;
	else
		return 0;
}
namespace nsTest4{
	int func2(int a, int b){
		if (a > b)
			return 1;
		else
			return 0;
	}	
}
/*
int main(){
	cout << nsTest4::func2(1,2);
	return 0;
}
*/
