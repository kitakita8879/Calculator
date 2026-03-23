# Calculator

這是一個以 Java + XML 開發的 Android 計算機 app。  
專案原本是較早期的單一 `Activity` 集中處理 UI 與計算邏輯的寫法，這一輪已整理為較清楚的 `Data Binding + ViewModel + Engine` 結構，方便後續持續做小幅優化與維護。

## 技術現況

- `compileSdk = 36`
- `targetSdk = 36`
- `minSdk = 24`
- Java 17
- Android Gradle Plugin / Gradle 已升級到較新的組合
- UI 採用 XML + Data Binding
- UI 元件以 Material Components 為主

## 架構說明

目前專案的主要責任分層如下：

- `MainActivity`
  - 負責初始化 `Data Binding`
  - 建立並綁定 `CalculatorViewModel`
  - 不直接處理四則運算邏輯

- `CalculatorViewModel`
  - 作為畫面事件入口
  - 接收按鈕操作，例如數字、運算子、清除、刪除、等號、精度調整
  - 呼叫 `CalculatorEngine`
  - 將 `CalculatorUiState` 同步到 Data Binding 使用的 observable 欄位

- `CalculatorEngine`
  - 集中處理計算邏輯
  - 包含四則運算、乘除優先、輸入規則、小數點限制、刪除、精度調整、錯誤處理
  - 不直接接觸 Android UI 元件

- `CalculatorUiState`
  - 表示目前畫面應顯示的狀態快照
  - 例如算式文字、主顯示文字、小數點是否可輸入、精度按鈕是否可用、是否為錯誤狀態

- `CalculatorAction`
  - 將使用者操作整理成明確型別
  - 例如數字、小數點、運算子、清除、刪除、等號、精度上下調整

- `Operator`
  - 定義加、減、乘、除等運算子型別
  - 避免直接用字串判斷運算種類

## 資料流

目前畫面互動的主要流程如下：

1. 使用者點擊 XML 按鈕
2. XML 透過 Data Binding 呼叫 `CalculatorViewModel`
3. `CalculatorViewModel` 將事件轉成對應的 `CalculatorAction`
4. `CalculatorEngine` 根據 action 更新內部狀態並回傳 `CalculatorUiState`
5. `CalculatorViewModel` 將 state 同步到 observable 欄位
6. Data Binding 自動更新畫面顯示

## 目前功能

目前已完成並可作為後續調整基礎的功能如下：

- 四則運算：加、減、乘、除
- 乘除優先於加減
- 小數點輸入限制
  - 同一個數字區段不能重複輸入 `.` 
- `C`
  - 清除目前整個輸入與狀態
- `DEL`
  - 刪除目前輸入最後一位
  - 若目前停在尾端運算子，可刪除尾端運算子
  - 刪除尾端運算子後，會還原前一個操作數供繼續編輯
- `=`
  - 計算目前公式結果
- `=` 後重新輸入
  - 輸入新數字後會開始新的算式
- `=` 後接續運算
  - 計算完成後按運算子，會以前一次結果作為新的左操作數繼續運算
- 精度調整
  - 透過 `<` / `>` 調整結果顯示的小數位數
- 除以零錯誤處理
  - 顯示 `無法除以 0`
  - 錯誤顯示後重新輸入數字，會重置狀態並開始新的算式
- 畫面旋轉後結果保留
  - 已有基本 instrumentation test 驗證
