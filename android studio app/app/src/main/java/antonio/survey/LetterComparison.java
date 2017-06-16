package antonio.survey;

import java.util.Arrays;

/**
 * Created by antoniolopez on 6/16/17.
 */

public class LetterComparison {


    public int getPercentThatMatch(int[] winningNumbers) {
        Arrays.sort(DataManager.template);
        Arrays.sort(winningNumbers);
        int i = 0, n = 0, match = 0;
        while (i < DataManager.template.length && n < winningNumbers.length) {
            if (DataManager.template[i] < winningNumbers[n]) {
                i++;
            } else if (DataManager.template[i] > winningNumbers[n]) {
                n++;
            } else {
                match++;
                i++;
                n++;
            }
        }
        return match * 100 / winningNumbers.length;
    }










}
