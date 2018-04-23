
enum Color {
	RED,
	GREEN,
	BLUE
};

int SimpleTest(Color color){
	if (color == RED)
		return 0;
	else if (color == GREEN)
		return 1;
	else if (color == BLUE)
		return 2;
	else
		return -1;
}



int SimpleTest1(Color color){
	if (color == RED)
		return 0;
	else
		return 1;
}

int SimpleTest2(Color color){
	if (color == RED)
		return 0;
	else if (color == GREEN)
		return 1;
	else
		return 2;
}

enum Animal {
	Dog,
	Cat = 20,
};
int SimpleTest3(Animal a){
	if (a == Dog)
		return 0;
	else if (a == Cat)
		return 1;
	else
		return 2;
}

