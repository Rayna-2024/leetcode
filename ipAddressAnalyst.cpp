// #include <bits/stdc++.h>
#include <iostream>

using namespace std;

class Solution{
    public:
    string IPAddressConversion(string inputStr){
        string res = "";
        string res1 = inputStr.substr(6, 2) + inputStr.substr(9, 2);
        string res2 = inputStr.substr(18, 1);

        string res3 = inputStr.substr(48, 2);
        string res4 = inputStr.substr(51, 2);
        string res5 = inputStr.substr(54, 2);
        string res6 = inputStr.substr(57, 2);

        // length
        int length = stoi(res1, nullptr, 16);
        // mark
        // 好难啊，这里为什么要右移一位呢？？
        // 因为例子中标志位是三位二进制，而input一个字母就是四位二进制数了（这里位40的4，即0100）
        // 0100取前三位就是010，这就是为什么要右移一位
        // 另外一件事，右移的时候默认是以二进制形式右移的
        int tmp = stoi(res2, nullptr, 16)>>1;
        // ip address
        int ipAdr1 = stoi(res3, nullptr, 16);
        int ipAdr2 = stoi(res4, nullptr, 16);
        int ipAdr3 = stoi(res5, nullptr, 16);
        int ipAdr4 = stoi(res6, nullptr, 16);

        string resAdr = to_string(ipAdr1) + "." + to_string(ipAdr2) + "." + to_string(ipAdr3) + "." + to_string(ipAdr4);
        string resMark = to_string(tmp).substr(0, 3);
        string resLength = to_string(length);
        res = res + resLength + "," + resMark + "," + resAdr;
        return res;

    }
};
int main(){
    Solution solution;
    string res = solution.IPAddressConversion("45 00 00 3C 1C 46 40 00 40 06 B1 E6 C0 A8 00 01 C0 A8 00 C7");
    cout<< res<< endl;
    return 0;
}


// 如何将int变成字符串？string s = to_string(num);
// string转为num是stoi(str)

