/*
Namespace include another namespace in its function.
The included namespace is defined by its header
*/
#include "nsTest4.h"
#include <iostream>
using namespace std;

namespace nsTest5{
	int test1(int a){
		int x = nsTest4::func1(a);
		return x;
	}
	int test2(int a){
		using namespace nsTest4;
		int x = func1(a);
		return x;
	}
}
/*
int main(){
	cout << nsTest5::test2(-1);
	return 0;
}*/
