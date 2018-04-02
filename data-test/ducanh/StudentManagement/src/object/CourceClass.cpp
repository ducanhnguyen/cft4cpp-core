/*
 * CourceClass.cpp
 *
 *  Created on: Jan 8, 2018
 *      Author: Duc Anh Nguyen
 */

#include "CourceClass.h"

CourceClass::CourceClass() {

}

CourceClass::~CourceClass() {
}

list<Student*> CourceClass::findStudent(string studentInfo, int searchType) {
	list<Student*> searchedStudents;
	list<Student*> currentStudents = getStudents();

	for (std::list<Student*>::iterator item = currentStudents.begin();
			item != currentStudents.end(); item++) {
		switch (searchType) {
		case SEARCH_BY_NAME:
			if (((*item)->getStudentName()).compare(studentInfo) == 0) {
				searchedStudents.push_back(*item);
			}
			break;

		case SEARCH_BY_STUDENT_ID:
			if (((*item)->getStudentId()).compare(studentInfo) == 0) {
				searchedStudents.push_back(*item);
			}
			break;
		}
	}
	return searchedStudents;
}
