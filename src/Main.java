import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    // main 是 Java 程式的入口點。
    // 這裡只負責建立需要的物件，真正的遊戲邏輯交給 App 處理。
    Scanner scanner = new Scanner(System.in);
    App app = new App(scanner);

    // 啟動遊戲主流程。
    app.run();

    // 遊戲結束後關閉輸入資源。
    scanner.close();
  }
}
