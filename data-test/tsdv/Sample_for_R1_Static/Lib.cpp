
// Header file
#include "Lib.h"

int findMin(int a[], int n){
	if (n <= 0)
		throw std::exception();
	
	int min = a[0];
	
	for (int i = 1; i < n; i++)
		if (a[i] < min)
			min = a[i];
	
	return min;
}
