#include <stdio.h>

/* User defined types */
// 1 enum, 2 typedef, 4 primitive, 1 struct, 3 function
enum deptcode {sales,personnel,packing,engineering};//field

typedef enum deptcode DEPT;//field

struct person {
                 int age, salary;
                 DEPT department;//lack
                 char name[12];
                 char address[6][20];
              };

typedef struct person EMPLOYEE;//lack

void read_line(char Str[]) {
    int i = 0;   char next;
    while ((next=getchar())!='\n') {
     Str[i] = next;
     i++;
    }
    Str[i] = 0;    /* Set the null char at the end */
}

void print_employee(EMPLOYEE Emp) {
    int i;
    printf(" %d %d %d\n",Emp.age,Emp.salary,Emp.department);
    printf("%s\n",Emp.name);
    for (i=0;i<=5;i++) printf("%s\n",Emp.address[i]);
}

void main () {
    EMPLOYEE This_Employee;
    int i;
    scanf("%d",&This_Employee.age);
    scanf("%d",&This_Employee.salary);
    scanf("%d\n",&This_Employee.department);
    read_line(This_Employee.name);
    for (i=0; i<=5; i++) read_line(This_Employee.address[i]);
    print_employee(This_Employee);
}