package unlockscreen.nvt.com.unlockscreen;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class LockScreenView extends FrameLayout implements View.OnClickListener{
    private Word[] words;
    private Database database;
    private Random random;
    private TextView txtEn, txtVi1, txtVi2;

    private LayoutInflater inflater;
    private Handler handler;
    private Vibrator vibrator;

    public LockScreenView(Context context, Handler handler) {
        super(context);

        inflater = LayoutInflater.from(context);
        this.handler = handler;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        inflater.inflate(R.layout.view_lock_screen, this);
        txtEn = (TextView) findViewById(R.id.txt_en);
        txtVi1 = (TextView) findViewById(R.id.txt_vi_1);
        txtVi2 = (TextView) findViewById(R.id.txt_vi_2);
        txtVi1.setOnClickListener(this);
        txtVi2.setOnClickListener(this);
        database = new Database();
        database.copyDatabase(context);
        random= new Random();
        bindData();
    }

    public void bindData(){
        words = database.getRandomTwoWord();
        txtEn.setText(words[0].getEn());
        if(random.nextInt(100) < 50){
            txtVi1.setText(words[0].getVi());
            txtVi2.setText(words[1].getVi());
        }else{
            txtVi1.setText(words[1].getVi());
            txtVi2.setText(words[0].getVi());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_vi_1:
                if(txtVi1.getText().equals(words[0].getVi())){
                    //Mở khóa
                    openLockScreenView();
                }else{
                    //tao lai bộ từ mới
                    vibrator.vibrate(500);
                    Toast.makeText(v.getContext(), "Ngu", Toast.LENGTH_LONG).show();
                    bindData();
                }
                break;

            case R.id.txt_vi_2:
                if(txtVi2.getText().equals(words[0].getVi())){
                    openLockScreenView();
                }else{
                    vibrator.vibrate(500);
                    Toast.makeText(v.getContext(), "Ngu", Toast.LENGTH_LONG).show();
                    bindData();
                }
                break;
        }
    }
    //phuong thuc truyen sang service de  xu ly su kien tat man hinh
    private void openLockScreenView(){
        handler.sendEmptyMessage(1000);
    }
}