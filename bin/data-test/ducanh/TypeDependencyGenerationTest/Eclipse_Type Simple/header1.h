/*
 * typedefine.h
 *
 *  Created on: May 20, 2016
 *      Author: DucAnh
 */

#ifndef HEADER1_H
#define HEADER1_H

#include "Accommodation.h"
#include <vector>
typedef float float_type;

//Đại diện nơi ở là một căn nhà
class House: public Accommodation {
private:
	int nPerson;
	const House* c;
public:
	void setX();
	void setNPerson(int n) {
		nPerson = n;
	}
};

// Cửa hàng cá nhân là nhà riêng
class IndividualStore: public House {
private:
	struct Fruit {
		char* name; //Apple, Orange, etc.
		int price;
		int num, kkkk;
	};

	std::vector<Fruit> fruits;
public:
	typedef Fruit hoaqua;

	// Kiểu biến nằm trong cùng class
	void addFruit(hoaqua newFruit) {
		//s
	}

	// Kiểu biến nằm trong class thừa kế
	void displayAddress(
			IndividualStore::House::Accommodation::information infor) {
	}

	// Kiểu biến nằm trong class thừa kế
	void displayAddress2(House::Accommodation::information infor) {
	}
} mystore;
struct ChainStore {

};

//-------------Sample struct thua ke struct
struct C {

};
struct PP: C {

};
#endif /* TYPEDEFINE_H_ */
