#include "gtest/gtest.h"
#include "C:/Users/DucToan/Documents/GitHub/ava/Source code/CppProjectTesting/data-test/ducanh/SymbolicExecutionTest/advanced_test.cpp"
TEST(xoa_dau_cach_thua1, testpath1) {
	char s[11];s[0] = ' ';s[1] = '$';s[2] = '3';s[3] = '6';s[4] = 'H';s[5] = '4';s[6] = '0';s[7] = '3';s[8] = 'i';s[9] = '1';
	
	char EO_s[] = "tran duc toan";

	xoa_dau_cach_thua(s);
EXPECT_EQ(EO_s,s);

}

TEST(xoa_dau_cach_thua1, testpath2) {
	char s[11];s[0] = ' ';s[1] = 'e';s[2] = 'S';s[3] = 'y';s[4] = 'U';s[5] = '%';s[6] = '=';s[7] = '2';s[8] = 'J';s[9] = 'u';
	
	char EO_s[] = "Nguyen Thi Hanh";

	xoa_dau_cach_thua(s);
EXPECT_EQ(EO_s,s);

}

TEST(xoa_dau_cach_thua1, testpath3) {
	char s[11];s[0] = ' ';s[1] = '6';s[2] = '%';s[3] = 'Z';s[4] = 39;s[5] = 'G';s[6] = 'q';s[7] = 'u';s[8] = 'M';s[9] = 'o';
	
	char EO_s[] = "Nguyen Tuan anh";

	xoa_dau_cach_thua(s);
EXPECT_EQ(EO_s,s);

}

TEST(xoa_dau_cach_thua1, testpath4) {
	char s[11];s[0] = ' ';s[1] = 'Y';s[2] = 'L';s[3] = 'c';s[4] = 'b';s[5] = 'F';s[6] = 'd';s[7] = '0';s[8] = 34;s[9] = 'p';
	
	char EO_s[] = "Tran Duc Phuc";

	xoa_dau_cach_thua(s);
EXPECT_EQ(EO_s,s);

}

TEST(xoa_dau_cach_thua1, testpath5) {
	char s[11];s[0] = 1;s[1] = '$';s[2] = 'F';s[3] = '{';s[4] = '{';s[5] = '`';s[6] = '^';s[7] = 'h';s[8] = '2';s[9] = 34;
	
	char EO_s[] = "Nguyen Van A";

	xoa_dau_cach_thua(s);
EXPECT_EQ(EO_s,s);

}

TEST(xoa_dau_cach_thua1, testpath6) {
	char s[11];s[0] = 1;s[1] = '<';s[2] = 'E';s[3] = 'Q';s[4] = '_';s[5] = 'p';s[6] = 'h';s[7] = 'M';s[8] = '1';s[9] = '8';
	
	char EO_s[] = "ưerwewe";

	xoa_dau_cach_thua(s);
EXPECT_EQ(EO_s,s);

}

TEST(xoa_dau_cach_thua1, testpath7) {
	char s[11];s[0] = 1;s[1] = 'g';s[2] = '!';s[3] = '2';s[4] = '@';s[5] = 'G';s[6] = ',';s[7] = 'b';s[8] = '(';s[9] = 'j';
	
	char EO_s[] = "đáasd";

	xoa_dau_cach_thua(s);
EXPECT_EQ(EO_s,s);

}

TEST(xoa_dau_cach_thua1, testpath8) {
	char s[11];s[0] = 1;s[1] = '6';s[2] = 'G';s[3] = 'o';s[4] = 'D';s[5] = 't';s[6] = 'f';s[7] = 'Y';s[8] = '3';s[9] = '#';
	
	char EO_s[] = "adasdaasd";

	xoa_dau_cach_thua(s);
EXPECT_EQ(EO_s,s);

}



