#include <iostream>


using namespace std;


int  main(){
	int * pd;
	float * pd_tmp;
try {
    
    if (pd==0) cout << "test try catch 1\n";
	else cout << "test try catch 2\n";
    

  } catch (exception& e) {cout << "Exception: " << e.what();}
	return 0;
}
