#include <iostream>
using namespace std;

/** 

Parameter:
 - b_w: weight
 - he: height

Return list
 - UNDERWEIGHT: 0
 - Perfect Shape: 1
 - OVERWEIGHT: 2
 - A VICTIM OF OBESITY: 3
 - Out of believeable: -1
 
*/
int bmi(float b_w, float he)
{
	double c;
	c = (b_w / (he * he / 10000));
	
	{
		if (c < 19) {
			return 0;
		}

		else if (c >= 19 && c < 25) {
			return 1;

		}

		else if (c >= 25 && c < 30) {
			return 2;
		}

		else if (c >= 30) {
			return 3;

		} else {
			// (infeasible)
			return -1;
		}
	}
}

struct Date{
	int date;
	int month;
	int year;
};

Date calculateAge(Date born, Date today)
;

int calculateZodiac(Date born){

	int date1 = born.date;
	int month1 = born.month;
	int year1 = born.year;	
	
	int t;
	
	{

		if (((month1 == 3) && (date1 >= 21) && (date1 <= 31)) || ((month1 == 4) && (date1 <= 19)))

		{
			cout << "\n\n\t\t\tYour Zodiac sign is ARIES";
			cout << "\n\n 2012 would be a good year overall. You will experience a rise in financial luck and inflows";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 1;
		}

		else if (((month1 == 4) && (date1 >= 20) && (date1 <= 30)) || ((month1 == 5) && (date1 <= 20))) {
			cout << "\n\n\t\tYour Zodiac sign is TAURUS";
			cout << "\n\n A very eventful year. Career would be on a high growth trajectory and bring in major progress and achievements.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 2;
		}

		else if (((month1 == 5) && (date1 >= 21) && (date1 <= 31)) || ((month1 == 6) && (date1 <= 20))) {
			cout << "\n\n\t\tYour Zodiac sign is GEMINI";
			cout << "\n\nA very positive year. Income & professional growth would be immense. You will find the ability to make some very profitable deals now.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 3;
		}

		else if (((month1 == 6) && (date1 >= 21) && (date1 <= 30)) || ((month1 == 7) && (date1 <= 22))) {
			cout << "\n\n\t\tYour Zodiac sign is CANCER";
			cout << "\n\nA very eventful year, although negative thoughts and unnecessary pessimism could spoil your spirit and bring in unnecessary tension.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 4;
		}

		else if (((month1 == 7) && (date1 >= 23) && (date1 <= 31)) || ((month1 == 8) && (date1 <= 22))) {
			cout << "\n\n\t\tYour Zodiac sign is LEO";
			cout << "\n\nAn exceptional year again. You will see a rise in status and expansion in career this year too. Luck will favor you throughout.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 5;
		}

		else if (((month1 == 8) && (date1 >= 23) && (date1 <= 31)) || ((month1 == 9) && (date1 <= 22))) {
			cout << "\n\n\t\tYour Zodiac sign is VIRGO";
			cout << "\n\nA brilliant & positive year, where you will make things happen on your own strength, rather than seeking support of others.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 6;
		}

		else if (((month1 == 9) && (date1 >= 23) && (date1 <= 30)) || ((month1 == 10) && (date1 <= 22))) {
			cout << "\n\n\t\tYour Zodiac sign is LIBRA";
			cout << "\n\nA powerful phase will be in operation this month. You will find your role as defined by nature will change and all efforts and activities carried out by you will assume higher importance and effectiveness.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 7;
		}

		else if (((month1 == 10) && (date1 >= 23) && (date1 <= 31)) || ((month1 == 11) && (date1 <= 21))) {
			cout << "\n\n\t\tYour Zodiac sign is SCORPIO";
			cout << "\n\nPositive period would continue, although you need to be careful that throwing good money after bad money is not a great idea.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 8;
		}

		else if (((month1 == 11) && (date1 >= 22) && (date1 <= 31)) || ((month1 == 12) && (date1 <= 21))) {
			cout << "\n\n\t\tYour Zodiac sign is SAGITTARIUS";
			cout << "\n\n2012 brings in promise and progress. New ideas and cerebral approach to matters will bring in much progress this year. You will be at your creative best till May 2012 and thereafter dynamic activity will take you along.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 9;
		}

		else if (((month1 == 12) && (date1 >= 22) && (date1 <= 30)) || ((month1 == 1) && (date1 <= 19))) {
			cout << "\n\n\t\tYour Zodiac sign is CAPRICORN";
			cout << "\n\nA very positive year for you. You would be at your creative best and luck related peak in most of the works you get into.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 10;
		}

		else if (((month1 == 1) && (date1 >= 20) && (date1 <= 31)) || ((month1 == 2) && (date1 <= 18))) {
			cout << "\n\n\t\tYour Zodiac sign is AQUARIUS";
			cout << "\n\nA much better year in comparison to 2010 & 2011. You will feel a surge in your luck, productivity and general sense of positive outlook.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 11;
		}

		else if (((month1 == 2) && (date1 >= 19) && (date1 <= 29)) || ((month1 == 3) && (date1 <= 20))) {
			cout << "\n\n\t\tYour Zodiac sign is PISCES";
			cout << "\n\nSome amount of struggle and hurdles could come about in life this year. You will have a positive and gainful period till May 2012.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 12;
		}

		else {
			t = -1;
		}

	}
	
	return t;
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
Date findStructureDateByName(string nameStructure) {
	Date structure;
	structure.date = findValueByName<int>(nameStructure+".date");
	structure.month = findValueByName<int>(nameStructure+".month");
	structure.year = findValueByName<int>(nameStructure+".year");


	return structure;
}
Date findStructureDateByName(string nameStructure) {
	Date structure;
	structure.date = findValueByName<int>(nameStructure+".date");
	structure.month = findValueByName<int>(nameStructure+".month");
	structure.year = findValueByName<int>(nameStructure+".year");


	return structure;
}

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
}string build = "";bool mark(string append){build += append + "\n";writeContentToFile("E:/workspace/java/cft4cpp-core/data-test/tsdv/Sample_for_R1_4_copy/0testdriver_execution.txt", build);return true;}Date calculateAge(::Date born,::Date today){mark("statement={###line-of-blockin-function=2###openning-function=true");
	mark("line-in-function=3###offset=50###statement=int x, y, z;");int x, y, z;

	mark("line-in-function=4###offset=65###statement=Date result;");Date result;

	mark("line-in-function=6###offset=82###statement=int date = today.date;");int date = today.date;

	mark("line-in-function=7###offset=107###statement=int month = today.month;");int month = today.month;

	mark("line-in-function=8###offset=134###statement=int year = today.year;");int year = today.year;

	mark("line-in-function=10###offset=161###statement=int date1 = born.date;");int date1 = born.date;

	mark("line-in-function=11###offset=186###statement=int month1 = born.month;");int month1 = born.month;

	mark("line-in-function=12###offset=213###statement=int year1 = born.year;");int year1 = born.year;

	mark("line-in-function=14###offset=241###statement=x = ((year) - (year1 + 1));");x = ((year) - (year1 + 1));

	{mark("statement={###line-of-blockin-function=15");
		if (mark("line-in-function=16###offset=280###statement=(month >= month1) && (date >= date1)###control-block=if") && ((month >= month1) && (date >= date1))) {mark("statement={###line-of-blockin-function=16");
						{mark("statement={###line-of-blockin-function=17");
				mark("line-in-function=18###offset=331###statement=y = (month - month1);");y = (month - month1);

				mark("line-in-function=19###offset=358###statement=x = x + 1;");x = x + 1;

				mark("line-in-function=20###offset=374###statement=z = (date - date1);");z = (date - date1);

			mark("statement=}###line-of-blockin-function=17");}

		mark("statement=}###line-of-blockin-function=16");}
		else {
						if (mark("line-in-function=22###offset=414###statement=((month > month1) && (date < date1)) && ((month == 5)  || (month == 7) || (month == 10)  || (month == 12))###control-block=if") && (((month > month1) && (date < date1)) && ((month == 5)  || (month == 7) || (month == 10)  || (month == 12)))) {mark("statement={###line-of-blockin-function=22");
								{mark("statement={###line-of-blockin-function=23");
					mark("line-in-function=24###offset=535###statement=y = ((month - month1) - 1);");y = ((month - month1) - 1);

					mark("line-in-function=25###offset=568###statement=x = x + 1;");x = x + 1;

					mark("line-in-function=26###offset=584###statement=z = ((30 - date1) + date);");z = ((30 - date1) + date);

				mark("statement=}###line-of-blockin-function=23");}

			mark("statement=}###line-of-blockin-function=22");}
			else {
								if (mark("line-in-function=28###offset=631###statement=((month > month1) && (date < date1)) && ((month == 4) || (month == 1) || (month == 2) || (month == 6) || (month == 8) || (month == 9) || (month == 11))###control-block=if") && (((month > month1) && (date < date1)) && ((month == 4) || (month == 1) || (month == 2) || (month == 6) || (month == 8) || (month == 9) || (month == 11)))) {mark("statement={###line-of-blockin-function=28");
										{mark("statement={###line-of-blockin-function=29");
						mark("line-in-function=30###offset=797###statement=y = ((month - month1) - 1);");y = ((month - month1) - 1);

						mark("line-in-function=31###offset=830###statement=x = x + 1;");x = x + 1;

						mark("line-in-function=32###offset=846###statement=z = ((31 - date1) + date);");z = ((31 - date1) + date);

					mark("statement=}###line-of-blockin-function=29");}

				mark("statement=}###line-of-blockin-function=28");}
				else {
										if (mark("line-in-function=36###offset=898###statement=(month > month1) && (date < date1) && (month == 3)###control-block=if") && ((month > month1) && (date < date1) && (month == 3))) {mark("statement={###line-of-blockin-function=36");
												{mark("statement={###line-of-blockin-function=37");
							mark("line-in-function=38###offset=963###statement=y = ((month - month1) - 1);");y = ((month - month1) - 1);

							mark("line-in-function=39###offset=996###statement=x = x + 1;");x = x + 1;

							mark("line-in-function=40###offset=1012###statement=z = ((28 - date1) + date);");z = ((28 - date1) + date);

						mark("statement=}###line-of-blockin-function=37");}

					mark("statement=}###line-of-blockin-function=36");}
					else {
												if (mark("line-in-function=44###offset=1064###statement=((month == month1) && (date < date1)) && ((month == 1) || (month == 2) || (month == 4)  || (month == 6) || (month == 8) || (month == 9) || (month == 11))###control-block=if") && (((month == month1) && (date < date1)) && ((month == 1) || (month == 2) || (month == 4)  || (month == 6) || (month == 8) || (month == 9) || (month == 11)))) {mark("statement={###line-of-blockin-function=44");
														{mark("statement={###line-of-blockin-function=45");
								mark("line-in-function=46###offset=1232###statement=y = 11;");y = 11;

								mark("line-in-function=47###offset=1245###statement=z = ((31 - date1) + date);");z = ((31 - date1) + date);

							mark("statement=}###line-of-blockin-function=45");}

						mark("statement=}###line-of-blockin-function=44");}
						else {
														if (mark("line-in-function=51###offset=1297###statement=((month == month1) && (date < date1)) && ((month == 5) || (month == 7) || (month == 10)  || (month == 12))###control-block=if") && (((month == month1) && (date < date1)) && ((month == 5) || (month == 7) || (month == 10)  || (month == 12)))) {mark("statement={###line-of-blockin-function=51");
																{mark("statement={###line-of-blockin-function=52");
									mark("line-in-function=53###offset=1418###statement=z = ((30 - date1) + date);");z = ((30 - date1) + date);

								mark("statement=}###line-of-blockin-function=52");}

							mark("statement=}###line-of-blockin-function=51");}
							else {
																if (mark("line-in-function=57###offset=1470###statement=month == month1 && date < date1 && month == 3###control-block=if") && (month == month1 && date < date1 && month == 3)) {mark("statement={###line-of-blockin-function=57");
																		{mark("statement={###line-of-blockin-function=58");
										mark("line-in-function=59###offset=1530###statement=z = ((28 - date1) + date);");z = ((28 - date1) + date);

									mark("statement=}###line-of-blockin-function=58");}

								mark("statement=}###line-of-blockin-function=57");}
								else {
																		if (mark("line-in-function=63###offset=1582###statement=((month < month1) && (date > date1)) && ((month == 1) || (month == 3) || (month == 5)  || (month == 7) || (month == 8) || (month == 10) || (month == 12))###control-block=if") && (((month < month1) && (date > date1)) && ((month == 1) || (month == 3) || (month == 5)  || (month == 7) || (month == 8) || (month == 10) || (month == 12)))) {mark("statement={###line-of-blockin-function=63");
																				{mark("statement={###line-of-blockin-function=64");
											mark("line-in-function=65###offset=1750###statement=y = ((12 - month1) + month);");y = ((12 - month1) + month);

											mark("line-in-function=66###offset=1784###statement=z = date - date1;");z = date - date1;

										mark("statement=}###line-of-blockin-function=64");}

									mark("statement=}###line-of-blockin-function=63");}
									else {
																				if (mark("line-in-function=70###offset=1827###statement=((month < month1) && (date > date1)) && ((month == 4) || (month == 6) || (month == 9)  || (month == 11))###control-block=if") && (((month < month1) && (date > date1)) && ((month == 4) || (month == 6) || (month == 9)  || (month == 11)))) {mark("statement={###line-of-blockin-function=70");
																						{mark("statement={###line-of-blockin-function=71");
												mark("line-in-function=72###offset=1946###statement=y = ((12 - month1) + month);");y = ((12 - month1) + month);

												mark("line-in-function=73###offset=1980###statement=z = date - date1;");z = date - date1;

											mark("statement=}###line-of-blockin-function=71");}

										mark("statement=}###line-of-blockin-function=70");}
										else {
																						if (mark("line-in-function=77###offset=2023###statement=(month < month1) && (date > date1) && (month == 2)###control-block=if") && ((month < month1) && (date > date1) && (month == 2))) {mark("statement={###line-of-blockin-function=77");
																								{mark("statement={###line-of-blockin-function=78");
													mark("line-in-function=79###offset=2088###statement=y = ((12 - month1) + month);");y = ((12 - month1) + month);

													mark("line-in-function=80###offset=2122###statement=z = date - date1;");z = date - date1;

												mark("statement=}###line-of-blockin-function=78");}

											mark("statement=}###line-of-blockin-function=77");}
											else {
																								if (mark("line-in-function=84###offset=2165###statement=((month < month1) && (date < date1)) && ((month == 1) || (month == 2) || (month == 4)  || (month == 6) || (month == 8) || (month == 9) || (month == 11))###control-block=if") && (((month < month1) && (date < date1)) && ((month == 1) || (month == 2) || (month == 4)  || (month == 6) || (month == 8) || (month == 9) || (month == 11)))) {mark("statement={###line-of-blockin-function=84");
																										{mark("statement={###line-of-blockin-function=85");
														mark("line-in-function=86###offset=2332###statement=y = ((12 - month1) + month - 1);");y = ((12 - month1) + month - 1);

														mark("line-in-function=87###offset=2370###statement=z = ((31 - date1) + date);");z = ((31 - date1) + date);

													mark("statement=}###line-of-blockin-function=85");}

												mark("statement=}###line-of-blockin-function=84");}
												else {
																										if (mark("line-in-function=91###offset=2422###statement=((month < month1) && (date < date1)) && ((month == 5) || (month == 7) || (month == 10)  || (month == 12))###control-block=if") && (((month < month1) && (date < date1)) && ((month == 5) || (month == 7) || (month == 10)  || (month == 12)))) {mark("statement={###line-of-blockin-function=91");
																												{mark("statement={###line-of-blockin-function=92");
															mark("line-in-function=93###offset=2542###statement=y = ((12 - month1) + month - 1);");y = ((12 - month1) + month - 1);

															mark("line-in-function=94###offset=2580###statement=z = ((30 - date1) + date);");z = ((30 - date1) + date);

														mark("statement=}###line-of-blockin-function=92");}

													mark("statement=}###line-of-blockin-function=91");}
													else {
																												if (mark("line-in-function=96###offset=2627###statement=(month < month1) && (date < date1) && (month == 3)###control-block=if") && ((month < month1) && (date < date1) && (month == 3))) {mark("statement={###line-of-blockin-function=96");
																														{mark("statement={###line-of-blockin-function=97");
																mark("line-in-function=98###offset=2692###statement=y = ((12 - month1) + month - 1);");y = ((12 - month1) + month - 1);

																mark("line-in-function=99###offset=2730###statement=z = ((28 - date1) + date);");z = ((28 - date1) + date);

															mark("statement=}###line-of-blockin-function=97");}

														mark("statement=}###line-of-blockin-function=96");}
														else {mark("statement={###line-of-blockin-function=103");
																														mark("line-in-function=104###offset=2784###statement=x = y = z = -1;");x = y = z = -1;

														mark("statement=}###line-of-blockin-function=103");}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

	mark("statement=}###line-of-blockin-function=15");}

	mark("line-in-function=108###offset=2813###statement=result.date = z;");result.date = z;

	mark("line-in-function=109###offset=2832###statement=result.month = y;");result.month = y;

	mark("line-in-function=110###offset=2852###statement=result.year = x;");result.year = x;

	mark("line-in-function=112###offset=2874###statement=return result;");return result;

mark("statement=}###line-of-blockin-function=2");}int main(){try{using namespace utils;Elements data = readContentFromFile("E:/workspace/java/cft4cpp-core/data-test/tsdv/Sample_for_R1_4_copy/input.txt");Date born=data.findStructureDateByName("born");Date today=data.findStructureDateByName("today");calculateAge(born,today); }catch(exception& error){build="Phat hien loi runtime";}return 0;}
