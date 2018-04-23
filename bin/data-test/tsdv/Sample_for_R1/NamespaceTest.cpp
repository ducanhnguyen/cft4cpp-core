



struct X{
	int global;
};

namespace ns1 {

	struct X{
		int level1;
	};

	namespace ns2{

		struct X{
			int level2;
		};

		struct Y{
			float level2;
		};

		int Level2SimpleTest(X x){
			if (x.level2 > 0)
				return 1;
			else
				return 0;
		}

		int Level2MultipleNsTest(::X x0, ::ns1::X x1, X x2){
			if (x0.global > 0 && x1.level1 > 0 && x2.level2 > 0)
				return true;
			else
				return false;
		}
	}

	int Level1SimpleTest(X x){
		if (x.level1 > 0)
			return 1;
		else
			return 0;
	}

	int Level1MultipleNsTest(::X x0, X x1, ns2::X x2){
		if (x0.global > 0 && x1.level1 > 0 && x2.level2 > 0)
			return true;
		else
			return false;
	}
}

int Level0MultipleNsTest(X x0, ns1::X x1, ns1::ns2::X x2){
	if (x0.global > 0 && x1.level1 > 0 && x2.level2 > 0)
		return true;
	else
		return false;
}


namespace {
	int FunctionInAnnonymousNsTest(int x){
		if (x > 0)
			return 1;
		else
			return 0;
	}
}

int Level0UsingNsTest(int x){
	using namespace ns1::ns2;

	Y y;
	y.level2 = x + 1;

	if (y.level2 > 0)
		return 1;
	else
		return 0;
}

using namespace ns1::ns2;
int Level0UsingAutoNsTest(Y y){
	if (y.level2 > 0)
		return 1;
	else
		return 0;
}

