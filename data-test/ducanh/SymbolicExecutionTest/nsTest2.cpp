/*
This is an example of simple namespace.
Function is defined in namespace directly without putting in any classes.
*/
#include <iostream>
using namespace std;

namespace nsTest2{	
	int isAvailable(int a){
		if (a<999)
			return 0;
		else
			return 1;
	}
}
/*
int main(){
	cout << nsTest2::isAvailable(1);
	return 0;
}
*/
