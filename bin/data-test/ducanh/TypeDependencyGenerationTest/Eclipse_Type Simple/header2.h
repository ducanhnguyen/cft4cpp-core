/*
 * header2.h
 *
 *  Created on: May 21, 2016
 *      Author: DucAnh
 */

#ifndef HEADER2_H_
#define HEADER2_H_

// Hai class lồng nhau có hai method y hệt nhau
class A1 {
	struct customStruct {

	};
};
class A: public A1 {
	struct customStruct {

	};
	void testMethod(customStruct x) {
	}
	class B {
		void testMethod(A1::customStruct x) {
		}
	} b;
} a;

#endif /* HEADER2_H_ */
