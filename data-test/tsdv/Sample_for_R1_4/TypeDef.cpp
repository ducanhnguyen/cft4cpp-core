
typedef int MyInt;
int SimpleTypeDefTest(MyInt x){
	if (x > 0)
		return 1;
	else
		return 0;
}


typedef int *MyIntPtr;
int PointerTypeDefTest(MyIntPtr x){
	if (*x > 0)
		return 1;
	else
		return 0;
}

typedef int IntType, *IntTypePtr, IntType2;
int CombineTypeDefTest(IntType x, IntTypePtr y, IntType2* z){
	if (x > 0 && *y > 0 && *z > 0)
		return 1;
	else
		return 0;
}

typedef int Color[3];
int ArrayTypeDefTest(Color c){
	if (c[0] > 0 && c[1] > 1 && c[2] > 2)
		return 1;
	else
		return 0;
}

typedef struct {
	int x;
} MyStruct1;
int AnnonymousStructTypeDefTest(MyStruct1 s){
	if (s.x > 0)
		return 1;
	else
		return 0;
}

struct MyStruct2{
	int x;
};
typedef MyStruct2 MyStruct3;
int RedeclareStructTypeDefTest(MyStruct3 s){
	if (s.x > 0)
		return 1;
	else
		return 0;
}

typedef struct MyStruct4{
	int x;
} MyStruct5;
int StructCombineTypeDefTest(MyStruct4 m, MyStruct5 n){
	if (m.x > 0 && n.x > 0)
		return 1;
	else
		return 0;
}

typedef MyStruct3 MyStruct33;
int ChainingTypeDefTest(MyStruct33 s){
	if (s.x > 0)
		return 1;
	else
		return 0;
}
