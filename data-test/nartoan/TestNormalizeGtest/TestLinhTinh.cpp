#include <iostream>
using namespace std;
void testNormalizeArray(int a[4]){
	cout << a[0] << endl;
	if (a[0] < 3) {
		a[2] = 8;
	} else {
		a[3] = 9;
	}
	 return;
}

//void generatePointer(int* a, int* b) {
void  sample06(int * p){

 int numbers[5];
  p = numbers;  *p = 10;
  p++;  *p = 20;
  p = &numbers[2];  *p = 30;
  p = numbers + 3;  *p = 40;
  p = numbers;  *(p+4) = 50;
  for (int n=0; n<5; n++)
    cout << numbers[n] << ", ";

}

int main()
{
	/* code */
	return 0;
}
