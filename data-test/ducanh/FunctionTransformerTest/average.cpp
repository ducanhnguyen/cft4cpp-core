#include <iostream>
using namespace std;
class House;
class People;

class Town{
	private:
		House* houses;
	public:
		House* getHouses(){return houses;}
};

class House{
	private:
		char* address;
		People* individuals;
	public:
		char* getAddress(){return address;}
};

class People{
      private:
              char* name;
              int age;
      public:
             char* getName(){return name;}
             int getAge(){return age;}
};
// 
void average_test_0(Town town){
	char* firstAddress = town.getHouses()[0].getAddress();

	if (firstAddress[0] == 'M') 
	   cout << "The first house is located at M. street";
    else
	   cout << "The first house is not located at M. street";    	   
}
void average_test_1(Town town){
	char* firstAddress = town.getHouses()[0].getAddress();

	if (firstAddress != NULL) 
	   cout << "The address of first house is set";
    else
	   cout << "The address of first house is not set";    	   
}

//-----only for test---------------------------------
void average_test_0(char* town_houses_0_address,Town town){
	char* firstAddress = town_houses_0_address;

	if (firstAddress[0] == 'M') 
	   cout << "The first house is located at M. street";
    else
	   cout << "The first house is not located at M. street";    	   
}
