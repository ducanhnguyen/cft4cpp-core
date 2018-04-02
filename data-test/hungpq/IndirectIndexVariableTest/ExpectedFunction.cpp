#include <iostream>
using namespace std;

int struct_test1(SinhVien sv){
	
	/*char *s=sv.name, *p=sv.other[0].name;
	
	SinhVien sv1,sv2;
	SinhVien sv=sv,qwe=sv.other[1];
	SinhVien *poi,*asd;
	
	SinhVien ar[12]=sv.qwe,rt[23]=asd;*/
	
	char *op,kl[1];
	
	char* s = sv.other[0].eeee;
	char* s1;
	s1 = s;
	
	sv.other[0].eeee[0]=op[2]='b';
	s = sv.name;
	sv.name[0]='q';
	sv.other[0].eeee[0] = 'a';
	
	for (int i=0;i<2;i++){
		char* s = sv.other[1].eeee;
		sv.other[1].eeee[0]='h';
		if ('h'==sv.other[1].eeee[i]!=sv.other[1].eeee[2]) return 0;
	}
	cout << op[2] << sv.name[0] << endl;

	SinhVien test = sv;
	SinhVien sv1 = test;
	
	if (NULL == sv.name){
		if (sv.name[0]!='a'){
			
		}
	}

	if (sv.age > 0) 
	if (sv.name[0] == 'a')
		return 0;
	else
		return 1;
}

