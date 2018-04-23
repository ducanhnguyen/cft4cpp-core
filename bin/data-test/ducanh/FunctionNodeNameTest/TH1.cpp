/**
 * Trường hợp tham số truyền vào là tên ảo (tên được định nghĩa qua typedef).
 */
#include "math.h"
#include "vector"
#include "header1.h"

typedef int int_type;

//TH 1.1. Định nghĩa typedef đặt trong cùng một tệp
void increment(int_type n) {
	// do something here
}

//TH 1.2. Định nghĩa typedef đặt trong tệp header khác
float_type getSQRT(float_type n) {
	return sqrt(n);
}

//TH 1.3. Định nghĩa typedef đặt trong class
void getInforOfApple(IndividualStore::hoaqua t) {
	// do something here
}

//TH 2.1. vector
void listAllApples(std::vector<IndividualStore::hoaqua> fruits) {
	// do something here
}
//TH 3.1. Con trỏ một mức
void getInforOfApples(IndividualStore::hoaqua* t) {
	// do something here
}

//TH 3.2. Con trỏ nhiều mức
void getInforOfApples(IndividualStore::hoaqua** t) {
	// do something here
}

//TH 3.3. Địa chỉ
void getInforOfApples(IndividualStore::hoaqua& t) {
	// do something here
}

//TH 4.1. Mảng một chiều
void getInforOfApples2(IndividualStore::hoaqua t[]) {
	// do something here
}

//TH 4.2. Mảng nhiều chiều
void getInforOfApples2(IndividualStore::hoaqua t[][5]) {
	// do something here
}

//TH5. Kiểu biến là kiểu struct
void getInforOfChainStore(ChainStore c) {
	// do something here
}

//TH6.1. Kiểu biến là kiểu class định nghĩa độc lập bên ngoài
void getInforOfChainStore(IndividualStore c) {
	// do something here
}

//TH6.1. Kiểu biến là kiểu class định nghĩa trong namespace khác
namespace ns1{
	class C1{

	};
}
void getInforOfChainStore(ns1::C1 c) {
	// do something here
}

int main() {
	return 0;
}
