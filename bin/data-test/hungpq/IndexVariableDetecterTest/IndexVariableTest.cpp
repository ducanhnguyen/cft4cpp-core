#include <iostream>
using namespace std;
struct Others;
struct SinhVien{
		int age;
		char* name;
		Others* other;
};

struct Others{
		char* eeee;
};

int index_test1(SinhVien sv){
	
	/*char *s=sv.name, *p=sv.other[0].name;
	
	SinhVien sv1,sv2;
	SinhVien sv=sv,qwe=sv.other[1];
	SinhVien *poi,*asd;
	
	SinhVien ar[12]=sv.qwe,rt[23]=asd;*/
	
	char *op,kl[1];
	
	char* s = sv.other[0].eeee;
	char* s1;
	s1 = s;
	
	s[0]=op[2]='b';
	s = sv.name;
	s[0]='q';
	s1[0] = 'a';
	
	for (int i=0;i<2;i++){
		char* s = sv.other[1].eeee;
		s[0]='h';
		if ('h'==s[i]!=s[2]) return 0;
	}
	cout << op[2] << s[0] << endl;

	SinhVien test = sv;
	SinhVien sv1 = test;
	
	if (NULL == test.name){
		if (test.name[0]!='a'){
			
		}
	}

	if (sv1.age > 0) 
	if (s[0] == 'a')
		return 0;
	else
		return 1;
}
