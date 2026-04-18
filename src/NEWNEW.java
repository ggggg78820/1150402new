import java.util.Scanner;
public class NEWNEW {
    //1. 顯示menu清單
    //2. 讓玩家輸入選項
    //3. 根據玩家選項執行
    //4. 判斷是否結束
    //5. 迴圈
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        while(true){
                System.out.println("1. 打招呼 ");
                System.out.println(x: "2. 吃飯 ");
                
                System.out.println("0. 離開遊戲");
                Scanner sc = new Scanner(System.in);
                System.out.print("請輸入選項：");
                String input = sc.nextLine().trim();
                switch (input) {
                    case "1":
                        System.out.println("哈囉，歡迎使用軟體！");
                        break;
                    case "0":
                        System.out.println("感謝使用，再見！");
                        return; // 結束程式
                    default:
                        System.out.println("無效的選項，請重新輸入。");
                }
        }
    }
}