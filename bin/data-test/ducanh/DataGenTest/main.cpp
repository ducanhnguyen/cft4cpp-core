#include <iostream>
#include <cstring>
using namespace std;
struct TT{
       char name[10];
       char address[10];
};

class SinhVien{
      private:
              int age;
              char* name;
              TT tt;
      public:
             int age1;
             char* name1;
             
             void setAge_(int age_){age = age_;}
             int getAge_(){return age;}
             
             void setName_(char* name_){name = name_;}
             char* getName_(){return name;}
             
             void setTT_(TT tt_){tt = tt_;}
             TT getTT_(){return tt;}
             
};

int test(int i,int* i1,int i2[],int i3[2],char c,char* c1,char c2[],char c3[10],
     SinhVien* sv,SinhVien sv1,SinhVien sv2[]){
     	if (i>0&&i1[0]==1&&i2[1]==2&&i3[2]==3&&c==65&&c1[0]=='7'&&c2[4]==67&&c3[10]==88&&sv1.age1==1&&sv==NULL)
     		return 0;
     	else
     	return 1;
}
int main(){
    return 0;
}
