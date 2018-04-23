// test class goi/chua class khac
#include <iostream>
using namespace std;
class simpleClass{
	public:
	int element1;
	int element2;
};
class innerComplexClass{
	public:
	int element1;
	int element2;
	simpleClass ll;
};
class complexClass{
	public:
	innerComplexClass* r;
	innerComplexClass d;
};
void test3(complexClass complexClassVariable){
}

int main(){
	complexClass complexClassVariable;
	complexClassVariable.r = new innerComplexClass[2];
	complexClassVariable.r[0].element1 = 93;
	complexClassVariable.r[0].element2 = 48;
	complexClassVariable.r[0].ll.element1 = 53;
	complexClassVariable.r[0].ll.element2 = -91;
	
	
	complexClassVariable.r[1].element1 = -47;
	complexClassVariable.r[1].element2 = -43;
	complexClassVariable.r[1].ll.element1 = 67;
	complexClassVariable.r[1].ll.element2 = -62;
	
	
	complexClassVariable.d.element1 = -32;
	complexClassVariable.d.element2 = -34;
	complexClassVariable.d.ll.element1 = 93;
	complexClassVariable.d.ll.element2 = 37;

	return 0;
}

