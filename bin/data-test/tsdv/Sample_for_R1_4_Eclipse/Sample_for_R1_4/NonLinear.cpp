int PlusTest(int x, int y){
	//int z = y + x;
	
	if (321 + y + x *1234> 1000)
		return 1;
	else
		return 0;
}


int PlusTest2(int x, int y){
	//int z = y + x;
	
	if (321 + y * x *1234> 1000)
		return 1;
	else
		return 0;
}

int PlusTest3(int x, int y){

	int z = y * x * x;

	if (z > 100)
		return 1;
	else
		return 0;
}


int PlusTest4(int x, int y){

	if (y * x * x * y  > 100)
		return 1;
	else
		return 0;
}


int PlusTest5(int x, int y){

	if (y * x * x * y *y > 100)
		return 1;
	else
		return 0;
}



int getFare(int age, int distance)
{
	int fare;
	if(age >= 4 && age <= 14)
	{
		if(distance > 10)	
			fare = 130;
		else fare = 100;
	}
	if(age >= 15)
	{
		if(distance <10 && age >= 60)
			fare = 160;
		else if(distance > 10 && age < 60)
			fare = 250;
			else fare = 200;
	}
	return fare;
}
