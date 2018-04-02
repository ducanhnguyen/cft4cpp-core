#include <iostream>
#include <cstring>
#include <iomanip>
#include <cstdlib> 
using namespace std;
 
struct Node
{
 	Node *N;
};
 
class F{
	public:
		int classf;
		F *classF;
};
 
class A{
	private:
		float externalA;
	public:
		int o;
		int f;
		int p;
		Node *front;
		static int x;
		static Node *staticNode;
		static F classFF;
};

class B{
	public:
		int e;
		A classA;
};

int externalClass;

class C
{
  	private:
  	int external,sum,external2;
  	A *sv;
  	B classB;
  	F classF;
  	int classf;
  	public:
  		static int staticInt;
  		
  	void test(A *,int);
  	
  	void testStatic(){
  		A::x = 123;
  		A::classFF.classf++;
  		A::classFF.classF->classF = NULL;
  	};
};


void C::test(A *n, int asd)
{
	classf++;
	//classF.classF[0].classf++;
	classF.classF[0].classF->classF[1].classf++;
	
	int a;
	
	classB.classA.front->N=NULL;
	
	classB.classA.f++;
	classB.e++;
	
	int array[10];
	
	int asdfq, sum ;
	sum++;
	
	int b=a+1;
	 for (int i=0;i<a;i++){
    	A sv;
    	
    	while (external<0){
    		external+=12;
    	}
    	
    	int external2 = 0;
    	for (int pos=0;pos<b;pos++){
    		cout <<"qwe";
    		sv.o+=0;
			external2+=12;
    	}
    	
    }

    sv->o++;

	externalClass++;
	
    if(sv == NULL)
    {
        sv = n;
    }
    else
    {
        sv->p = n->o;
    }
}
 
int main()
{
    return 0;
}


