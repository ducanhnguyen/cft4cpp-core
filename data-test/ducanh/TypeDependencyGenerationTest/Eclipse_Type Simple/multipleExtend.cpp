class F {
public:
	struct sinhvien {
		int age;
	} sv;
	int f;
};
class G {
	int g;
};
class D: public F, public G {
	int d;
};
class C {
	int c;
};
class A: public C, public D {
	int a;
};
class E {
	int e;
};
class B: public E {
	int b;
};
class Final: public A, public B {
	int final;//
};
void testMultipleExtends(Final::A::D::F::sinhvien y) {

}
void testMultipleExtends(Final y) {

}
// 5 duong
// FInal A C
// Final A D F
// Final A D G
// Final B E
