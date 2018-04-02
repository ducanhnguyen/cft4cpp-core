#include <iostream>
#include <fstream>
#include <string>
#include <windows.h>
#include <conio.h>
#include <iomanip>
#include "qlsv.h"

using namespace std;

TS::thisinh()
{
    strcpy(id,"");
    strcpy(ho,"");
    strcpy(ten,"");
    strcpy(tendem,"");
    gt = '0';
    strcpy(ns,"");
    strcpy(qq,"");
    toan = ly = hoa = tongdiem = tienganh = 0;
    head = next = last = NULL;
}

// xoa khoi xau S H ki tu bat dau tu ki tu thu K
void TS::StrDel(char s[],int k,int h)
{
    memmove(s+k,s+k+h,strlen(s)-k+1);
}

// chen xau S1 vao xau S tu vi tri POS
void TS::StrIns(char s[],int pos, char s1[])
{
    memmove(s+strlen(s1)+pos,s+pos,strlen(s)-pos+1);
    strncpy(s+pos,s1,strlen(s1));
}

// chen ki tu CH vao xau S tai vi tri POS
void TS::StrIns(char s[],int pos, char ch)
{
    memmove(s+1+pos,s+pos,strlen(s)-pos+1);
    s[pos] = ch;
}

void TS::xoa_dau_cach_thua(char s[]) //
{
    while (s[0] == ' ') StrDel(s, 0, 1);
    int i = 0;
    while( i<strlen(s))
    {
        if (s[i]==' ')
            while (s[i+1]==' ' & s[i+1]!='\0')
                StrDel(s, i+1, 1);
        i++;
    }
}

void TS::chuan_hoa(char* s)
{
    xoa_dau_cach_thua(s); 
    int i = 0;
	while (i <strlen(s))
    {
        if (s[i]==':' || s[i] ==';' || s[i]=='|')
        {
            if (s[i+1]==' ')StrDel(s, i+1, 1);
            if (s[i-1]==' ')StrDel(s, i-1, 1);
        }
        i++;
    }
    if (s[strlen(s)-1]==' ')	StrDel(s, strlen(s)-1, 1);
    for (int i = 0; i < strlen(s); i++)
        if (s[i]>='A' & s[i]<='z') s[i] = toupper(s[i]) + 32;
    for (int i = 0; i<strlen(s); i++)
        if (s[i]=='|' || s[i]==':' || s[i]==';' || s[i]==' ')
            if (s[i+1]>='a' & s[i+1]<='z')
                s[i+1] = s[i+1] - 32;
    for (int i = 0; i< strlen(s); i++)
    {
        if (s[i]=='|'|| s[i]==':' || s[i]==';') break;
        if(s[i]>='A' & s[i]<='z')s[i] = s[i]-32;
    }
}


void TS::gotoxy(short x, short y)
{
    HANDLE hCon = GetStdHandle(STD_OUTPUT_HANDLE);
    COORD pos;
    pos.X=x-1;
    pos.Y=y-1;
    SetConsoleCursorPosition(hCon, pos);
}

int TS::choose(int cot, int dong, int n) // n la tong so opitions
{
    int opi = 1;
    char ch;
    int icot = cot, idong = dong;
    // phan xu li, dung cac phim len xuong phai trai
    do
    {
        gotoxy(icot, idong);
        textcolor(9);
        cout <<char(178);//1
        ch = getch();
        if (ch==72) // phim mui ten len
        {
            gotoxy(icot, idong);
            cout <<" ";
            opi--;
            idong--;
            if(opi<=0) opi = n;
            if(idong<dong) idong = dong + n - 1;
        }
        if (ch==80) // phim mui ten xuong
        {
            gotoxy(icot, idong);
            cout <<" ";
            opi++;
            idong++;
            if(opi > n) opi = 1;
            if(idong > dong + n - 1) idong = dong;
        }

    }
    while (ch!=13); // ma ASCII Enter
    textcolor(11);
    return opi;
}

void TS::xoa_dong(int dong)
{
    gotoxy(1, dong);
    for (int i = 0; i < 80; i++) cout << " ";
}

void TS::xoa_dong(int a, int b)
{
    for (int i = a; i <= b; i++)
    {
        gotoxy(1, i);
        for (int j = 0; j < 80; j++) cout << " ";
    }
}

void TS::inScreen(TS* student, int opi)
{
    char ten[30]="\0";
    StrIns(ten,0,student->ten);
    StrIns(ten, 0, " ");
    StrIns(ten,0,student->tendem);
    StrIns(ten, 0, " ");
    StrIns(ten,0,student->ho);
    cout<<setprecision(1)<<fixed;
    if (opi==1)
    {

        textcolor(3);
        cout<<setw(10)<<student->id;
        textcolor(7);
        cout<< setw(24) <<left<<ten;
        textcolor(3);
        cout<< student->gt;
        textcolor(7);
        cout <<"   ";
        cout <<student->ns ;
        cout <<"  ";
        textcolor(3);
        cout<< setw(17) << student->qq;
        textcolor(7);
        cout <<" ";
        if (student->toan != 10) cout<< setw(4) << student->toan;
        else
        {
            cout<< setw(4) << "10";
        }
        cout <<" ";
        textcolor(3);
        if (student->ly != 10) cout<< setw(4) << student->ly;
        else
        {
            cout<< setw(4) << "10";
        }
        textcolor(7);
        cout <<"  ";
        if (student->hoa != 10) cout<< setw(4) << student->hoa;
        else
        {
            cout<< setw(4) << "10";
        }
        cout <<"  ";
        textcolor(3);
        cout <<endl;
    }
    if (opi==2)
    {
        textcolor(3);
        cout<<setw(9)<<student->id;
        textcolor(7);
        cout<< setw(25) <<left<<ten;
        textcolor(3);
        cout<< student->gt;
        textcolor(7);
        cout <<"   ";
        cout <<student->ns ;
        cout <<"  "; 
        textcolor(3);
        cout<< setw(14) << student->qq;
        textcolor(7);
        cout <<" ";
        if (student->toan != 10) cout<< setw(4) << student->toan;
        else
        {
            cout<< setw(4) << "10";
        }
        cout <<" ";
        textcolor(3);
        if (student->ly != 10) cout<< setw(4) << student->ly;
        else
        {
            cout<< setw(4) << "10";
        }
        textcolor(7);
        cout <<"  ";
        if (student->hoa != 10) cout<< setw(4) << student->hoa;
        else
        {
            cout<< setw(4) << "10";
        }
        cout <<"  ";
        textcolor(3);
        cout<<student->tienganh;
        cout <<endl;
    }
    textcolor(8);
}

void TS::xu_li(TS* student, char* s)
{
    char* pch;
    chuan_hoa(s);
    // ID
    pch = strtok(s,"|;:");
    strcpy(student->id, pch);
    // ten
    pch = strtok(NULL,"|:;");
    char* ten;
    ten = new char[40];
    strcpy(ten, pch);
    // cout << ten <<endl;
    int num = 1;
    for (int i = 0; i < strlen(ten); i++)
        if (ten[i]==' ')
            num++;
    switch (num)
    {
    case 2:
    {
        student->ten[0]='\0';
        student->tendem[0]='\0';
        student->ho[0]='\0';

        int i = 0;
        while (ten[i]!=' ')
        {
            StrIns(student->ho,strlen(student->ho), ten[i] );
            i++;
        }
        i++;
        while (ten[i]!='\0')
        {
            StrIns(student->ten,strlen(student->ten), ten[i] );
            i++;
        }
        break;
    }
    case 3:
    {
        student->ten[0]='\0';
        student->tendem[0]='\0';
        student->ho[0]='\0';

        int i = 0;
        while (ten[i]!=' ')
        {
            StrIns(student->ho,strlen(student->ho), ten[i] );
            i++;
        }
        i++;
        while (ten[i]!=' ')
        {
            StrIns(student->tendem,strlen(student->tendem), ten[i] );
            i++;
        }
        i++;
        while (ten[i]!='\0')
        {
            StrIns(student->ten,strlen(student->ten), ten[i] );
            i++;
        }
        break;
    }
    case 4:
    {
        student->ten[0]='\0';
        student->tendem[0]='\0';
        student->ho[0]='\0';

        int i = 0;
        while (ten[i]!=' ')
        {
            StrIns(student->ho,strlen(student->ho), ten[i] );
            i++;
        }
        i++;
        while (ten[i]!=' ')
        {
            StrIns(student->tendem,strlen(student->tendem), ten[i] );
            i++;
        }
        StrIns(student->tendem,strlen(student->tendem), " " );
        i++;
        while (ten[i]!=' ')
        {
            StrIns(student->tendem,strlen(student->tendem), ten[i] );
            i++;
        }
        i++;
        while (ten[i]!='\0')
        {
            StrIns(student->ten,strlen(student->ten), ten[i] );
            i++;
        }
        break;
    }
    };
    //gioi tinh
    pch = strtok(NULL,"|;:");
    student->gt = pch[0];

    //nam sinh
    pch = strtok(NULL,"|;:");
    strcpy(student->ns ,pch);

    // noi sinh

    pch = strtok(NULL,"|;:");
    strcpy(student->qq ,pch);

    // diem thi
    pch = strtok(NULL,"|;:");
    student->toan = atof(pch);
    pch = strtok(NULL,"|;:");
    student->ly = atof(pch);
    pch = strtok(NULL,"|;: ");
    student->hoa = atof(pch);
}

void TS::trao_doi(int& a, int& b)
{
    int tg;
    tg = a;
    a = b;
    b = tg;
}

void TS::trao_doi(char& a, char& b)
{
    char tg;
    tg = a;
    a = b;
    b = tg;
}

void TS::trao_doi(float& a, float& b)
{
    float tg;
    tg = a;
    a = b;
    b = tg;
}

void TS::trao_doi(char a[],char b[])
{

    char tg[20];
    strcpy( tg, a);
    strcpy(a, b);
    strcpy(b, tg);
}

void TS::trao_doi(TS* a,TS* b)
{
    trao_doi(a->id,b->id);
    trao_doi(a->ho, b->ho);
    trao_doi(a->tendem, b->tendem);
    trao_doi(a->ten, b->ten);
    trao_doi(a->gt, b->gt);
    trao_doi(a->ns, b->ns);
    trao_doi(a->qq, b->qq);
    trao_doi(a->toan, b->toan);
    trao_doi(a->ly, b->ly);
    trao_doi(a->hoa, b->hoa);
    trao_doi(a->kq, b->kq);
    trao_doi(a->tongdiem, b->tongdiem);
    trao_doi(a->tienganh, b->tienganh);
}

void TS::inFile(ofstream& fo, TS* student)
{
    fo
    << setprecision(1)<<fixed<<student->id << " | "
    << student->ho <<" " << student->tendem <<" " << student->ten <<" | "
    << student->gt <<" | " << student->ns << " | "<< student->qq <<" | "
    << student->toan << " | " << student->ly << " | "  << student->hoa
    <<endl;
}

void TS::inFileTiengAnh(ofstream& fo, TS* student)
{
    char ten[30]="\0";
    StrIns(ten,0,student->ten);
    StrIns(ten, 0, " ");
    StrIns(ten,0,student->tendem);
    StrIns(ten, 0, " ");
    StrIns(ten,0,student->ho);
    fo<<setprecision(1)<<fixed
    <<setw(9)<<student->id << " | "
    << setw(18) <<left<<ten << " | "
    <<student->gt <<" | " << student->ns << " | "<< setw(13) <<student->qq <<" |"
    << setw(4)<<student->toan << " |" << setw(4)<<student->ly
    << " | "  <<setw(4)<< student->hoa <<" | "<<student->tienganh;
    fo <<endl;
}

void TS::inFileAll(ofstream& fo)
{
    TS* head1=head;
    do
    {
        inFile(fo, head1);
        head1 = head1->next;
    }
    while (head1!= NULL);
}

int TS::nhap_so()
{
    int a;
    do
    {
        cin >> a;
        if (a<=0)
            cout << endl<<"Nhap sai. Nhap lai : ";
    }
    while (a<=0);
    return a;
}

char* TS::nhap_xau() // dung nhap thong tin thi sinh
{
    char* s;

    while (1)
    {
        s = new char[200];
        fflush(stdin);
        cin.getline(s, 199);
        int j = 0;
        for (int i  = 0; i < strlen(s); i++)
            if (s[i]=='|' || s[i] == ';' || s[i] == ':')
                j++;
        if (j== 7) break;
        cout << endl<<" Ban nhap sai thi sinh. Moi nhap lai: " << endl<<" ";
        delete s;
    }
    return s;
}

/*****************************************************************
thao tac sap xep du lieu
******************************************************************/
void TS::sap_xep_ten() // n la so thi sinh
{

    TS* head1 = head;
    TS* head2 = head;
    // sap xep ten
    do
    {
        head2 = head1;
        do
        {
            if (strcmp(head1->ten, head2->ten) > 0)
                trao_doi(head1, head2);
            head2=head2->next;
        }
        while (head2!=NULL);

        head1=head1->next;
    }
    while (head1!=NULL);
    // roi sap xep ho
    head1=head;
    do
    {
        head2 = head1;
        do
        {
            if ((strcmp(head1->ten, head2->ten) == 0)&&((strcmp(head1->ho, head2->ho) > 0)))
                trao_doi(head1, head2);
            head2=head2->next;
        }
        while (head2!=NULL);

        head1=head1->next;
    }
    while (head1!=NULL);
    // sau do sap xep ten dem
    head1=head;
    do
    {
        head2 = head1;
        do
        {
            if ((strcmp(head1->ten, head2->ten) == 0)&&((strcmp(head1->ho, head2->ho) == 0)))
                if (strcmp(head1->tendem, head2->tendem) > 0)
                    trao_doi(head1, head2);
            head2=head2->next;
        }
        while (head2!=NULL);

        head1=head1->next;
    }
    while (head1!=NULL);
}

void TS::sap_xep_diem_thi() // n la so thi sinh
{
    TS* head1 = head;
    TS* head2 = head;

    do
    {
        head2 = head1;
        do
        {
            if (head1->tongdiem < head2->tongdiem)
                trao_doi(head1, head2);
            head2=head2->next;
        }
        while (head2!=NULL);

        head1=head1->next;
    }
    while (head1!=NULL);
}

void TS::in_TS_do(ofstream& fo)
{
    fo<< "                  \n";
    int iDO = 0;
    TS* head1 = head;
    do
    {
        if (head1->kq==1)
        {
            inFile(fo, head1);
            iDO++;
        }
        head1 = head1->next;
    }
    while (head1!= NULL);
    fo.seekp(0, ios::beg);
    fo << iDO;
}

void TS::in_CLC(ofstream& fo)
{
    fo << "                  \n";
    //in danh sach sinh vien do, so sinh vien <=30
    TS* head1;
    sap_xep_diem_thi();
    int iCLC = 0, iCount;
    TS* head2 ;
    head1 = head;
    while ( head1 != NULL )
    {
        //tim so thi sinh cung diem, du tieu chuan vao CLC
        iCount = 0;
        head2 = head1;
        while (head2!=NULL)
        {
            if (head1->tongdiem != head2->tongdiem) break;
            if (head2 -> kq)
                if (head2->tienganh >= 6.5 )
                    iCount++;
            head2 = head2->next;
        }
        if (iCLC + iCount > 30)
            break;
        iCLC += iCount;
        // in cac thi sinh neu duoc chon
        if (iCount !=0 )
        {
            head2 = head1;
            while ( head2!=NULL)
            {
                if (head1->tongdiem != head2->tongdiem) break;
                if (head2 -> kq)
                    if (head2->tienganh >= 6.5 )
                        inFileTiengAnh(fo, head2);
                head2 = head2->next;
            }
        }
        head1 = head2;
    }
    //
    //in so thi sinh do
    fo.seekp(0, ios::beg);
    fo << iCLC;
}

/*****************************************************************
thao tac tinh tong diem cac mon
******************************************************************/
void TS::tinh_tong_diem()
{
    TS* head1=head;
    do
    {
        head1->tongdiem = head1->toan + head1->ly + head1->hoa;
        if (head1->tongdiem >= 24)
            head1->kq = 1;
        else head1->kq = 0;
        head1 = head1->next;
    }
    while (head1!= NULL);
}

void TS::in_danh_sach_sap_xep_ten(ofstream& fo)
{
    int iNUM = 0;
    TS* head1 = head;
    while (head1!=NULL)
    {
        head1 = head1->next;
        iNUM++;
    }
    fo << "                  \n";
    inFileAll(fo);
    fo.seekp(0, ios::beg);
    fo << iNUM;
}

/*****************************************************************
******************************************************************/
void TS::nhap_qua_File(ifstream& fi)
{
    TS* student;
    fi >> n;
    int iCount = 0, length_s = 0;
    char ch;
    char s[100];
    fi.get(ch);
    while (iCount < n)
    {
        student = new TS;
        if (iCount == 0)
        {
            head = student;
            student->next = NULL;
            last = head;
        }
        else
        {
            last->next = student;
            student->next = NULL;
            last = student;
        }
        fi.get(ch);
        do
        {
            s[length_s++] = ch;
            fi.get(ch);
            if (ch==10)   break;
            if (fi.eof()==1) break;
        }
        while (1);
        s[length_s] = '\0';
        // finish
        xu_li(student, s);
        length_s = 0;
        iCount++;
    }
}

void TS::nhap_qua_ban_phim()
{
    // giao dien
    system("cls");
    cout << "                        MENU nhap thi sinh                                   " << endl<<endl;
    cout << "  ==========================================================================="<< endl;
    cout << " |  So thi sinh ban nhap ?                                                   |" << endl;
    cout << "  ==========================================================================="<< endl;
    gotoxy(30, 4);
    n = nhap_so();

    system("cls");
    fflush(stdin);// su dung cin.ignore() khong hieu qua
    cout << "                        MENU nhap thi sinh                                   " << endl<<endl;
    cout << "  ==========================================================================="<< endl<<endl;
    for (int iCount = 0; iCount < n; iCount++)
    {
        if ( iCount % 5 ==0 & iCount!=0)
        {
            xoa_dong(5, 23);
            gotoxy(1,5);
        }
        // chuong trinh
        char* s;
        fflush(stdin);
        cout << " " << iCount+1 << " : ";
        s = nhap_xau();
        TS* student;
        student = new TS;
        if (iCount == 0)
        {
            head = student;
            student->next = NULL;
            last = head;
        }
        else
        {
            last->next = student;
            student->next = NULL;
            last = student;
        }
        xu_li(student, s);
    }
}

void TS::nhap_diem_tieng_anh(ifstream& fi)
{
    int m;
    fi >> m;
    char s[10],s2[10];
    float diemtienganh;
    TS* head1=head;
    fflush(stdin);
    while (fi >> s >> s2 >> diemtienganh)
    {
        head1 = head;
        do
        {
            if (head1 == NULL) break;
            if (head1->kq == 1 )
                if(stricmp(head1->id, s) == 0)
                {
                    head1->tienganh  = diemtienganh;
                    break;
                }
            head1 = head1 -> next;
        }
        while (1);
    }
}

/***************************************************************
phuc khao diem thi
 ****************************************************************/
void TS::phuc_khao()
{
    textcolor(10);
    cout << "                             PHUC KHAO THI SINH" << endl<<endl;
    cout << "  ==========================================================================="<< endl;
    cout << " |  MSV thi sinh phuc khao?                                                  |" << endl;
    cout << "  ==========================================================================="<< endl;
    textcolor(7);
    gotoxy(30, 4);
    char msv[20];
    cin.getline(msv, 19);
    fflush(stdin);
    int opi;
    TS* head1 = head;
    float diem_moi;
    while (stricmp(head1->id, msv)!=0)
    {
        head1 = head1->next;
    }
    cout << " \n\n Thong tin thi sinh:\n" << endl;
    cout << "  ";
    inScreen(head1, 1);
    cout << endl<<endl;
    cout << "    Ban chon phuong an nao?" << endl;
    cout << " ============================================================================"<< endl;
    cout << "|    1 - Thay doi truc tiep diem                                             |" << endl;
    cout << "|    2 - Thay the bang thi sinh khac                                         |" << endl;
    cout << " ============================================================================"<< endl;
    opi =choose(4, 15, 2);
    if (opi==1)
    {
        xoa_dong(11, 15);
        gotoxy(1, 11);
        cout << endl;
        cout << "    Ban thay doi diem nao?    " << endl;
        cout << "  ============================================================================"<< endl;
        cout << " |     1 - Toan                                                               |"<< endl;
        cout << " |     2 - Ly                                                                 |" << endl;
        cout << " |     3 - Hoa                                                                |" << endl;
        cout << "  ============================================================================"<< endl;
        opi =choose(4, 14,  3);
        gotoxy(2,19);
        switch (opi)
        {
        case 1:
            cout << "Diem toan moi: ";
            cin >> diem_moi;
            head1->toan = diem_moi;
            break;
        case 2:
            cout << "Diem ly moi: ";
            cin >> diem_moi;
            head1->ly = diem_moi;
            break;
        case 3:
            cout << "Diem hoa moi: ";
            cin >> diem_moi;
            head1->hoa = diem_moi;
            break;
        };
    }
    else
    {
        char s[200];
        xoa_dong(11, 24);
        gotoxy(1, 11);
        cout << " Nhap thi sinh moi: \n ";
        fflush(stdin);
        cin.getline(s, 199);
        xu_li(head1, s);
    }
    xoa_dong(11, 24);
    gotoxy(1, 11);
    cout << " Cap nhat lai diem thi sinh: \n\n";
    cout << "  ";
    inScreen(head1, 1);
    cout << endl<<endl;

    cout << "    Ban chon phuong an nao?    " << endl;
    cout << "  ============================================================================"<< endl;
    cout << " |    1 - Tiep tuc thay doi diem thi sinh                                     |" << endl;
    cout << " |    2 - Quay ve menu                                                        |" << endl;
    cout << "  ============================================================================"<< endl;
    opi = choose(4, 19, 2);
    if (opi==1)
    {
        system("cls");
        fflush(stdin);
        phuc_khao();
    }
}

void TS::in_danh_sach_thi_sinh_len_man_hinh()
{
    textcolor(10);
    cout << "                         DANH SACH THI SINH" << endl;
    cout <<"+============================================================================+"<<endl;
    cout <<"   MSV   |         TEN          |GT |N.SINH|    NOI SINH    | TOAN|  LY | HOA "<<endl;
    cout <<"+============================================================================+"<<endl;
    textcolor(7);
    int khoang_cach = 16;
    if ( n <= khoang_cach)
    {
        TS* head1 = head;
        gotoxy(70, 24);
        cout <<"Trang 1";
        gotoxy(1, 5);
        for (int i = 0; i < n; i++)
        {
            inScreen(head1, 1);
            head1 = head1->next;
            if (head1==NULL) break;
        }
        char ch = getch();
    }
    else
    {
        TS* head1 = head;
        char ch;
        int tong_so_trang = n / khoang_cach;
        if (tong_so_trang * khoang_cach != n) tong_so_trang++;
        int k = 1;
        do
        {
            gotoxy(65, 24);
            cout <<"Trang "<<setw(3)<<left << k << "/ "<< tong_so_trang;
            gotoxy(1,5);
            int dau = (k-1)*khoang_cach + 1;
            int cuoi = k*khoang_cach;
            head1 = head;
            for (int i = 1; i < dau; i++) head1 = head1->next;
            if (cuoi > n)
            {
                cuoi = n;
                xoa_dong(5, 23);
                gotoxy(1,5);
            }
            for (int i = dau; i <= cuoi; i++)
            {
                inScreen(head1, 1);
                head1 = head1->next;
            }
            ch = getch();
            if (ch == 80 || ch == 77 ) k++;
            if (ch == 72 || ch == 75) k--;
            if (k <1) k = 1;
            if (k > tong_so_trang) k = tong_so_trang;
        }
        while (ch != 27);
    }
}

void TS::in_thi_sinh_do_len_man_hinh()
{
    textcolor(10);
    cout << "                         DANH SACH THI SINH" << endl;
    cout <<"+============================================================================+"<<endl;
    cout <<"   MSV   |         TEN          |GT |N.SINH|    NOI SINH    | TOAN|  LY | HOA "<<endl;
    cout <<"+============================================================================+"<<endl;
    textcolor(7);
    TS* b[10001];
    TS* head1 = head;
    int so_TS_do = 0, khoang_cach = 18;

    while(head1!=NULL)
    {
        if (head1->kq==1) b[++so_TS_do] = head1;
        head1 = head1->next;
    }

    if (so_TS_do <= khoang_cach)
    {
        gotoxy(70, 24);
        cout <<"Trang 1";
        gotoxy(1, 5);
        for (int i = 1; i <= so_TS_do; i++)
            inScreen(b[i], 1);
        char ch = getch();
    }
    else
    {
        int so_trang = so_TS_do / khoang_cach;
        if (so_trang * khoang_cach != n)    so_trang++;
        int k = 1;
        do
        {
            gotoxy(65, 24);
            cout <<"Trang "<<setw(3)<<left << k << "/ "<< so_trang;
            gotoxy(1,5);
            int dau = (k-1)*khoang_cach+1;
            int cuoi = k*khoang_cach;
            if (cuoi > so_TS_do)
            {
                xoa_dong(5, 23);
                gotoxy(1,5);
                cuoi = so_TS_do;
            }
            for (int i = dau; i<=cuoi; i++)
                inScreen(b[i], 1);

            char ch = getch();
            if (ch==27) break;
            if (ch == 80 || ch == 77 ) k++;
            if (ch == 72 || ch == 75) k--;
            if (k <1) k = 1;
            if (k > so_trang) k = so_trang;
        }
        while (1);
    }
}

void TS::in_thi_sinh_CLC_len_man_hinh()
{
    textcolor(6);
    cout << "                         DANH SACH THI SINH" << endl;
    cout <<"+=============================================================================+"<<endl;
    cout <<"   MSV   |         TEN           |GT|N.SINH|   NOI SINH  | TOAN| LY | HOA|  TA"<<endl;
    cout <<"+=============================================================================+"<<endl;
    textcolor(7);
    int iCLC = 0, iCount, so_TS_CLC =0;
    TS* head1 = head, *head2 ;
    TS* b[10001];
    while ( head1 != NULL )
    {
        //tim so thi sinh cung diem, du tieu chuan vao CLC
        iCount = 0;
        head2 = head1;
        while (head2!=NULL)
        {
            if (head1->tongdiem != head2->tongdiem) break;
            if (head2 -> kq)
                if (head2->tienganh >= 6.5 )
                    iCount++;
            head2 = head2->next;
        }
        if (iCLC + iCount > 30) break;
        iCLC += iCount;
        // in cac thi sinh neu duoc chon
        if (iCount !=0 )
        {
            head2 = head1;
            while ( head2!=NULL)
            {
                if (head1->tongdiem != head2->tongdiem) break;
                if (head2 -> kq)
                    if (head2->tienganh >= 6.5 )
                        b[++so_TS_CLC]= head2;
                head2 = head2->next;
            }
        }
        head1 = head2;
    }


    // in ra man hinh
    int khoang_cach = 18;
    if (so_TS_CLC <= khoang_cach)
    {
        gotoxy(70, 24);
        cout <<"Trang 1 / 1";
        gotoxy(1, 5);
        for (int i = 1; i <= so_TS_CLC; i++)
            inScreen(b[i], 2);
        char ch = getch();
    }
    else
    {
        int so_trang = so_TS_CLC / khoang_cach;
        if (so_trang * khoang_cach != n)    so_trang++;
        int k = 1;
        do
        {
            gotoxy(65, 24);
            cout <<"Trang "<<setw(3)<<left << k << "/ "<< so_trang;
            gotoxy(1,5);
            int dau = (k-1)*khoang_cach+1;
            int cuoi = k*khoang_cach;
            if (cuoi > so_TS_CLC)
            {
                xoa_dong(5, 23);
                gotoxy(1,5);
                cuoi = so_TS_CLC;

            }
            for (int i = dau; i<=cuoi; i++)
                inScreen(b[i], 2);
            char ch = getch();
            if (ch==27) break;
            if (ch == 80 || ch == 77 ) k++;
            if (ch == 72 || ch == 75) k--;
            if (k <1) k = 1;
            if (k > so_trang) k = so_trang;
        }
        while (1);
    }
}

void TS::tac_gia()
{
    gotoxy(1, 4);
    textcolor(11);
    cout << "                                 NHOM TAC GIA" << endl;
    textcolor(12);
    cout << "       +=================================================================+"<<endl;
    cout << "       |       1. Nguyen Duc Anh K56CC       - Bien soan code            |" << endl;
    cout << "       |                                     - Kiem thu chuong trinh     |" << endl;
    cout << "       |       2. Dao Minh Hoa K56CC         - Thiet ke input            |" << endl;
    cout << "       |                                     - Kiem thu chuong trinh     |" << endl;
    cout << "       |       3. Do Ngoc Thuc K56CC         - Thiet ke input            |" << endl;
    cout << "       +=================================================================+";
    char s[200] = "                Phan mem Quan Li Thi Sinh thi Dai Hoc 2012                    ";
    int l = strlen(s);//cout << l; char ch = getch();
    int cot = 1, dong = 20;
    do
    {
        textcolor(8);
        gotoxy(cot, dong);
        if (80 - cot < l)
        {
            for (int i = 0; i <= 80 - cot; i++) cout << s[i];
            gotoxy(1, dong);
            for (int j = 80 - cot + 1; j < l; j++) cout << s[j];
        }
        else
            cout << s;
        Sleep(130);
        cot ++;
        if (cot >=80) cot = 1;
    }
    while (1);
}

void TS::textcolor(int mau)
{
    SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), mau);
}






