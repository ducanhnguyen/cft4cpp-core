struct BigData{
	char data[10];
	int size;
};

int PassAsConstRefTest(const BigData& b){
	int sum = 0;
	for (int i = 0; i < b.size; i++)
		sum += b.data[i];
	return sum;
}

int PassAsConstValueTest(const BigData b){
	int sum = 0;
	for (int i = 0; i < b.size; i++)
		sum += b.data[i];
	return sum;
}

int PassAsRefTest(BigData& b){
	int sum = 0;
	for (int i = 0; i < b.size; i++)
		sum += b.data[i];
	return sum;
}

int PassAsValueTest(BigData b){
	int sum = 0;
	for (int i = 0; i < b.size; i++)
		sum += b.data[i];
	return sum;
}
