#ifndef TEST_H_
#define TEST_H_

typedef  int int16_t;

struct Point {
    int16_t x;
    int16_t y;
    int16_t z;
}; 

bool flag = false;
Point gp;
int16_t global_variable_test(Point p); 

#endif /*TEST_H_*/
