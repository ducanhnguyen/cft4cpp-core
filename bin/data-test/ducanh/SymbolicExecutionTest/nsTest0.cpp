/*
This is an example of simple namespace.
Function is declared in class, but defined outside its class
*/
#include <iostream>
using namespace std;

namespace nsTest0{
	const int MINIMUM = 999;
	
	class Student{
		private:
			int id;
		public:
			Student(){}
			int getId(){return id;}
			void setId(int id_){id = id_;}
			int isAvailable(int id);
			int isAvailable2(int id);
	};
	
	int Student::isAvailable(int id){
		if (id < 9999)
			return 0;
		else
			return 1;
	}
	
	int Student::isAvailable2(int id){
		if (id < MINIMUM)
			return 0;
		else
			return 1;
	}
}

/*
int main(){
	using namespace nsTest0;
	Student s;
	s.setId(12);
	cout << s.getId();
	return 0;
}
*/
