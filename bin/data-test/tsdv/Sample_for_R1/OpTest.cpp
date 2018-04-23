



int PlusTest(int x, int y){
	int z = x + y;

	if (z > 0)
		return 1;
	else
		return 0;
}

int MinusTest(int x, int y){
	int z = x - y;

	if (z > 0)
		return 1;
	else
		return 0;
}

int MultiplyTest(int x, int y){
	int z = x * y;

	if (z > 0)
		return 1;
	else
		return 0;
}

int MultiplyTest2(int x, int y){


	if (x * y > 0)
		return 1;
	else
		return 0;
}

int DivideTest(int x, int y){
	int z = x / y;

	if (z > 0)
		return 1;
	else
		return 0;
}

int RemainderTest(int x, int y){
	int z = x / y;

	if (z > 0)
		return 1;
	else
		return 0;
}



int EqualTest(int x, int y){
	if (x == y)
		return 1;
	else
		return 0;
}

int GreaterOrEqualTest(int x, int y){
	if (x >= y)
		return 1;
	else
		return 0;
}

int LessThanOrEqualTest(int x, int y){
	if (x <= y)
		return 1;
	else
		return 0;
}

int GreaterTest(int x, int y){
	if (x > y)
		return 1;
	else
		return 0;
}

int LessThanTest(int x, int y){
	if (x < y)
		return 1;
	else
		return 0;
}

int NotEqualTest(int x, int y){
	if (x != y)
		return 1;
	else
		return 0;
}



int LogicAndTest(int x, int y){
	if (x > 0 && y > 0)
		return 1;
	else
		return 0;
}

int LogicOrTest(int x, int y){
	if (x > 0 || y > 0)
		return 1;
	else
		return 0;
}

int LogicNotTest(int x){
	if (!(x > 0))
		return 1;
	else
		return 0;
}



int AssignTest(int x, int y){
	int z = x;
	z = y;

	if (z > 0)
		return 1;
	else
		return 0;
}

int PlusAndAssignTest(int x, int y){
	int z = x;
	z += y;

	if (z > 0)
		return 1;
	else
		return 0;
}

int MinusAndAssignTest(int x, int y){
	int z = x;
	z -= y;

	if (z > 0)
		return 1;
	else
		return 0;
}

int MultiplyAndAssignTest(int x, int y){
	int z = x;
	z *= y;

	if (z > 0)
		return 1;
	else
		return 0;
}

int DivideAndAssignTest(int x, int y){
	int z = x;
	z /= y;

	if (z > 0)
		return 1;
	else
		return 0;
}

int RemainderAndAssignTest(int x, int y){
	int z = x;
	z %= y;

	if (z > 0)
		return 1;
	else
		return 0;
}

int NestedAssignTest(int x, int y, int z){
	int t = x;
	t = z = y;

	if (t > 0 && z > 0)
		return 1;
	else
		return 0;
}



int SizeOfTest(int x){
	int y = sizeof(int);
	if (x == y)
		return 1;
	else
		return 0;
}

int AddressAndPointerTest(int x, int y){

	int *px = &x, *py = &y;

	int z = *px;
	*px = *py;
	*py = z;

	if (x > y)
		return 1;
	else
		return 0;
}

int ConditionalTest(int x){
int t;if ( x > 0 ) t = -1 ; else t = 1;

	if (t > 0)
		return 1;
	else
		return 0;
}

