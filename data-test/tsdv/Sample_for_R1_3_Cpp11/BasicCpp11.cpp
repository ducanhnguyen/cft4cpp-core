
int AutoKwTest(int x){
	auto x_alias = x;
	
	if (x_alias > 0)
		return 1;
	else
		return 0;
}

int NullPtrTest(int *x){
	// if (x == NULL)    : NULL is just define as 0 (int)
	if (x == nullptr)    // nullptr is not int ("int abc = nullptr;" is error) , it is a pointer value
		return -1;
	else
		return *x;
}

//---------------------------VNU add---------------------------
int AutoKwTest2(int x){
	auto x_alias = x*(-1.1);

	if (x_alias > 0)
		return 1;
	else
		return 0;
}

int AutoKwTest3(int x){
	auto x_alias = new int[2];
	x_alias[0] = x;
	if (x_alias[0] > 0)
		return 1;
	else
		return 0;
}
