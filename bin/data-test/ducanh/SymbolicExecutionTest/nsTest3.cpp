/*
Nested namespace contains the same name of methods
*/
#include <iostream>
using namespace std;

namespace nsTest3{
	
	int isAvailable(int a){
		if (a<999)
			return 0;
		else
			return 1;
	}
	
	namespace second_space{
		int isAvailable(int a){
			a--;
			return a;
		}
	}
}
/*
int main(){
	cout << nsTest3::second_space::isAvailable(6);
	return 0;
}*/
