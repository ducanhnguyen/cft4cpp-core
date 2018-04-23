g++ -c main.cpp -o main.o
g++ -c qlsv.cpp -o qlsv.o
g++ -c test.cpp -o test.o
g++ qlsv.o main.o test.o -o Project1.exe