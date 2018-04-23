int testLoop1(int a, int b ) {
	while (a > 2 && b <4)  {
		a++;
		b++;
	}

	return a + b;
}

int testLoop2(int a) {
	for(int i = 0; a > 2 && i < 100; i++)  {
		a++;
	}

	return a;
}

int testLoop3(int a, int b) {
	do  {
		a++;
		b++;
	} while(a > 2 && b < 4);

	return a + b;
}

