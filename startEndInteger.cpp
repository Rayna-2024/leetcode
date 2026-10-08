#include<iostream>
#include<sstream>
// 这道题更重要的是记住怎么转变进制
// 字符串转变进制：stoi("1010", nullptr, 2);   // 10.   这个是从字符串转变为int
// int类型转变进制：用C++的stringstream 这个用了ss.str之后可以变为string类型
// 如何求2的32次方 in C++？pow(2, 32)
using namespace std;
class Solution{
    public:
    string GetHexString(long long input){
        if(input > pow(2, 32) || input < -pow(2, 31))return "overflow";
        // 开始使用stringstream把input转变为十六进制的数字
        stringstream ss;
        ss<<hex<<input;
        string res = ss.str();

        while(res.size()< 8){
            res += "0";
        }
        
        if(input < 0){
            res = res.substr(res.size()-8);
        }

        for(char c:res){
            c = toupper(c);
        }

        string ans1 = res.substr(0, 2);
        string ans2 = res.substr(2, 2);
        string ans3 = res.substr(4, 2);
        string ans4 = res.substr(6, 2);

        string finalRes = ans1 + " " + ans2 + " " + ans3 + " " + ans4 + " " + "\n" +
        ans4 + " " + ans3 + " " + ans2 + " " + ans1;
        return finalRes;
    }
};

int main(){
    Solution solution;
    string res = solution.GetHexString(-10);
    cout << res << endl;
}

// 报错？
// incomplete type "std::__1::stringstream" is not allowed
// 引入包：#include <sstream>
// 或者直接全部引入：#include <bits/stdc++.h>


// 报错：no suitable conversion function from "std::__1::basic_string<char, 
// std::__1::char_traits<char>, std::__1::allocator<char>>::iterator" 
// (aka "std::__1::__wrap_iter<std::__1::__pointer<char, std::__1::allocator<char>>>")
//  to "std::__1::basic_string<char, std::__1::char_traits<char>, std::__1::allocator<char>>::size_type" 
// (aka "std::__1::__size_type<std::__1::allocator<char>, std::__1::ptrdiff_t>") exists
// 其实是不能再substr写str.begin()这种迭代器。在对string进行操作时，不能像vector那样写迭代器，要写成数组的索引
// substr(res.begin(), res.begin()+4);不对
// substr(0， 4);对


//"/n"错
// "\n"对