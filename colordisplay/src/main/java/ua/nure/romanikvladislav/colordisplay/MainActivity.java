package ua.nure.romanikvladislav.colordisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private View ivColor;
    private SeekBar sbRed;
    private SeekBar sbGreen;
    private SeekBar sbBlue;

    private Rgb rgb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rgb = new Rgb();

        ivColor = findViewById(R.id.ivColor);
        sbRed = findViewById(R.id.sbRed);
        sbGreen = findViewById(R.id.sbGreen);
        sbBlue = findViewById(R.id.sbBlue);

        sbRed.setOnSeekBarChangeListener(this);
        sbGreen.setOnSeekBarChangeListener(this);
        sbBlue.setOnSeekBarChangeListener(this);
        setColorToImageView();
    }

    private void setColorToImageView() {
        int color = Color.rgb(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
        ivColor.setBackgroundColor(color);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int id = seekBar.getId();
        switch (id) {
            case R.id.sbRed:
                rgb.setRed(i);
                break;
            case R.id.sbGreen:
                rgb.setGreen(i);
                break;
            case R.id.sbBlue:
                rgb.setBlue(i);
                break;
        }
        setColorToImageView();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }
}
