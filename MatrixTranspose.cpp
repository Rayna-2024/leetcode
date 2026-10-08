#include <iostream>
#include <cmath>

using namespace std;

class Solution{
    public:
    string Transpose(string str){
        int strSize = str.length();
        int sqrtNum = sqrt(strSize);
        if(sqrtNum*sqrtNum != strSize){
            return "ERROR";
        }
        string res = "";
        for(int i = 0;i < sqrtNum;i++){
            int count = 0;
            while(count < sqrtNum){
                res += str.substr(i + count*sqrtNum, 1);
                count++;
            }
        }
        return res;
    }
};

int main(){
    Solution solution;
    string res1 = solution.Transpose("435245019");
    string res2 = solution.Transpose("0123456789");
    cout<<res1<<endl;
    cout<<res2<<endl;
    return 0;
}

// git config --global user.name "你的名字"
// git config --global user.email "你的邮箱"hh

// <algorithm> 里常见：
// sort(v.begin(), v.end());          // 排序
// reverse(v.begin(), v.end());       // 反转

// max(a, b);                         // 最大值
// min(a, b);                         // 最小值

// max_element(v.begin(), v.end());   // 返回最大值的迭代器
// min_element(v.begin(), v.end());   // 返回最小值的迭代器

// find(v.begin(), v.end(), x);       // 查找 x
// count(v.begin(), v.end(), x);      // 统计 x 出现次数

// fill(v.begin(), v.end(), 0);       // 全部赋值为 0

// lower_bound(v.begin(), v.end(), x); // 第一个 >= x 的位置
// upper_bound(v.begin(), v.end(), x); // 第一个 > x 的位置

// 比如：
// vector<int> v = {3, 1, 5, 2};

// sort(v.begin(), v.end());
// // 1 2 3 5

// <utility> 里你最常见的是：
// pair<int, int> p = {1, 2};

// p.first;   // 1
// p.second;  // 2

// 还有：
// swap(a, b);          // 交换
// make_pair(a, b);     // 创建 pair

// 例如：
// int a = 1, b = 2;
// swap(a, b);

// // a = 2
// // b = 1

// <cmath> 主要是数学：
// sqrt(x);     // 平方根
// pow(a, b);   // a 的 b 次方

// abs(x);      // 绝对值

// floor(x);    // 向下取整
// ceil(x);     // 向上取整
// round(x);    // 四舍五入

// log(x);      // ln
// log10(x);    // 以 10 为底
// exp(x);      // e^x

// sin(x);
// cos(x);
// tan(x);

// 比如：
// sqrt(16);   // 4.0
// pow(2, 3);  // 8
// floor(4.9); // 4
// ceil(4.1);  // 5
// round(4.6); // 5