#include <iostream>
#include <cstring>
using namespace std;

int test1(){   
	int i=49;
	sv.age=34;
	
	SinhVien* sv = new SinhVien[3];
	sv[0].age=48;
	
	sv[0].name[2]='a';
	TT o = sv->tt;
	
	o.qwe=12;
	
	sv[0].sv[0].tt.qwe=3;
	
	sv[0].sv[0].tt.qwe=2;
	sv[0].sv[1]->sv[0]->age=1;
	
	sv[0].sv->age=78;
	
	sv[0].name="D5";
	strcpy(sv[0].tt.name,"1H=13.F/**");
	strcpy(sv[0].tt.address,"=->4-EB-D>");
	sv[0].age1=49;
	sv[0].name1="B0";
	sv[1].age=48;
	sv[1].name="AO";
	
	return 0;
}
