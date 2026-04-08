import java.util.Scanner;

public class App {
  // 數值上限與下限，所有狀態都會被限制在 0 到 100 之間。
  // 這樣可以避免餵食後超過 100，或扣太多變成負數。
  private static final int MAX_STAT = 100;
  private static final int MIN_STAT = 0;

  // Scanner 專門負責從終端機讀取玩家輸入。
  private final Scanner scanner;

  // 電子雞的基本資料與三個核心狀態。
  // hunger: 飢餓值，越高表示越飽。
  // happiness: 心情值，越高表示越開心。
  // energy: 體力值，越高表示越有精神。
  private String petName;
  private int hunger;
  private int happiness;
  private int energy;

  // day 代表目前是第幾天。
  // alive 用來控制遊戲主迴圈是否繼續進行。
  private int day;
  private boolean alive;

  public App(Scanner scanner) {
    this.scanner = scanner;

    // 設定初始狀態，讓玩家一開始就能直接進入遊戲。
    petName = "電子雞";
    hunger = 55;
    happiness = 60;
    energy = 70;
    day = 1;
    alive = true;
  }

  public void run() {
    // 遊戲開始前先顯示簡介，再讓玩家命名。
    showIntro();
    askPetName();

    // 只要 alive 為 true，就持續進行每天的照顧流程。
    while (alive) {
      // 每一回合的固定順序：
      // 1. 顯示目前狀態
      // 2. 顯示選單
      // 3. 讀取玩家選項
      // 4. 根據選項執行對應行為
      showStatus();
      showMenu();
      int choice = readChoice();
      handleChoice(choice);

      // 如果玩家選擇結束遊戲，handleChoice 會把 alive 改成 false。
      // 這裡立刻跳出迴圈，就不再進行時間推進。
      if (!alive) {
        break;
      }

      // 玩家完成一個動作後，時間往下一天推進。
      // 推進後會自然消耗狀態，最後再檢查是否達成失敗或通關條件。
      advanceTime();
      checkLifeState();
    }

    // 無論是玩家主動退出、照顧失敗，或成功養到第 10 天，
    // 離開主迴圈後都會統一顯示結算畫面。
    showEnding();
  }

  private void showIntro() {
    System.out.println("=== 電子雞餵養遊戲 ===");
    System.out.println("你要照顧一隻住在終端機裡的小電子雞。");
    System.out.println("請透過餵食、玩耍和休息，讓牠健康長大。");
    System.out.println();
  }

  private void askPetName() {
    System.out.print("幫你的電子雞取個名字：");
    String input = scanner.nextLine().trim();

    // 如果玩家有輸入名字，就使用玩家輸入的內容。
    // 如果直接按 Enter，則保留預設名稱「電子雞」。
    if (!input.isEmpty()) {
      petName = input;
    }

    System.out.println(petName + " 出生了，照顧牠吧。");
    System.out.println();
  }

  private void showStatus() {
    System.out.println("------------");
    System.out.println("第 " + day + " 天");
    System.out.println("名字：" + petName);

    // statLabel 會把數值與文字描述組合在一起，
    // 例如「55 (普通)」，讓玩家更直觀看懂狀態。
    System.out.println("飢餓值：" + statLabel(hunger));
    System.out.println("心情值：" + statLabel(happiness));
    System.out.println("體力值：" + statLabel(energy));
    System.out.println("------------");
  }

  private void showMenu() {
    System.out.println("請選擇行動：");
    System.out.println("1. 餵食");
    System.out.println("2. 玩耍");
    System.out.println("3. 休息");
    System.out.println("4. 查看說明");
    System.out.println("5. 結束遊戲");
    System.out.print("輸入選項：");
  }

  private int readChoice() {
    while (true) {
      String input = scanner.nextLine().trim();

      try {
        int choice = Integer.parseInt(input);

        // 只有 1 到 5 是有效選項，其餘數字都要求重新輸入。
        if (choice >= 1 && choice <= 5) {
          return choice;
        }
      } catch (NumberFormatException ignored) {
        // 如果玩家輸入的不是數字，例如文字或空白，
        // 會進入 catch，但這裡不需要中斷程式，
        // 只要繼續提示玩家重新輸入即可。
      }

      System.out.print("請輸入 1 到 5：");
    }
  }

  private void handleChoice(int choice) {
    // switch 用來把選單數字對應到不同功能。
    // 這樣主流程只需要處理一個 choice，邏輯會比較清楚。
    switch (choice) {
      case 1:
        feedPet();
        break;
      case 2:
        playWithPet();
        break;
      case 3:
        letPetRest();
        break;
      case 4:
        showHelp();
        break;
      case 5:
        // 玩家主動離開遊戲，直接讓主迴圈停止。
        alive = false;
        System.out.println("你決定先結束今天的照顧行程。");
        break;
      default:
        // 理論上不會進入這裡，因為 readChoice 已經先過濾掉非法輸入。
        break;
    }
  }

  private void feedPet() {
    // 餵食邏輯：
    // 飢餓值上升最多，心情和體力小幅增加，
    // 表示吃飽後會比較滿足，也比較有精神。
    hunger = clamp(hunger + 25);
    happiness = clamp(happiness + 5);
    energy = clamp(energy + 5);
    System.out.println("你餵了 " + petName + " 一份飼料，牠吃得很開心。");
  }

  private void playWithPet() {
    // 玩耍邏輯：
    // 心情會明顯提升，但同時會消耗體力與飢餓值。
    // 這代表玩得越開心，也越容易肚子餓和疲累。
    happiness = clamp(happiness + 20);
    hunger = clamp(hunger - 15);
    energy = clamp(energy - 18);
    System.out.println(petName + " 跟你玩了一會兒，開心得跳來跳去。");
  }

  private void letPetRest() {
    // 休息邏輯：
    // 體力恢復最多，但睡覺期間沒有進食，所以飢餓值會下降。
    energy = clamp(energy + 30);
    hunger = clamp(hunger - 10);
    System.out.println(petName + " 睡了一覺，精神恢復不少。");
  }

  private void showHelp() {
    System.out.println("照顧提示：");
    System.out.println("- 飢餓值越高越好，太低代表肚子餓。");
    System.out.println("- 心情值太低時，電子雞會不開心。");
    System.out.println("- 體力值太低時，需要讓牠休息。");
    System.out.println("- 每回合結束後，數值都會自然變化。");
  }

  private void advanceTime() {
    // 每做完一次選擇，就算過了一天。
    day++;

    // 自然消耗邏輯：
    // 電子雞每天都會消耗飢餓、心情與體力。
    // randomBetween 讓每次下降值略有不同，遊戲會更有變化。
    hunger = clamp(hunger - randomBetween(8, 16));
    happiness = clamp(happiness - randomBetween(4, 10));
    energy = clamp(energy - randomBetween(5, 12));

    // 額外懲罰邏輯 1：
    // 如果肚子太餓，除了飢餓值本身很低，心情還會再被扣一次。
    // 這表示狀態之間有關聯，而不是彼此完全獨立。
    if (hunger < 30) {
      happiness = clamp(happiness - 5);
      System.out.println(petName + " 肚子有點餓，心情也受到影響。");
    }

    // 額外懲罰邏輯 2：
    // 如果體力太低，也會讓心情下降。
    // 這讓玩家不能只顧著餵食，還要注意休息安排。
    if (energy < 25) {
      happiness = clamp(happiness - 5);
      System.out.println(petName + " 太累了，看起來有點沒精神。");
    }

    System.out.println();
  }

  private void checkLifeState() {
    // 失敗條件 1：飢餓值歸零。
    // 一旦其中一個核心狀態降到 0，就直接結束遊戲。
    if (hunger <= 0) {
      alive = false;
      System.out.println(petName + " 因為太久沒吃東西，虛弱地倒下了。");
      return;
    }

    // 失敗條件 2：體力值歸零。
    if (energy <= 0) {
      alive = false;
      System.out.println(petName + " 累壞了，沒有撐過今天。");
      return;
    }

    // 失敗條件 3：心情值歸零。
    if (happiness <= 0) {
      alive = false;
      System.out.println(petName + " 一直悶悶不樂，最後失去了活力。");
      return;
    }

    // 通關條件：
    // 當天數超過 10，代表玩家成功照顧到第 10 天。
    // 此時也把 alive 改成 false，結束主迴圈。
    if (day > 10) {
      alive = false;
      System.out.println("恭喜，你成功照顧 " + petName + " 到第 10 天。");
    }
  }

  private void showEnding() {
    System.out.println();
    System.out.println("=== 遊戲結束 ===");
    System.out.println(petName + " 最後的狀態：");
    System.out.println("飢餓值：" + hunger);
    System.out.println("心情值：" + happiness);
    System.out.println("體力值：" + energy);
    System.out.println("感謝遊玩。");
  }

  private String statLabel(int value) {
    // 把數值和等級文字組合在一起，方便統一顯示。
    return value + " " + moodWord(value);
  }

  private String moodWord(int value) {
    // 依照數值區間回傳不同描述文字。
    // 這是一種常見的分級寫法，把連續數值轉成容易理解的狀態語意。
    if (value >= 80) {
      return "(超棒)";
    }
    if (value >= 60) {
      return "(穩定)";
    }
    if (value >= 40) {
      return "(普通)";
    }
    if (value >= 20) {
      return "(偏低)";
    }
    return "(危險)";
  }

  private int clamp(int value) {
    // clamp 的用途是「夾住數值範圍」。
    // 小於最小值就回傳最小值，大於最大值就回傳最大值，
    // 否則保持原值。
    if (value < MIN_STAT) {
      return MIN_STAT;
    }
    if (value > MAX_STAT) {
      return MAX_STAT;
    }
    return value;
  }

  private int randomBetween(int min, int max) {
    // 產生 min 到 max 之間的隨機整數，包含 min 和 max。
    // 寫成工具方法後，主邏輯就能直接表達「扣 8 到 16 之間」的意思。
    return (int) (Math.random() * (max - min + 1)) + min;
  }
}
