package app.jimmy.locuslist.Views;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.jimmy.locuslist.DataListAdapter;
import app.jimmy.locuslist.Models.DataModel;
import app.jimmy.locuslist.Presenters.MainActivityPresenter;
import app.jimmy.locuslist.R;
import app.jimmy.locuslist.Utils.AppConstants;

public class MainActivity extends AppCompatActivity implements
        MainActivityPresenter.View, DataListAdapter.ItemClicks {

    private MainActivityPresenter presenter;
    private static final String TAG = MainActivity.class.getSimpleName();

    private int clickedPosition;
    private Uri photoUri;
    private RecyclerView.Adapter mAdapter;

    List<DataModel> list;

    private int MY_CAMERA_PERMISSION_CODE = 10001;
    private int CAMERA_REQUEST = 10002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = new MainActivityPresenter(this);
        presenter.start(getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new DataListAdapter(list, new WeakReference<DataListAdapter.ItemClicks>(this));
        mAdapter.setHasStableIds(true);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        MenuItem item = menu.findItem(R.id.action_submit);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                presenter.submitClicked();
                return true;
            }
        });
        return true;
    }


    @Override
    public void photoClicked(boolean openFullScreen,int position) {
        clickedPosition = position;
        presenter.imageClicked(openFullScreen,position);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.cameraPermissionGranted();
            } else {
                presenter.permissionDenied();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            presenter.photoClicked();
        }
    }

    @Override
    public void logData() {
        for(DataModel item: list)
            Log.v("RESULT", "Id :"+item.getId()+" Selected Value :"+item.getSelectedValue());
    }

    @Override
    public boolean checkCameraPermission() {
        return checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA},
                MY_CAMERA_PERMISSION_CODE);
    }

    @Override
    public void startFullScreenActivity(int clickedPosition) {
        Intent intent = new Intent(this, FullScreenActivity.class);
        intent.putExtra(AppConstants.PARAMS.TYPE_PHOTO, list.get(clickedPosition).getSelectedValue());
        startActivity(intent);
    }

    @Override
    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(getApplicationContext(),
                        "app.jimmy.locuslist.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    @Override
    public void setClickedImage() {
        list.get(clickedPosition).setSelectedValue(photoUri.toString());
        mAdapter.notifyDataSetChanged();
    }



    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setDataList(List<DataModel> asList) {
        list = asList;
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

}
