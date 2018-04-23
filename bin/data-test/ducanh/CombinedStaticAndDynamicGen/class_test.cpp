#include <iostream>
#include <cstring>
#include <iomanip>
#include <cstdlib> 
using namespace std;
class Others;
class SinhVien{
private:
	int age;
	char name[2];
	Others* other;
public:
    void setAge_(int age){ this->age = age; }
    int getAge(){return age;}

	void setName_(char* name1){ strcpy(name,name1); }
	char* getName(){return name;}
        
    void setOther_(Others* other){this->other = other;}
    Others* getOther(){ return other; }
};

class Others{
private:
	char* eeee;
public:
	char* getEeee(){ return eeee; }
	void setEeee(char* name){ this->eeee = name; }	
};

int class_test0(int i,int* i1,int i2[],int i3[2],char c,char* c1,char c2[],char c3[10], SinhVien* sv,SinhVien sv1,SinhVien sv2[]){
    if (i>0&&i1[0]==1&&i2[1]==2&&i3[2]==3&&c==65&&c1[0]=='7'&&c2[4]==67&&c3[10]==88&&sv1.getAge()==1&&sv==NULL)
    	return 0;
    else
    	return 1;
}

int class_test1(SinhVien sv){
	if (sv.getAge() > 0){
			if (sv.getName()[2] == 'a')
				return 0;
			else
				return 1;
	}else
		return 2;
}

int class_test2(SinhVien sv){
	char c = sv.getName()[2];

	if (sv.getAge() > 0){
			if (c == 'a')
				return 0;
			else
				return 1;
	}else
		return 2;		
}

int class_test3(SinhVien sv){
	char* s = sv.getOther()[0].getEeee();

	if (sv.getAge() > 0){
		if (s[0] == 'a')
			return 0;
		else
			return 1;
	}
	else
		return 2;
}

SinhVien class_test4(SinhVien sv){
    if (sv.getAge() > 0){
       int a = 0;
    }
    return sv;     
}

int main(){
    return 0;
}
