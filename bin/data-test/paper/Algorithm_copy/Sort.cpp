#include <stdio.h>
#include <math.h>
#include<stdlib.h>
#include <iostream>
#include <cstdio>

#include "Sort.h"
using namespace std;

namespace Algorithm {
namespace Sort {
/*
 * UTILITIES
 */
void swap(int *xp, int *yp) {
	int temp = *xp;
	*xp = *yp;
	*yp = temp;
}

/**
 * SELECTION SORT
 *
 * http://www.geeksforgeeks.org/selection-sort/
 */
void selectionSort(int arr[], int n) {
	int i, j, min_idx;

	// One by one move boundary of unsorted subarray
	for (i = 0; i < n - 1; i++) {
		// Find the minimum element in unsorted array
		min_idx = i;
		for (j = i + 1; j < n; j++)
			if (arr[j] < arr[min_idx])
				min_idx = j;

		// Swap the found minimum element with the first element
		swap(&arr[min_idx], &arr[i]);
	}
}

/**
 * BUBBLE SORT 1
 *
 * http://www.geeksforgeeks.org/bubble-sort/
 */
void bubbleSort1(int arr[], int n) {
	int i, j;
	for (i = 0; i < n - 1; i++)

		// Last i elements are already in place
		for (j = 0; j < n - i - 1; j++)
			if (arr[j] > arr[j + 1])
				swap(&arr[j], &arr[j + 1]);
}

/**
 * BUBBLE SORT 2 (An optimized version of Bubble Sort 1)
 *
 * http://www.geeksforgeeks.org/bubble-sort/
 */
void bubbleSort2(int arr[], int n) ;

/**
 * INSERTION SORT
 *
 * http://www.geeksforgeeks.org/insertion-sort/
 */
void insertionSort(int arr[], int n) {
	int i, key, j;
	for (i = 1; i < n; i++) {
		key = arr[i];
		j = i - 1;

		/* Move elements of arr[0..i-1], that are
		 greater than key, to one position ahead
		 of their current position */
		while (j >= 0 && arr[j] > key) {
			arr[j + 1] = arr[j];
			j = j - 1;
		}
		arr[j + 1] = key;
	}
}

/**
 * QUICK SORT
 *
 * http://www.geeksforgeeks.org/quick-sort/
 *
 * This function takes last element as pivot, places the pivot element at its correct position in sorted array, and places all smaller (smaller than pivot) to left of pivot and all greater elements to right of pivot
 */
int partition(int arr[], int low, int high) {
	int pivot = arr[high];    // pivot
	int i = (low - 1);  // Index of smaller element

	for (int j = low; j <= high - 1; j++) {
		// If current element is smaller than or
		// equal to pivot
		if (arr[j] <= pivot) {
			i++;    // increment index of smaller element
			swap(&arr[i], &arr[j]);
		}
	}
	swap(&arr[i + 1], &arr[high]);
	return (i + 1);
}

//The main function that implements QuickSort arr[] --> Array to be sorted, low --> Starting index, high --> Ending index
void quickSort(int arr[], int low, int high) {
	if (low < high) {
		/* pi is partitioning index, arr[p] is now
		 at right place */
		int pi = partition(arr, low, high);

		// Separately sort elements before
		// partition and after partition
		int x = pi - 1;
		quickSort(arr, low, x);
		
		int y = pi + 1;
		quickSort(arr, y, high);
	}
}

/**
 * ITERATIVE QUICK SORT
 *
 * http://www.geeksforgeeks.org/iterative-quick-sort/
 * A[] --> Array to be sorted; l  --> Starting index, h  --> Ending index
 */
void quickSortIterative(int arr[], int l, int h) {
	// Create an auxiliary stack
	int stack[h - l + 1];

	// initialize top of stack
	int top = -1;

	// push initial values of l and h to stack
	top++;
	stack[top] = l;
	top++;
	stack[top] = h;

	// Keep popping from stack while is not empty
	while (top >= 0) {
		// Pop h and l
		h = stack[top];
		top--;
		l = stack[top];
		top--;

		// Set pivot element at its correct position
		// in sorted array
		int p = partition(arr, l, h);

		// If there are elements on left side of pivot,
		// then push left side to stack
		if (p - 1 > l) {
			top++;
			stack[top] = l;
			top++;
			stack[top] = p - 1;
		}

		// If there are elements on right side of pivot,
		// then push right side to stack
		if (p + 1 < h) {
			top++;
			stack[top] = p + 1;
			top++;
			stack[top] = h;
		}
	}
}

/**
 *  QUICK SORT ON SINGLY LINKED LIST
 *  http://www.geeksforgeeks.org/quicksort-on-singly-linked-list/
 */

// A utility function to insert a node at the beginning of linked list
void push(struct Node** head_ref, int new_data) {
	/* allocate node */
	struct Node* new_node = new Node;

	/* put in the data  */
	new_node->data = new_data;

	/* link the old list off the new node */
	new_node->next = (*head_ref);

	/* move the head to point to the new node */
	(*head_ref) = new_node;
}

// A utility function to print linked list
void printList(struct Node *node) {
	while (node != NULL) {
		printf("%d  ", node->data);
		node = node->next;
	}
	printf("\n");
}

// Returns the last node of the list
struct Node *getTail(struct Node *cur) {
	while (cur != NULL && cur->next != NULL)
		cur = cur->next;
	return cur;
}

// Partitions the list taking the last element as the pivot
struct Node *partition(struct Node *head, struct Node *end,
		struct Node **newHead, struct Node **newEnd) {
	struct Node *pivot = end;
	struct Node *prev = NULL, *cur = head, *tail = pivot;

	// During partition, both the head and end of the list might change
	// which is updated in the newHead and newEnd variables
	while (cur != pivot) {
		if (cur->data < pivot->data) {
			// First node that has a value less than the pivot - becomes
			// the new head
			if ((*newHead) == NULL)
				(*newHead) = cur;

			prev = cur;
			cur = cur->next;
		} else { // If cur node is greater than pivot
			// Move cur node to next of tail, and change tail
			if (prev)
				prev->next = cur->next;
			struct Node *tmp = cur->next;
			cur->next = NULL;
			tail->next = cur;
			tail = cur;
			cur = tmp;
		}
	}

	// If the pivot data is the smallest element in the current list,
	// pivot becomes the head
	if ((*newHead) == NULL)
		(*newHead) = pivot;

	// Update newEnd to the current last node
	(*newEnd) = tail;

	// Return the pivot node
	return pivot;
}

// here the sorting happens exclusive of the end node
struct Node *quickSortRecur(struct Node *head, struct Node *end) {
	// base condition
	if (!head || head == end)
		return head;

	Node *newHead = NULL, *newEnd = NULL;

	// Partition the list, newHead and newEnd will be updated
	// by the partition function
	struct Node *pivot = partition(head, end, &newHead, &newEnd);

	// If pivot is the smallest element - no need to recur for
	// the left part.
	if (newHead != pivot) {
		// Set the node before the pivot node as NULL
		struct Node *tmp = newHead;
		while (tmp->next != pivot)
			tmp = tmp->next;
		tmp->next = NULL;

		// Recur for the list before pivot
		newHead = quickSortRecur(newHead, tmp);

		// Change next of last node of the left half to pivot
		tmp = getTail(newHead);
		tmp->next = pivot;
	}

	// Recur for the list after the pivot element
	pivot->next = quickSortRecur(pivot->next, newEnd);

	return newHead;
}

// The main function for quick sort. This is a wrapper over recursive
// function quickSortRecur()
void quickSort(struct Node **headRef) {
	(*headRef) = quickSortRecur(*headRef, getTail(*headRef));
	return;
}

/**
 *  MERGE SORT
 *
 * http://www.geeksforgeeks.org/merge-sort/
 *
 * Merges two subarrays of arr[]. First subarray is arr[l..m]. Second subarray is arr[m+1..r]
 */
void merge(int arr[], int l, int m, int r) {
	int i, j, k;
	int n1 = m - l + 1;
	int n2 = r - m;

	/* create temp arrays */
	int L[n1], R[n2];

	/* Copy data to temp arrays L[] and R[] */
	for (i = 0; i < n1; i++)
		L[i] = arr[l + i];
	for (j = 0; j < n2; j++)
		R[j] = arr[m + 1 + j];

	/* Merge the temp arrays back into arr[l..r]*/
	i = 0; // Initial index of first subarray
	j = 0; // Initial index of second subarray
	k = l; // Initial index of merged subarray
	while (i < n1 && j < n2) {
		if (L[i] <= R[j]) {
			arr[k] = L[i];
			i++;
		} else {
			arr[k] = R[j];
			j++;
		}
		k++;
	}

	/* Copy the remaining elements of L[], if there
	 are any */
	while (i < n1) {
		arr[k] = L[i];
		i++;
		k++;
	}

	/* Copy the remaining elements of R[], if there
	 are any */
	while (j < n2) {
		arr[k] = R[j];
		j++;
		k++;
	}
}

// l is for left index and r is right index of the  sub-array of arr to be sorted
void mergeSort(int arr[], int l, int r) {
	if (l < r) {
		// Same as (l+r)/2, but avoids overflow for
		// large l and h
		int m = l + (r - l) / 2;

		// Sort first and second halves
		mergeSort(arr, l, m);
		mergeSort(arr, m + 1, r);

		merge(arr, l, m, r);
	}
}
}
}



//-------------------------------
// Generate automatically
//-------------------------------

#include <iostream>
#include <fstream>
#include <cstring>
#include <cstddef>
#include <string>
#include <vector>
#include <typeinfo>
using namespace std;

/**
 * CONFIGURATION
 */
// Count the number of recursive call
static int NUM_RECURSIVE = 0; // by default

// The maximum recursive calls of a function, e.g., Fibonaxi
static int MAX_RECURSIVE = 10; // by default

// The maximum iteration for loop
static int MAX_LOOP_ITERATIONS = 1000; // by default
static int iteration[100] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

static int DEFAULT_VALUE_FOR_NUMBER = 0; // by default
static int DEFAULT_VALUE_FOR_CHARACTER = 58; // by default

/**
 * END CONFIGURATION
 */

namespace utils {
char* convertStringToCharPointer(string original) {
	char * S = new char[original.length() + 1];
	strcpy(S, original.c_str());
	return S;
}

float division(int length) {
	float division = 1;
	for (int i = 0; i < length; i++)
		division /= 10;
	return division;
}

template<class myType>
myType toInteger2(char* str) {
	myType mult = 1;
	myType re = 0;

	int len = strlen(str);
	for (int i = len - 1; i >= 0; i--) {
		re = re + ((int) str[i] - 48) * mult;
		mult = mult * 10;
	}
	return re;
}
char* integer_to_string(int x) {
	char* buffer = new char[10];
	sprintf(buffer, "%d", x);
	return buffer;
}
template<class myType>
myType toInteger(char* str) {
	myType mult = 1;
	myType re = 0;

	string clone = str;
	string beforePoint = "";
	string afterPoint = "";
	if (clone.find(".")) {
		beforePoint = clone.substr(0, clone.find_first_of("."));
		afterPoint = clone.substr(clone.find_first_of(".") + 1, clone.length());
	} else {
		afterPoint = "0";
		beforePoint = clone;
	}

	if (afterPoint.compare("0") == 0) {
		return toInteger2<myType>(convertStringToCharPointer(beforePoint));
	} else
		return toInteger2<myType>(convertStringToCharPointer(beforePoint))
				+ division(afterPoint.length())
						* toInteger2<myType>(
								convertStringToCharPointer(afterPoint));
}
}

using namespace utils;
/**
 * Represent a line in the input file
 */
class Element {
private:
	string leftSide;
	string rightSide;
public:

	string getLeftSide(string data) {
		return data.substr(0, data.find_last_of("="));
	}
	string getRightSide(string data) {
		return data.substr(data.find_last_of("=") + 1, data.length() - 1);
	}

	Element(string data) {
		leftSide = getLeftSide(data);
		rightSide = getRightSide(data);
	}
	string getRight() {
		return rightSide;
	}
	string getLeft() {
		return leftSide;
	}

	virtual string toString() {
		return "[Element] " + getLeft() + "=" + getRight();
	}

	bool equalLeftSide(string str) {
		return strcmp(leftSide.c_str(), str.c_str()) == 0;
	}
};

class OneDimensionIndexElement: public virtual Element {
public:
	OneDimensionIndexElement(string data) :
			Element(data) {
	}

	int getIndex() {
		int l = getLeft().find_last_of("[");
		int r = getLeft().find_last_of("]");
		string index = getLeft().substr(l + 1, r - l - 1);
		return toInteger<int>(convertStringToCharPointer(index));
	}

	string getNameElement() {
		return getLeft().substr(0, getLeft().find_last_of("["));
	}

	string toString() {
		return "[OneDimensionIndexElement] " + getLeft() + "=" + getRight();
	}
};

class StructureElement: public virtual Element {
public:
	StructureElement(string data) :
			Element(data) {
	}

	string toString() {
		return "[StructureElement] " + getLeft() + "=" + getRight();
	}
};

class BasicElement: public virtual Element {
public:
	BasicElement(string data) :
			Element(data) {
	}
	string toString() {
		return "[BasicElement] " + getLeft() + "=" + getRight();
	}
};

class SizeofElement: public virtual Element {
public:
	SizeofElement(string data) :
			Element(data) {
	}
	string getNameElement() {
		return getLeft().substr(getLeft().find_first_of("(") + 1,
				getLeft().find_first_of(")") - getLeft().find_first_of("(") - 1);
	}
	string toString() {
		return "[SizeofElement] " + getLeft() + "=" + getRight();
	}
};

class Elements {
private:
	vector<Element*> data;
public:
	vector<Element*>& getList() {
		return data;
	}

	void addElement(string str) {
		// If element e is represent in format of "A=B"
		if (str.find("=") != std::string::npos) {
			if (str.find("sizeof") != std::string::npos) {
				Element* e = new SizeofElement(str);
				data.push_back(e);

			} else if (str.find("[") != std::string::npos) {
				Element* e = new OneDimensionIndexElement(str);
				data.push_back(e);

			} else {
				Element* e = new BasicElement(str);
				data.push_back(e);
			}
		}
	}

	string toString() {
		string output = "data=\r";
		for (int i = 0; i < data.size(); i++) {
			output += "\t" + data[i]->toString() + "\n";
		}
		return output;
	}
	template<class myType>
	myType findValueByName(string nameElement) {
		for (int i = 0; i < data.size(); i++)
			if (dynamic_cast<BasicElement*>(data[i])) {
				if (data[i]->equalLeftSide(nameElement.c_str())) {
					cout << "Initialize " << nameElement << " to "
							<< data[i]->getRight() << endl;
					return utils::toInteger<myType>(
							utils::convertStringToCharPointer(
									data[i]->getRight()));
				}
			}
		return 0;
	}
	// TYPE: ONE DIMENSION, ONE LEVEL POINTER
	// Case 1: int*, int[], char*, char[], etc.
	template<class myType>
	myType* findOneDimensionOrLevelBasicByName(string nameElement,
			myType defaultValue) {
		cout << "Initialize " << nameElement << endl;
		myType* newVar = NULL;
		// Allocate size for the variable corresponding to the given name
		// Ex1: "sizeof(student.name)=10" (size=10)
		// Ex1: "sizeof(student.name)=-1" (size=-1, means NULL)
		bool foundNameElement = false;
		for (int i = 0; i < data.size(); i++)
			if (dynamic_cast<SizeofElement*>(data[i])) {

				string nameElementInSizeOf =
						(dynamic_cast<SizeofElement*>(data[i]))->getNameElement();
				if (nameElementInSizeOf.compare(nameElement) == 0) {
					int size =
							utils::toInteger<int>(
									convertStringToCharPointer(
											dynamic_cast<SizeofElement*>(data[i])->getRight()));
					// Case in Ex2
					if (size <= 0) {
						newVar = NULL;
						cout << "\tset to NULL" << endl;
					} else {
						// Case in Ex1
						cout << "\tsize=" << size << endl;
						newVar = new myType[size];

						for (int j = 0; j < size; j++)
							newVar[j] = defaultValue;
					}
					foundNameElement = true;
					break;
				} else
					continue;
			}

		// Assign value for each one of elements in the given one dimension array, or one level pointer
		if (newVar != NULL && foundNameElement)
			for (int i = 0; i < data.size(); i++)
				if (dynamic_cast<OneDimensionIndexElement*>(data[i])) {
					OneDimensionIndexElement* element =
							dynamic_cast<OneDimensionIndexElement*>(data[i]);
					string nameElementCast = element->getNameElement();
					if (nameElementCast.compare(nameElement) == 0) {
						cout << "\tassign " << nameElementCast << "["
								<< element->getIndex() << "] to "
								<< element->getRight() << endl;
						newVar[element->getIndex()] = utils::toInteger<myType>(
								convertStringToCharPointer(
										element->getRight()));
					}
				}
		return newVar;
	}
	// Case 1: People[], People*, Student[], Student*, etc.
	int RECENT_STRUCTURE_SIZE = 0;
	template<class myStructureType>
	myStructureType* findOneDimensionOrLevelStructureByName(
			string nameElement) {
		cout << "Initialize " << nameElement << endl;
		myStructureType* newVar = NULL;
		for (int i = 0; i < data.size(); i++)
			if (dynamic_cast<SizeofElement*>(data[i])) {
				string nameElementInSizeOf =
						(dynamic_cast<SizeofElement*>(data[i]))->getNameElement();
				if (nameElementInSizeOf.compare(nameElement) == 0) {
					int size =
							utils::toInteger<int>(
									convertStringToCharPointer(
											dynamic_cast<SizeofElement*>(data[i])->getRight()));
					if (size <= 0) {
						newVar = NULL;
						cout << "\tassign to NULL" << endl;
					} else {
						RECENT_STRUCTURE_SIZE = size;
						newVar = new myStructureType[size];
						cout << "\tsize=" << size << endl;
					}
					break;
				} else
					continue;
			}
		return newVar;
	}
	// TYPE: STRUCTURE

};

Elements readContentFromFile(const char* path) {
	Elements data;
	ifstream myReadFile;
	myReadFile.open(path);
	if (myReadFile.is_open()) {
		while (!myReadFile.eof()) {
			string dataItem = "";
			myReadFile >> dataItem;
			data.addElement(dataItem);
		}
	}
	myReadFile.close();
	return data;
}

void writeContentToFile(char* path, string content) {
	ofstream myfile;
	myfile.open(path);
	myfile << content;
	myfile.close();
}
// Avoiding-error checkers from here
bool mark(string);
#include <stdlib.h>
bool checkIndex(int index, string statement) {
	if (index < 0) {
		string negation = "!(" + statement + ")";
		mark(negation);
		exit(0);
	} else {
		return true;
	}
}string build = "";bool mark(string append){build += append + "\n";writeContentToFile("E:/workspace/java/cft4cpp-core/data-test/paper/Algorithm_copy/0testdriver_execution.txt", build);return true;}void Algorithm::Sort::bubbleSort2(int arr[],int n){mark("statement={###line-of-blockin-function=1###openning-function=true");
	mark("line-in-function=2###offset=55###statement=int i, j;");int i, j;

	mark("line-in-function=3###offset=67###statement=bool swapped;");bool swapped;

	mark("statement={###additional-code=true###surrounding-control-block=for");mark("line-in-function=4###offset=88###statement=i = 0;");for (i = 0;
			mark("line-in-function=4###offset=95###statement=i < n - 1") && i < n - 1;
			mark("line-in-function=4###offset=106###statement=i++") && i++) {mark("statement={###line-of-blockin-function=4");
				mark("line-in-function=5###offset=116###statement=swapped = false;");swapped = false;

		mark("statement={###additional-code=true###surrounding-control-block=for");mark("line-in-function=6###offset=141###statement=j = 0;");for (j = 0;
			mark("line-in-function=6###offset=148###statement=j < n - i - 1") && j < n - i - 1;
			mark("line-in-function=6###offset=163###statement=j++") && j++) {mark("statement={###line-of-blockin-function=6");
						if (mark("line-in-function=7###offset=178###statement=arr[j] > arr[j + 1]###control-block=if") && (arr[j] > arr[j + 1])) {mark("statement={###line-of-blockin-function=7");
								mark("line-in-function=8###offset=206###statement=swap(&arr[j], &arr[j + 1]);");swap(&arr[j], &arr[j + 1]);

				mark("line-in-function=9###offset=239###statement=swapped = true;");swapped = true;

			mark("statement=}###line-of-blockin-function=7");}

		mark("statement=}###line-of-blockin-function=6");}mark("statement=}###additional-code=true###surrounding-control-block=for");

		if (mark("line-in-function=14###offset=339###statement=swapped == false###control-block=if") && (swapped == false)) {
						mark("line-in-function=15###offset=361###statement=break;");break;
		}

	mark("statement=}###line-of-blockin-function=4");}mark("statement=}###additional-code=true###surrounding-control-block=for");

mark("statement=}###line-of-blockin-function=1");}int main(){try{using namespace utils;Elements data = readContentFromFile("E:/workspace/java/cft4cpp-core/data-test/paper/Algorithm_copy/input.txt");int* arr=data.findOneDimensionOrLevelBasicByName<int>("arr", DEFAULT_VALUE_FOR_NUMBER);int n=data.findValueByName<int>("n");using namespace Algorithm::Sort;Algorithm::Sort::bubbleSort2(arr,n); }catch(exception& error){build="Phat hien loi runtime";}return 0;}
