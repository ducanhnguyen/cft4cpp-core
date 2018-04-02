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
//add structure reader here
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
}