#include <iostream>
#include <cstring>
#include <iomanip>
#include <cstdlib> 
using namespace std;
 
struct DAT
{
    int id;
    char fname[20];
    char mname[20];
    char lname[20];
    char address[80];
    double salary;
    char tele_no[15];
};
 
struct Node
{
  	DAT data;
 	Node *N;
  	Node *P;
  	Node(const int i , const char *f, const char *m, const char *l, const char *ad, const double s, const char *tel)
    {
       	data.id = i;
       	strcpy(data.fname,f);
       	strcpy(data.mname,m);
       	strcpy(data.lname,l);
       	strcpy(data.address,ad);
       	data.salary = s;
       	strcpy(data.tele_no,tel);
       	N = NULL;
       	P = NULL;
  }
  Node(){}
};
 
class StackLinkedList
{
  	private:
  		Node *front;
  	public:
	  	Node* getFront(){return front;}
		void setFront(Node* front_){front=front_;}
		
	  	StackLinkedList(){front = NULL;}
	  	~StackLinkedList(){destroyList();}
	  	void push(Node *);
	  	Node* pop1();
	  	Node* pop2();
	  	void destroyList();
};
void StackLinkedList::push(Node *n)
{
    if(front == NULL)
    {
        front = n;
    }
    else
    {cout <<"1";
        front->P = n;
        n->N = front;
        front = n;
    }
}
 
Node* StackLinkedList::pop1()
{
    Node *temp;
    if(front == NULL )//no nodes
        return NULL;
    else if(front->N != NULL)//there is only one node
    {
        Node * temp2 = front;
            temp = temp2;
            front = NULL;
            delete temp2;
            return temp;
    }
   
}
Node* StackLinkedList::pop2()
{
    Node *temp;
    if(front == NULL )//no nodes
        return NULL;
    else if(front->N == NULL)//there is only one node
    {
        Node * temp2 = front;
            temp = temp2;
            front = NULL;
            delete temp2;
            return temp;
    }else{
    	int a = 0;
	}
   
}
void StackLinkedList::destroyList()
{
   while(front != NULL)
   {
       Node *temp = front;
       front = front->N;
       delete temp;
   }
}
 
void disp(Node *N)
{
    if( N == NULL )
    {
        cout << "\nStack is Empty!!!" << endl;
    }
    else
    {
        cout << "\nId No.     : " << N->data.id <<" ";
        cout << "\nFull Name  : " << N->data.fname << " ";
        cout <<  N->data.mname << " ";
        cout <<  N->data.lname << endl;
        cout << "Address    : " << N->data.address << endl;
        cout << "Salary     : " << setprecision(15)  << N->data.salary << endl;
        cout << "Tele_no    : " << N->data.tele_no<< endl << endl;
    }
}
 
int main()
{
	Node* front = NULL;StackLinkedList __call;__call.setFront(front);__call.destroyList();
    system("pause");
    return 0;
}
