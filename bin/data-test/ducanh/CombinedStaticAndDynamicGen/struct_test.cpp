struct Others;
struct SinhVien{
		int age;
		char* name;
		Others* other;
};

struct Others{
		char* eeee;
};

int struct_test0(SinhVien sv){
	if (sv.age > 0) 
	if (sv.name[2] == 'a')
		return 0;
	else
		return 1;
}

int struct_test1(SinhVien sv){
	char* s = sv.other[0].eeee;
	
	if (sv.age > 0){
		if (s[0] == 'a')
			return 0;
		else
			return 1;
	}else{
		return 2;		
	}
}
