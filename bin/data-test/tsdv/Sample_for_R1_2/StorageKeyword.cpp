
/*
// Test with C++ 11 later
int AutoKeywordTest(int x){
	auto x_alias = x;
	
	if (x_alias > 0)
		return 1;
	else
		return 0;
}*/

// register keyword: may be deprecated but still avaiable
// Just ignore this keyword as it does not exist!!
int RegisterKeywordTest(int a[], int n){
	int s = 0;
	
	for (register int i = 0; i < n; i++)
		s += a[i];
		
	return s;
}

class MathUtils{
public:
	
	static int AddTest(int x, int y){
		int z = x + y;
		
		if (z > 0)
			return 1;
		else
			return 0;
	}
	
	static int MinusTest(int x, int y);
};

int MathUtils::MinusTest(int x, int y){
	int z = x - y;
		
		if (z > 0)
			return 1;
		else
			return 0;
}

extern int MY_MAX_VALUE; // Define in main.cpp
int ExternKeywordTest(int x){
	if (x == MY_MAX_VALUE)
		return 1;
	else
		return 0;
}

