#include <iostream>
#include <cstring>
using namespace std;
struct TT{
       
       private:
	   		int qwe;
       public:
     	  	char name[10];
    	    char address[10];
       
       		void setQwe_(int qwe_){qwe = qwe_;}
    		int getQwe_(){return qwe;}
};

class SinhVien{
      private:
              int age;
              char* name;
              TT tt;
              SinhVien *sv;
      public:
             int age1;
             char* name1;
             
             void setAge_(int age_){age = age_;}
             int getAge_(){return age;}
             
             void setName_(char* name_){name = name_;}
             char* getName_(){return name;}
             
             void setTT_(TT tt_){tt = tt_;}
             TT getTT_(){return tt;}
             
             void setSv_(SinhVien* sv_){sv = sv_;}
             SinhVien* getSv_(){return sv;}
             
};

int test1(){   
	int i=49;
	sv.setAge_(34);
	
	SinhVien* sv = new SinhVien[3];
	sv[0].setAge_(48);
	
	sv[0].getName_()[2]='a';
	TT o = sv->getTT_();
	
	o.setQwe_(12);
	
	sv[0].getSv_().getTT_().getQwe_()=3;
	
	sv[0].getSv_().getTT_().setQwe_(2);
	sv[0].getSv_()[1]->getSv_()->setAge_(1);
	
	sv[0].sv->setAge_(78);
	
	sv[0].setName_("D5");
	strcpy(sv[0].getTT_().name,"1H=13.F/**");
	strcpy(sv[0].getTT_().address,"=->4-EB-D>");
	sv[0].age1=49;
	sv[0].name1="B0";
	sv[1].setAge_(48);
	sv[1].setName_("AO");
	
	return 0;
}
