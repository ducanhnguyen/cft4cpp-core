#include <iostream>
#include<cstdlib>

using namespace std;
struct Node{
	int head;
	Node *N;
	Node(){
		N = NULL;
	}
};


int TestNullPoint(){
	Node *node;
	if (node->N == NULL){
		return 1;
	}
	return 0;
}

int main(){
	int a = 1;
	int b = 2;
	
	cout << TestNullPoint << endl;
	system("pause");
	return 0;
}
