
// Define Marco
#define MY_DEFAULT_VALUE 10
#define ADD(a, b) ((a) + (b))

#define COMPILE_UNDER_WINDOWS
#ifdef COMPILE_AS_64BIT
	#define INT long
#else
	#define INT int
#endif


int SimpleMarcoTest(int x){
	if (x == MY_DEFAULT_VALUE)
		return 1;
	else
		return 0;
}

int MarcoAsFunctionTest(int x, int y){
	int z = ADD(x, y);
	
	if (z > 0)
		return 1;
	else
		return 0;
}

int IfDefTest(int x){
	
	// This code still valid and can be compile because COMPILE_UNDER_WINDOWS is defined above, even if code in #else is invalid
#ifdef COMPILE_UNDER_WINDOWS
	x = x + 10;
#else
	// These code will be ignored by preprocessor, so an invalid source code (under Windows) does not make sense
	
	// Function not avaiable on Windows
	x = x - 10;
#endif
	
	if (x > 0)
		return 1;
	else
		return 0;
}

int MarcoAsTypeTest(INT x){
	if (x > 0)
		return 1;
	else
		return 0;
}
