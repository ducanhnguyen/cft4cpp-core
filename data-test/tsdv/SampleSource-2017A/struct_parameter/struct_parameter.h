#ifndef TEST_H_
#define TEST_H_


typedef int					size_t2;
typedef unsigned int		BIG_INTEGER;
typedef unsigned long		ULONG;

#define TRUE 	1
#define FALSE	0

typedef struct _INFO {
    ULONG   Size;
} INFO, *INFO;


typedef struct _COMPLEX_STRUCT {         
    INFO           Info;               
} COMPLEX_STRUCT;

#endif /* TEST_H_*/
