#include "Node.h"
#ifndef SORT_H_
#define SORT_H_

namespace Algorithm {
namespace Sort {
void selectionSort(int arr[], int n);

void bubbleSort1(int arr[], int n);

void bubbleSort2(int arr[], int n);

void insertionSort(int arr[], int n);

void quickSort(int arr[], int low, int high);

void quickSortIterative(int arr[], int l, int h);

struct Node *quickSortRecur(struct Node *head, struct Node *end);

void merge(int arr[], int l, int m, int r);

void mergeSort(int arr[], int l, int r);

}
}
#endif /* SORT_H_ */

