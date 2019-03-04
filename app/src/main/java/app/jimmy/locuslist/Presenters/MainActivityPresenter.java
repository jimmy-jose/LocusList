package app.jimmy.locuslist.Presenters;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import app.jimmy.locuslist.Models.DataModel;
import app.jimmy.locuslist.R;

/**
 * @author Jimmy
 * Created on 4/3/19.
 */
public class MainActivityPresenter {
    private View view;

    public MainActivityPresenter(View view) {
        this.view = view;
    }

    public void submitClicked(){
        view.logData();
        view.showToast("Result logged in Verbose with Tag RESULT");
    }

    public void imageClicked(boolean openFullScreen,int position) {
        if(view.checkCameraPermission()){
            view.requestPermission();
        }else {
            if(!openFullScreen) {
                view.openCamera();
            }else {
                view.startFullScreenActivity(position);
            }
        }
    }

    public void photoClicked() {
        view.setClickedImage();
    }

    public void cameraPermissionGranted() {
        view.openCamera();
    }

    public void permissionDenied() {
        view.showToast("Permission Denied!");
    }

    public void start(Context context) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.data);
        String jsonString = new Scanner(inputStream).useDelimiter("\\A").next();
        Gson gson = new GsonBuilder().create();
        view.setDataList(Arrays.asList(gson.fromJson(jsonString, DataModel[].class)));
    }


    public interface View{

        void logData();

        boolean checkCameraPermission();

        void requestPermission();

        void startFullScreenActivity(int position);

        void openCamera();

        void setClickedImage();

        void showToast(String message);

        void setDataList(List<DataModel> asList);
    }
}
