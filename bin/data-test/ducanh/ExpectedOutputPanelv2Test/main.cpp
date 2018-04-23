#include <iostream>
using namespace std;
//-------------------------------
int a = 0;
int test0(){
    if (a>0)
        return 1;
    else
        return 0;
}
//-------------------------------
int* b;
int* test1(){
     b[1] = 2;
     return b;
}

//-------------------------------
class Lop{
      char* name;
};
class SinhVien{
      private:
      			Lop lop; 
              int a;
              int* a1;
              int a2[3];
              int a3[];
              int a4[][4];
			  
			  float f1;
			  double f2;
              
              char* b;
              char b1;
              char b2[];
              char e[5];
              bool* boo;
              bool b2[];
              bool b3[4];
              
              SinhVien* sv;
              SinhVien sv2[];
              SinhVien sv3[3];
                           
};
SinhVien test4(int age, char* name){
         SinhVien sv;
         //...
         return sv;
}

SinhVien* test5(int age, char* name){
         SinhVien* sv = new SinhVien[6];
         //...
         return sv;
}

int main(){
int EO_returnVar_a = 1;
int* EO_returnVar_a1 = new int[3];EO_returnVar_a1[1] = 1;EO_returnVar_a1[2] = 2;EO_returnVar_a1[3] = 3;
int EO_returnVar_a2[] = {5,6};
int EO_returnVar_a3[] = {1,2,3,4};
char* EO_returnVar_b = "anh";
char EO_returnVar_b1 = 'd';
char EO_returnVar_b2[] = "anhanh";
char EO_returnVar_e[] = "aaa";
int EO_returnVar_sv_0__a = 4;



    return 0;
}
