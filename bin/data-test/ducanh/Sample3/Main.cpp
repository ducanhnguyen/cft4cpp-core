#include <iostream>
#include<cstdlib>

using namespace std;

int TestDieuKienFirst(int *a, int *b){
	if (a >0){
		return 1;
	}
	return 0;
}

int main(){
	int a = 1;
	int b = 2;
	cout << TestDieuKienFirst(&a, &b) << endl;
	system("pause");
	return 0;
}
