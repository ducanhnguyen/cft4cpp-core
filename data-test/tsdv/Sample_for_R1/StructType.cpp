
struct Student{
	int age;
	double mark;
};

int SimpleTest(Student s){
	if (s.age > 10)
		return 1;
	else
		return 0;
}

int SimpleTestWithKeyword(struct Student s){
	if (s.age > 10)
		return 1;
	else
		return 0;
}

struct Class {
	int size;

	Student s1, s2;
};

int NestedStructTest(Class c1, Class c2){
	if (c1.size < c2.size)
		return -1;

	if (c1.s1.age == c2.s2.age)
		return 0;

	return 1;
}

