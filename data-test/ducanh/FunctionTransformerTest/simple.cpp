class People{
	private:
		int age;
		char* name;
	public:
		int getAge(){
			return age;
		}
		char* getName(){
			return name;
		}
};


int simple_test0(People p){
	if (p.getAge() > 0) 
		return 0;
	else
		return 1;
}

int simple_test1(People p){
	if (p.getName()[2] == 'a')
		return 0;
	else
		return 1;
}

int simple_test2(People p){
	if (p.getAge() > 0) 
	if (p.getName()[2] == 'a')
		return 0;
	else
		return 1;
}

int simple_test3(People p){
	if (p.getAge() > 0 && p.getName()[2] == 'a') 
		return 0;
	else
		return 1;
}

int main(){
	return 0;
}

//-----only for test---------------------------------
int simple_test0(int p_age,People p){
	if (p_age > 0) 
		return 0;
	else
		return 1;
}
int simple_test1(char p_name_2,People p){
	if (p_name_2 == 'a')
		return 0;
	else
		return 1;
}
int simple_test2(int p_age,char p_name_2,People p){
	if (p_age > 0) 
	if (p_name_2 == 'a')
		return 0;
	else
		return 1;
}

int simple_test3(int p_age,char p_name_2,People p){
	if (p_age > 0 && p_name_2 == 'a') 
		return 0;
	else
		return 1;
}
