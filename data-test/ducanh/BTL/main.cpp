#include <iostream>
#include <fstream>
#include <string>
#include <windows.h>
#include <conio.h>
#include <iomanip>
#include "qlsv.h"
using namespace std;

ifstream fi_TS("TS.inp.txt");
ofstream fo_TS_SapXep("TS_SapXep.out.txt");
ofstream fo_TS_Do("TS_Do.out.txt");
ifstream fi_TS_DiemNgoaiNgu("TS_DiemNgoaiNgu.inp.txt");
ofstream fo_TS_CLC("TS_CLC.out.txt");

void open_File(ifstream& fi)
{
    if (!fi)
    {
        cout << "Lo^i : Kho^ng the^? thao ta'c vo'i te^p " << endl;
        system("pause");
        exit(1);
    }
}
void open_File(ofstream& fo) // tham chieu stream
{
    if (!fo)
    {
        cout << "Lo^i : Kho^ng the^? thao ta'c vo'i te^p " << endl;
        system("pause");
        exit(1);
    }
}
void close_all_File()
{
    fi_TS.close();
    fo_TS_SapXep.close();
    fo_TS_Do.close();
    fi_TS_DiemNgoaiNgu.close();
    fo_TS_CLC.close();
}
void open_all_File()
{
    open_File(fi_TS);
    open_File(fo_TS_SapXep);
    open_File(fo_TS_Do);
    open_File(fi_TS_DiemNgoaiNgu);
    open_File(fo_TS_CLC);
}
// 8 trang
int main()
{
    open_all_File();
    TS a;

    // in menu
    a.textcolor(12);
    cout << endl;
    cout << "                CHUONG TRINH QUAN LI THI SINH THI DAI HOC"<<endl<<endl;
    a.textcolor(11);
    cout << "  ==========================================================================="  << endl;
    cout << " |    1 - Nhap du lieu qua File                                              |" << endl;
    cout << " |    2 - Nhap du lieu qua ban phim                                          |" << endl;
    cout << " |    3 - Tac gia                                                            |" << endl;
    cout << "  ==========================================================================="  << endl;

    // lua chon menu
    int opi = a.choose(4, 5, 3);
    switch (opi)
    {
    case 1:
        a.textcolor(8);
        a.nhap_qua_File(fi_TS);
        // in thong bao
        a.gotoxy(1, 10);
        cout << "                             XU LI DU LIEU" << endl<<endl;
        cout << "  ==========================================================================="<< endl;
        cout << " |  Nhap du lieu tu file TS.inp                                => hoan thanh |" << endl;
        cout << " |  Nhap du lieu diem Tieng Anh trong file TS_DiemNgoaiNgu.inp => hoan thanh |" << endl;
        cout << "  ==========================================================================="<< endl;
        a.gotoxy(40, 17);
        cout << "An phim bat ki de tiep tuc.";
        break;
    case 2:
        a.textcolor(8);
        a.nhap_qua_ban_phim();
        // in thong bao
        a.gotoxy(1, 9);
        cout << "                             XU LI DU LIEU" << endl<<endl;
        cout << "  ==========================================================================="<< endl;
        cout << " |  Du lieu cac thi sinh da nap vao bo nho                     => hoan thanh |" << endl;
        cout << " |  Nhap du lieu diem Tieng Anh trong file TS_DiemNgoaiNgu.inp => hoan thanh |" << endl;
        cout << "  ==========================================================================="<< endl;
        a.gotoxy(40, 17);
        cout << "An phim bat ki de tiep tuc.";
        break;
    case 3:
        system("cls");
        a.tac_gia();
        break;
    default:
        exit(1);
    };
    
    char ch = getch();
    
    // phan 2, lua chon chu nang
    do
    {
        // in menu chuc nang
        system("cls");
        a.gotoxy(1, 3);
        cout << "                            MENU chuc nang"<<endl;
        cout << "   +====================================================================+" <<endl;
        cout << "   |            1 - Thuc hien phuc khao                                 |" <<endl;
        cout << "   |            2 - Sap xep ten                                         |" <<endl;
        cout << "   |            3 - Tim thi sinh do                                     |" <<endl;
        cout << "   |            4 - Tim thi sinh lop CLC                                |" <<endl;
        cout << "   |            5 - Thoat chuong trinh                                  |" <<endl;
        cout << "   +====================================================================+" <<endl;
        // chon menu
        int opi = a.choose(15, 5, 5);

        switch (opi)
        {
            // thuc hien phuc khao
        case 1:
        {
            system("cls");
            a.textcolor(7);
            a.phuc_khao();a.textcolor(8);
            cin.ignore();
            break;
        }
        // sap xep ten
        case 2:
        {
            a.textcolor(8);
            system("cls");
            a.sap_xep_ten();
            a.in_danh_sach_sap_xep_ten(fo_TS_SapXep);
            a.gotoxy(10, 12);cout <<" * Ghi du lieu vao File TS_SapXep   :     hoan thanh "<<endl;
            a.gotoxy(10, 13);cout <<"   Ban co muon xem danh sach ngay bay gio? (c/k) ";
            char ch = getch();
            if (ch=='c'|| ch=='C')
            {
                system("cls");
                a.in_danh_sach_thi_sinh_len_man_hinh();
            }a.textcolor(8);
            break;
        }
        // tim thi sinh do
        case 3:
        {
            a.textcolor(8);
            system("cls");
            a.tinh_tong_diem();
            a.in_TS_do(fo_TS_Do);
            a.gotoxy(10, 12);cout <<" * Ghi du lieu vao File TS_Do   :     hoan thanh "<<endl;
            a.gotoxy(10, 13);cout <<"   Ban co muon xem danh sach ngay bay gio? (c/k) ";
            char ch = getch();
            if (ch=='c'|| ch=='C')
            {
                system("cls");
                a.in_thi_sinh_do_len_man_hinh();
            }
            a.textcolor(8);
            break;
        }
        // tim thi sinh CLC
        case 4:
        {
             a.textcolor(8);
            system("cls");
            a.tinh_tong_diem();
            a.nhap_diem_tieng_anh(fi_TS_DiemNgoaiNgu);
            a.in_CLC(fo_TS_CLC);
            a.gotoxy(10, 12);cout <<" * Ghi du lieu vao File TS_CLC   :     hoan thanh "<<endl;
            a.gotoxy(10, 13);cout <<"   Ban co muon xem danh sach ngay bay gio? (c/k) ";
            char ch = getch();
            if (ch=='c'|| ch=='C')
            {
                system("cls");
                a.in_thi_sinh_CLC_len_man_hinh();
            }
            a.textcolor(8);
            break;
        }
        default:
            exit(1);
        };
    }
    while( opi != 5 );

    // ket thuc chuong trinh
    close_all_File();
    return 0;
}

