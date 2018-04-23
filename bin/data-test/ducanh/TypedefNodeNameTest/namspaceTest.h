#ifndef NAMESPACE_H
#define NAMESPACE_H
class OuterClass {

};

namespace ns1 {
class A1 {
private:
	int x;
public:
	int getX() {
		return x;
	}
};
}

namespace ns2 {
class A1 {
	int x;
};

class A2: public OuterClass {

};

class A3: public ns1::A1 {

};
void testInNs2(ns1::A1 x) {

}
void testInNs2_1(A1 x) {

}
void testInNs2_2(ns2::A2 y) {

}
}
#endif
