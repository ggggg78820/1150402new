import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in); 
    String txt = sc.nextLine();
    int user = Integer.valueOf(txt);
    //int user = 0; // 0=剪刀 1=石頭 2=布 3=讚
    int computer = (int)(Math.random() * 4);
    System.out.println("你出: " + user);
    System.out.println("電腦出: " + computer);
    if (user == computer) {
      System.out.println("平手");
    } // && and 而且 || or 或者
    else if ((user == 0 && computer == 3) ||
             (user == 0 && computer == 2) ||
             (user == 1 && computer == 0) ||
             (user == 1 && computer == 3) ||
             (user == 2 && computer == 1) ||
             (user == 2 && computer == 0) ||
             (user == 3 && computer == 1) ||
             (user == 3 && computer == 2))
             {
      System.out.println("你贏了");
    } 
    else {
      System.out.println("你輸了");
    }
    sc.close();
  }
}