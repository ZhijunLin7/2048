package com.example.a2048;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import com.example.a2048.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements ViewSwitcher.ViewFactory {

    private ActivityMainBinding binding;
    private String usuario;

    private GestureDetectorCompat gestureDetector;
    private int[] imgIds;
    private  ImageSwitcher imageSwitcher;
    private int currentImagePosotion;
    private SeekBar indicadorImagen;
    private String[] gamedimen;
    private int currentdimenPosotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        usuario=intent.getStringExtra("Usuario");

        this.imageSlider();
        gamedimen=new String[]{"3x3","4x4","5x5"};
        currentdimenPosotion=0;

       this.configurarBotones();

    }

    // Congigura  el slide de imagen y sel seekbar que indica cuantos imagen hay.
    public void imageSlider(){
        indicadorImagen=binding.SliderIndicator;
        imageSwitcher = binding.imageSwitcher;

        gestureDetector=new GestureDetectorCompat(binding.imageSwitcher.getContext(),new GestureListener());

        imgIds=new int[] {R.drawable.a2048_4,R.drawable.lightout_4};
        currentImagePosotion = 0;

        indicadorImagen.setMax(imgIds.length-1);

        imageSwitcher.setFactory(this);
        imageSwitcher.setImageResource(imgIds[currentImagePosotion]);

        indicadorImagen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                currentImagePosotion=i;
                imageSwitcher.setImageResource(imgIds[currentImagePosotion]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public View makeView() {
        final ImageView i = new ImageView(this);
        i.setScaleType(ImageView.ScaleType.CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        i.setAdjustViewBounds(true);
        return i ;
    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffX = e2.getX() - e1.getX();

                    if (diffX > 0) {
                        if(currentImagePosotion > 0){
                            currentImagePosotion --;
                            imageSwitcher.setImageResource(imgIds[currentImagePosotion]);
                            indicadorImagen.setProgress(currentImagePosotion);
                        }
                    } else {
                        if(currentImagePosotion < imgIds.length-1){
                            currentImagePosotion ++;
                            imageSwitcher.setImageResource(imgIds[currentImagePosotion]);
                            indicadorImagen.setProgress(currentImagePosotion);
                        }
                    }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void configurarBotones(){
        binding.comenzar.setOnClickListener(view -> {
            Intent intent12;
            if (currentImagePosotion==0) {
                intent12 = new Intent(MainActivity.this, Juego2048.class);
            }else {
                intent12 = new Intent(MainActivity.this, Juegolightout.class);
            }
            intent12.putExtra("Dimension",currentdimenPosotion+3);
            intent12.putExtra("Usuario",usuario);
            startActivity(intent12);
        });

        binding.siguiente.setOnClickListener(view -> {
            currentdimenPosotion = (currentdimenPosotion + 1) % gamedimen.length;
            binding.verDimension.setText(gamedimen[currentdimenPosotion]);

        });
        binding.anterior.setOnClickListener(view -> {
            if (currentdimenPosotion == 0) {
                currentdimenPosotion = gamedimen.length - 1;
                binding.verDimension.setText(gamedimen[currentdimenPosotion]);
            } else {
                currentdimenPosotion = (currentdimenPosotion - 1) % gamedimen.length;
                binding.verDimension.setText(gamedimen[currentdimenPosotion]);
            }
        });

        binding.ranking.setOnClickListener(view -> {
            Intent intent1 = new Intent(MainActivity.this,GameRanking.class);
            startActivity(intent1);
        });
    }
}