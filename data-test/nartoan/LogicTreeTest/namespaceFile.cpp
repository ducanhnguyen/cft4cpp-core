#include "iostream"
namespace M {
	struct i {};
}
namespace N {
	static int i = 1;
}
using M::i;
using N::i;

int main() {
	std::cout << sizeof(i);
}
