#include "iostream"
#include "header/B.h"

using namespace std;
namespace A{
	//int namespaceB = 10;
	class C2;
	class C1{
		private:
			int x;
		public:
			int sum;
	};
	namespace B{
		int a=0;
	}
}
namespace B{
	class C1{
		public:
			C2 class2;
			int classC1;
			void test();
	};
	//int namespaceB=11;
	void C1::test(){
		using namespace A;
		//namespaceB++;
		cout << "namespaceB: " << namespaceB << endl;
		class2.f++;
	}
}
namespace A{
	int namespaceA;
	class C2{
		private:
			int x;
		public:
			static int oop;
			void test(){
				
				A::C2::oop++;
			
			}
	};
	C2 classC2;
	namespace B{
		int b = 0;
	}
}
int test5(){
	A::B::a++;
	A::B::b++;
	
	A::namespaceA++;
	B::namespaceB++;
	
	A::B::b++;
	
	B::C1 c;
	c.test();
	
	int b=0;
	b++;
	
	
	return 0;
}
int main(){
	return 0;
}
