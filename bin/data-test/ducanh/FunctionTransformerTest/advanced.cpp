class Others;
class SinhVien{
private:
	int age;
	char* name;
	Others* other;
public:
	void setAge(int age){ this->age = age; }
	void setName(char* name){ this->name = name; }
	int getAge(){return age;}
	char* getName(){return name;}
	Others* getOther(){ return other; }
};

class Others{
private:
	char* eeee;
public:
	char* getName(){ return eeee; }
	void setName(char* name){ this->eeee = name; }	
};

int class_test0(SinhVien sv){

	if (sv.getAge() > 0)
	if (sv.getName()[2] == 'a')
		return 0;
	else
		return 1;
}

int class_test1(SinhVien sv){
	char c = sv.getName()[2];

	if (sv.getAge() > 0)
	if (c == 'a')
		return 0;
	else
		return 1;
}

int class_test2(SinhVien sv){
	char* s = sv.getOther()[0].getName();

	if (sv.getAge() > 0)
	if (s[0] == 'a')
		return 0;
	else
		return 1;
}

SinhVien do_something(SinhVien sv){
    if (sv.getAge() > 0){
       int a = 0;
    }
    return sv;     
}
int struct_test0(int sv_age,char sv_name_2,SinhVien sv){
	if (sv_age > 0) 
	if (sv_name_2 == 'a')
		return 0;
	else
		return 1;
}

