#include <iostream>
using namespace std;
// 1 enum, 1 function
enum seasons { spring, summer, autumn, winter };

int main() {

    seasons s;
    s = autumn;   // Correct
    s = rainy;    // Error
    return 0;
}