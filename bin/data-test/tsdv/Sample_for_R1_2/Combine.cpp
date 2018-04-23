
struct Simple1{
	int data;
};

int ArrayOfStructTest(Simple1 list[]){
	if (list[0].data > 0)
		return 1;
	else
		return 0;
}

struct StructWArray{
	int list[2];
};

int StructOfArrayTest(StructWArray s){
	if (s.list[0] > 0 && s.list[1] > 0)
		return 1;
	else
		return 0;
}

int PointerOfStructTest(Simple1* list){
	if (list->data > 0)
		return 1;
	else
		return 0;
}

struct StructWPointer{
	int* list;
};

int StructOfPointerTest(StructWPointer s){
	if (*s.list > 0)
		return 1;
	else
		return 0;
}

class ClassWithStruct{
private:
	Simple1 myStruct;
	
public:
	void setMyStruct(const Simple1& s1){
		this->myStruct = s1;
	}
	
	Simple1 getMyStruct() const {
		return this->myStruct;
	}
};

int ClassOfStructTest(ClassWithStruct c){
	if (c.getMyStruct().data > 0)
		return 1;
	else
		return 0;
}

class ClassWithPointer{
private:
	int* myPointer;
	
public:
	void setMyPointer(int* p){
		this->myPointer = p;
	}
	
	int* getMyPointer() const {
		return this->myPointer;
	}
};

int ClassOfPointerTest(ClassWithPointer c){
	if (c.getMyPointer()[0] > 1)
		return 1;
	else
		return 0;
}

