#include <iostream>


using namespace std;


void  sample11(){

  int n=10;
mylabel:
  cout << n << ", ";
  n--;
  if (n>0) goto mylabel;
  cout << "liftoff!\n";

}

int  main(){

	return 0;
}