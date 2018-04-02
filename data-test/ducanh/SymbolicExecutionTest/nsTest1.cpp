/*
This is an example of simple namespace.
Function is declared and defined in its class
*/
#include <iostream>
using namespace std;

namespace nsTest1{	
	class Student{
		private:
			int id;
		public:
			Student(){}
			int getId(){return id;}
			void setId(int id_){id = id_;}
			int isAvailable(int id){
				if (id < 999)
					return 0;
				else
					return 1;	
			}
	};
}

/*
int main(){
	using namespace nsTest1;
	Student s;
	s.setId(12);
	cout << s.isAvailable(s.getId());
	return 0;
}
*/
