
class Class1 {
public:
	int x;
	float y;
};

int SimpleTest(Class1 c){
	if (c.x > 0)
		return 1;
	else
		return 0;
}

class Class2 {
private:
	int x;
public:
	void setX(int x){
		this->x = x;
	}
	int getX() const {
		return x;
	}

	int getXEx() const {
		return getX() + 50;
	}

	int SimpleMethodTest(){
		if (x > 0)
			return 1;
		else
			return 0;
	}
};

int SimpleTest2(Class2 c){
	if (c.getX() > 0)
		return 1;
	else
		return 0;
}

int SimpleTest3(Class2 c){
	if (c.getXEx() > 0)
		return 1;
	else
		return 0;
}

