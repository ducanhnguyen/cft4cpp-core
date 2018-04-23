
#include "Header.h"

int mmin3_extern(int x, int y, int z){
	int r = mmin(x, y);
	
	if (r < z)
		return r;
	else
		return z;
}
