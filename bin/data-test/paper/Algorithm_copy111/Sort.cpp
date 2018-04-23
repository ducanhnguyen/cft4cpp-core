#include <stdio.h>
#include <math.h>
#include<stdlib.h>
#include <iostream>
#include <cstdio>

#include "Sort.h"
using namespace std;

namespace Algorithm {
namespace Sort {
/*
 * UTILITIES
 */
void swap(int *xp, int *yp) {
	int temp = *xp;
	*xp = *yp;
	*yp = temp;
}

/**
 * SELECTION SORT
 *
 * http://www.geeksforgeeks.org/selection-sort/
 */
void selectionSort(int arr[], int n) {
	int i, j, min_idx;

	// One by one move boundary of unsorted subarray
	for (i = 0; i < n - 1; i++) {
		// Find the minimum element in unsorted array
		min_idx = i;
		for (j = i + 1; j < n; j++)
			if (arr[j] < arr[min_idx])
				min_idx = j;

		// Swap the found minimum element with the first element
		swap(&arr[min_idx], &arr[i]);
	}
}

/**
 * BUBBLE SORT 1
 *
 * http://www.geeksforgeeks.org/bubble-sort/
 */
void bubbleSort1(int arr[], int n) {
	int i, j;
	for (i = 0; i < n - 1; i++)

		// Last i elements are already in place
		for (j = 0; j < n - i - 1; j++)
			if (arr[j] > arr[j + 1])
				swap(&arr[j], &arr[j + 1]);
}

/**
 * BUBBLE SORT 2 (An optimized version of Bubble Sort 1)
 *
 * http://www.geeksforgeeks.org/bubble-sort/
 */
void bubbleSort2(int arr[], int n) {
	int i, j;
	bool swapped;
	for (i = 0; i < n - 1; i++) {
		swapped = false;
		for (j = 0; j < n - i - 1; j++) {
			if (arr[j] > arr[j + 1]) {
				swap(&arr[j], &arr[j + 1]);
				swapped = true;
			}
		}

		// IF no two elements were swapped by inner loop, then break
		if (swapped == false)
			break;
	}
}

/**
 * INSERTION SORT
 *
 * http://www.geeksforgeeks.org/insertion-sort/
 */
void insertionSort(int arr[], int n) {
	int i, key, j;
	for (i = 1; i < n; i++) {
		key = arr[i];
		j = i - 1;

		/* Move elements of arr[0..i-1], that are
		 greater than key, to one position ahead
		 of their current position */
		while (j >= 0 && arr[j] > key) {
			arr[j + 1] = arr[j];
			j = j - 1;
		}
		arr[j + 1] = key;
	}
}

/**
 * QUICK SORT
 *
 * http://www.geeksforgeeks.org/quick-sort/
 *
 * This function takes last element as pivot, places the pivot element at its correct position in sorted array, and places all smaller (smaller than pivot) to left of pivot and all greater elements to right of pivot
 */
int partition(int arr[], int low, int high) {
	int pivot = arr[high];    // pivot
	int i = (low - 1);  // Index of smaller element

	for (int j = low; j <= high - 1; j++) {
		// If current element is smaller than or
		// equal to pivot
		if (arr[j] <= pivot) {
			i++;    // increment index of smaller element
			swap(&arr[i], &arr[j]);
		}
	}
	swap(&arr[i + 1], &arr[high]);
	return (i + 1);
}

//The main function that implements QuickSort arr[] --> Array to be sorted, low --> Starting index, high --> Ending index
void quickSort(int arr[], int low, int high) {
	if (low < high) {
		/* pi is partitioning index, arr[p] is now
		 at right place */
		int pi = partition(arr, low, high);

		// Separately sort elements before
		// partition and after partition
		int x = pi - 1;
		quickSort(arr, low, x);
		
		int y = pi + 1;
		quickSort(arr, y, high);
	}
}

/**
 * ITERATIVE QUICK SORT
 *
 * http://www.geeksforgeeks.org/iterative-quick-sort/
 * A[] --> Array to be sorted; l  --> Starting index, h  --> Ending index
 */
void quickSortIterative(int arr[], int l, int h) {
	// Create an auxiliary stack
	int stack[h - l + 1];

	// initialize top of stack
	int top = -1;

	// push initial values of l and h to stack
	top++;
	stack[top] = l;
	top++;
	stack[top] = h;

	// Keep popping from stack while is not empty
	while (top >= 0) {
		// Pop h and l
		h = stack[top];
		top--;
		l = stack[top];
		top--;

		// Set pivot element at its correct position
		// in sorted array
		int p = partition(arr, l, h);

		// If there are elements on left side of pivot,
		// then push left side to stack
		if (p - 1 > l) {
			top++;
			stack[top] = l;
			top++;
			stack[top] = p - 1;
		}

		// If there are elements on right side of pivot,
		// then push right side to stack
		if (p + 1 < h) {
			top++;
			stack[top] = p + 1;
			top++;
			stack[top] = h;
		}
	}
}

/**
 *  QUICK SORT ON SINGLY LINKED LIST
 *  http://www.geeksforgeeks.org/quicksort-on-singly-linked-list/
 */

// A utility function to insert a node at the beginning of linked list
void push(struct Node** head_ref, int new_data) {
	/* allocate node */
	struct Node* new_node = new Node;

	/* put in the data  */
	new_node->data = new_data;

	/* link the old list off the new node */
	new_node->next = (*head_ref);

	/* move the head to point to the new node */
	(*head_ref) = new_node;
}

// A utility function to print linked list
void printList(struct Node *node) {
	while (node != NULL) {
		printf("%d  ", node->data);
		node = node->next;
	}
	printf("\n");
}

// Returns the last node of the list
struct Node *getTail(struct Node *cur) {
	while (cur != NULL && cur->next != NULL)
		cur = cur->next;
	return cur;
}

// Partitions the list taking the last element as the pivot
struct Node *partition(struct Node *head, struct Node *end,
		struct Node **newHead, struct Node **newEnd) {
	struct Node *pivot = end;
	struct Node *prev = NULL, *cur = head, *tail = pivot;

	// During partition, both the head and end of the list might change
	// which is updated in the newHead and newEnd variables
	while (cur != pivot) {
		if (cur->data < pivot->data) {
			// First node that has a value less than the pivot - becomes
			// the new head
			if ((*newHead) == NULL)
				(*newHead) = cur;

			prev = cur;
			cur = cur->next;
		} else { // If cur node is greater than pivot
			// Move cur node to next of tail, and change tail
			if (prev)
				prev->next = cur->next;
			struct Node *tmp = cur->next;
			cur->next = NULL;
			tail->next = cur;
			tail = cur;
			cur = tmp;
		}
	}

	// If the pivot data is the smallest element in the current list,
	// pivot becomes the head
	if ((*newHead) == NULL)
		(*newHead) = pivot;

	// Update newEnd to the current last node
	(*newEnd) = tail;

	// Return the pivot node
	return pivot;
}

// here the sorting happens exclusive of the end node
struct Node *quickSortRecur(struct Node *head, struct Node *end) {
	// base condition
	if (!head || head == end)
		return head;

	Node *newHead = NULL, *newEnd = NULL;

	// Partition the list, newHead and newEnd will be updated
	// by the partition function
	struct Node *pivot = partition(head, end, &newHead, &newEnd);

	// If pivot is the smallest element - no need to recur for
	// the left part.
	if (newHead != pivot) {
		// Set the node before the pivot node as NULL
		struct Node *tmp = newHead;
		while (tmp->next != pivot)
			tmp = tmp->next;
		tmp->next = NULL;

		// Recur for the list before pivot
		newHead = quickSortRecur(newHead, tmp);

		// Change next of last node of the left half to pivot
		tmp = getTail(newHead);
		tmp->next = pivot;
	}

	// Recur for the list after the pivot element
	pivot->next = quickSortRecur(pivot->next, newEnd);

	return newHead;
}

// The main function for quick sort. This is a wrapper over recursive
// function quickSortRecur()
void quickSort(struct Node **headRef) {
	(*headRef) = quickSortRecur(*headRef, getTail(*headRef));
	return;
}

/**
 *  MERGE SORT
 *
 * http://www.geeksforgeeks.org/merge-sort/
 *
 * Merges two subarrays of arr[]. First subarray is arr[l..m]. Second subarray is arr[m+1..r]
 */
void merge(int arr[], int l, int m, int r) {
	int i, j, k;
	int n1 = m - l + 1;
	int n2 = r - m;

	/* create temp arrays */
	int L[n1], R[n2];

	/* Copy data to temp arrays L[] and R[] */
	for (i = 0; i < n1; i++)
		L[i] = arr[l + i];
	for (j = 0; j < n2; j++)
		R[j] = arr[m + 1 + j];

	/* Merge the temp arrays back into arr[l..r]*/
	i = 0; // Initial index of first subarray
	j = 0; // Initial index of second subarray
	k = l; // Initial index of merged subarray
	while (i < n1 && j < n2) {
		if (L[i] <= R[j]) {
			arr[k] = L[i];
			i++;
		} else {
			arr[k] = R[j];
			j++;
		}
		k++;
	}

	/* Copy the remaining elements of L[], if there
	 are any */
	while (i < n1) {
		arr[k] = L[i];
		i++;
		k++;
	}

	/* Copy the remaining elements of R[], if there
	 are any */
	while (j < n2) {
		arr[k] = R[j];
		j++;
		k++;
	}
}

// l is for left index and r is right index of the  sub-array of arr to be sorted
void mergeSort(int arr[], int l, int r) {
	if (l < r) {
		// Same as (l+r)/2, but avoids overflow for
		// large l and h
		int m = l + (r - l) / 2;

		// Sort first and second halves
		mergeSort(arr, l, m);
		mergeSort(arr, m + 1, r);

		merge(arr, l, m, r);
	}
}
}
}

