package app.jimmy.locuslist.Models;

import java.util.Arrays;

import androidx.annotation.NonNull;

/**
 * @author Jimmy
 * Created on 4/3/19.
 */
public class DataMapModel {
    private String[] options;

    public String[] getOptions() {
        return options;
    }

    @NonNull
    @Override
    public String toString() {
        return Arrays.toString(options);
    }
}
