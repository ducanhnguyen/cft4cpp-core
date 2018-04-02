int testIfCondition1(int a, int b ) {
	if (a > 2 && b <4) {
		a++;
	} else {
		b--;
	}

	return a + b;
}


int testIfCondition3(int a, int b, int c) {
	if( ((a > 2 &&  b < 4) || c > 3) ) {
		return 1;
	}

	return 0;
}


int testIfCondition4(int a, int b, int c) {
	if(a > 2 || b < 4 || c > 3) {
		return 1;
	}

	return 0;
}

int testIfCondition5(bool x, bool y) {
	if(x && y) {
		return 1;
	}

	return 0;
}
