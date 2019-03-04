package app.jimmy.locuslist.Presenters;

/**
 * @author Jimmy
 * Created on 4/3/19.
 */
public class FullScreenActivityPresenter {
    private View view;

    public FullScreenActivityPresenter(View view) {
        this.view = view;
    }

    public void start(){

        view.loadImage(view.getUrl());
    }

    public interface View{

        void loadImage(String url);

        String getUrl();
    }
}
