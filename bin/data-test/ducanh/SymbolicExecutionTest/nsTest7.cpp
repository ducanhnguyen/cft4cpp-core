#include "nsTest4.h"

/*
nsTest4 is defined in multiple file
*/
namespace nsTest4{
	int func4(XXX xx){
		return func1(xx.getYY());
	}
	int func5(::XXX xx){
		return func1(xx.getXX());
	}	
}


