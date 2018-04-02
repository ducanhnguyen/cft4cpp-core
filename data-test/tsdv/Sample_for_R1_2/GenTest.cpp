
#include <exception>
#include <cstddef> //NULL define here

int Abs(int x){
	if (x < 0)
		x = -x;
	return x;
}

struct Inner {
	int y;
};
	
struct Outer {
	int x;
	Inner y;
};

int NestedTest(Outer x){
	if (x.x > 0 && x.y.y > 0)
		return 1;
	else
		return 0;
}

int Divide(int x, int y){
	if (y == 0)
		throw std::exception();
	
	return x / y;
}

int PointerTest(Outer* p){
	//if (p == nullptr) //C++ 11
	if (p == NULL)
		return -1;
	if (p->x > 1)
		return 1;
	else
		return 0;
}

struct Node {
	int data;
	Node* next = NULL;
};

int LinkedNodeTest(Node* head){
	if (head == NULL)
		return 0;
		
	int count = 1;
	while (head->next != NULL){
		count++;
		head = head->next;
	}
	
	return count;
}
