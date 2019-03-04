package app.jimmy.locuslist.Models;

import androidx.annotation.NonNull;

/**
 * @author Jimmy
 * Created on 4/3/19.
 */
public class DataModel {
    private String type;
    private String id;
    private String title;
    private DataMapModel dataMap;
    private String selectedValue = "";
    private boolean toggled = false;

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public DataMapModel getDataMap() {
        return dataMap;
    }

    @NonNull
    @Override
    public String toString() {
        return "type "+type+" id "+id+" title "+title+ " datamap "+dataMap.toString()+" selected value "+selectedValue;
    }
}
