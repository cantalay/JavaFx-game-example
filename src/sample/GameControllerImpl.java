package sample;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameControllerImpl {
//Initial degiskenlerimizi olusturduk.
    private static List<Integer> allPossibleNumbers;
    static Integer computerChosedIdontknow;
    List<Integer> newPossibleList;
    List<Integer> allNumbers;


    String newGame() {//Yeni bir oyuna baslamamiz gerektiginde tum olasi degerleri hesapladik ve bilgisayar icin bir sayi tuttuk
        findAllProbabilities();
        computerChosedIdontknow = guessOrFindRandomNumber(allPossibleNumbers);
        System.out.println("computer random value : " + computerChosedIdontknow);
        Integer guessedNumber = guessOrFindRandomNumber(allPossibleNumbers);
        return String.valueOf(guessedNumber);
    }


/* Bilgisayarin bizim tuttugumuz sayiyi score'ler yardimiyla
* Tahmin etmesini saglayan fonksiyon. Tum degerler arasindan
* Score'lere gore uygunsuz degerleri siler.*/
    boolean checkNewPossibleList(String compScore, String compGuess) {
        System.out.println("checkNewPossibleList... compScore : " + compScore + " compGuess : " + compGuess);
        newPossibleList = new ArrayList<>();
        String[] strings = parseCompScore(compScore);
        for (Integer possibleNumber : allPossibleNumbers) {
            String[] deneme = compCalc(possibleNumber, Integer.valueOf(compGuess));
            if (Arrays.equals(deneme, strings)) {
                newPossibleList.add(possibleNumber);
            }
        }
        System.out.println("for sonu all : " + allPossibleNumbers.size());
        System.out.println("for sonu new : " + newPossibleList.size());
        System.out.println("new Possible list size : " + newPossibleList.size());
        if (newPossibleList.size() > 1) {
            allPossibleNumbers = newPossibleList;
            return false;
            // compTurn();
        } else if (newPossibleList.size() == 0) {
            throw new RuntimeException("please check your point");
        } else {
            return true;
        }

    }

    //kullanıcının girdiği değerin uygunluğunu kontrol ediyoruz.
    boolean checkUserGuess(String userGuess) {
        System.out.println("checkUserGuess size... "+ allNumbers.size());
        try {
            return allNumbers.contains(Integer.valueOf(userGuess));
        } catch (Exception e) {
            return false;
        }
    }

    //Bu fonksiyon tum olasi degerler arasindan yeni bir secim yapmaktadir.
    String guessNewNumber() {
        System.out.println("guessNewNumber... size : " + newPossibleList.size());
        Collections.shuffle(newPossibleList);
        return String.valueOf(newPossibleList.get(0));

    }

    private Integer guessOrFindRandomNumber(List<Integer> possibleNumbers) {
        Collections.shuffle(possibleNumbers);
        return possibleNumbers.get(0);
    }

    //Bu fonksiyon bizim tahminimizin scorunu hesaplamaktadır.
    private String[] compCalc(Integer possibleNumber, Integer guessedNumber) {
        int plus = 0;
        int minus = 0;
        int length = String.valueOf(guessedNumber).length();
        String p = String.valueOf(possibleNumber);
        String g = String.valueOf(guessedNumber);
        for (int i = 0; i < length; i++) {
            if (p.charAt(i) == g.charAt(i)) {
                plus++;
            } else if (g.indexOf(p.charAt(i)) >= 0) {
                minus++;
            }
        }

        return new String[]{String.valueOf(plus), String.valueOf(minus)};
    }

    //compCalc icin bir header fonksiyon. Skorları uygun şekilde geri döndürüyoruz.
    public String calculateUserScore(String userNumber) {
        String[] scores = compCalc(Integer.valueOf(userNumber), computerChosedIdontknow);
        return scores[0] + "," + scores[1];
    }


    /* Tüm olası değerler hesaplanır */
    public List<Integer> findAllProbabilities() {
        allPossibleNumbers = new ArrayList<>();
        allNumbers = new ArrayList<>();
        // Start traversing the numbers
        for (int i = 1023; i < 9876; i++) {
            int num = i;
            boolean visited[] = new boolean[10];

            // Find digits and maintain its hash
            while (num != 0) {
                // if a digit occcurs more than 1 time
                // then break
                if (visited[num % 10])
                    break;

                visited[num % 10] = true;

                num = num / 10;
            }

            // num will be 0 only when above loop
            // doesn't get break that means the
            // number is unique so print it.
            if (num == 0) {
                allPossibleNumbers.add(i);
            }
        }
        allNumbers = allPossibleNumbers;
        return allPossibleNumbers;
    }

    private String[] parseCompScore(String compScore) {
        return compScore.split(",");
    }
}

