CPP      = g++
CC       = gcc
OBJ      = Search.o main.o Merge.o Sort.o Utils.o
LINKOBJ  = Search.o main.o Merge.o Sort.o Utils.o
BIN      = Algorithm.exe
CXXFLAGS = ''
CFLAGS   = $(INCS) 
RM       = rm.exe -f

.PHONY: all all-before all-after clean clean-custom

all: all-before $(BIN) all-after

clean: clean-custom
	${RM} $(OBJ) $(BIN)

$(BIN): $(OBJ)
	$(CPP) $(LINKOBJ) -o $(BIN) $(LIBS)

Search.o: Search.cpp
	$(CPP) -c Search.cpp -o Search.o

main.o: main.cpp
	$(CPP) -c main.cpp -o main.o

Merge.o: Merge.cpp
	$(CPP) -c Merge.cpp -o Merge.o

Sort.o: Sort.cpp
	$(CPP) -c Sort.cpp -o Sort.o

Utils.o: Utils.cpp
	$(CPP) -c Utils.cpp -o Utils.o

