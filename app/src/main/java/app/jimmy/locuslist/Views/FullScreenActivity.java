package app.jimmy.locuslist.Views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import app.jimmy.locuslist.Presenters.FullScreenActivityPresenter;
import app.jimmy.locuslist.R;
import app.jimmy.locuslist.Utils.AppConstants;

public class FullScreenActivity extends AppCompatActivity implements FullScreenActivityPresenter.View {
    private ImageView fullImage;
    private FullScreenActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        fullImage = findViewById(R.id.full_image);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        presenter = new FullScreenActivityPresenter(this);
        presenter.start();
    }



    @Override
    public void loadImage(String url) {
        fullImage.setImageURI(Uri.parse(url));
        fullImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public String getUrl() {
        Intent intent = getIntent();
        return intent.getStringExtra(AppConstants.PARAMS.TYPE_PHOTO);
    }
}
