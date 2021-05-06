package com.example.mineseeker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private Board background;
    int x, y;
    private Cell[][] cells;
    private boolean active = true;
    private Button btnRestart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            restartGame();
            *//*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*//*
        });*/

        btnRestart = findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(v -> restartGame());

        LinearLayout layout = findViewById(R.id.board);
        background = new Board(this);
        background.setOnTouchListener(this);
        layout.addView(background);

        restartGame();

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int posX = (int) event.getX();
        int posY = (int) event.getY();
        if(active){
            for(int i=0; i<8; i++){
                for(int j=0; j<8; j++){
                    if(cells[i][j].pixelIn(posX,posY)){
                        cells[i][j].visible=true;
                        if(cells[i][j].content==100){
                            Toast.makeText(this, "Boooom!", Toast.LENGTH_LONG).show();
                            active=false;
                        }else if(cells[i][j].content==0){
                            track(i,j);
                        }
                        background.invalidate();
                    }
                }
            }

            if(active && iWon()){
                Toast.makeText(this, "Congratulations! You've won", Toast.LENGTH_SHORT).show();
                active = false;
            }
        }
        return false;
    }

    public void restartGame() {
        cells = new Cell[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j] = new Cell();
            }
        }
        setMines();
        countOuterMines();
        active = true;
        background.invalidate();
    }

    private void setMines() {
        int mines = 8;
        while (mines > 0) {
            int row = (int) (Math.random() * 8);
            int column = (int) (Math.random() * 8);
            if (cells[row][column].content == 0) {
                cells[row][column].content = 100;
                mines--;
            }
        }
    }

    private boolean iWon() {
        int amount = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cells[i][j].visible) {
                    amount++;
                }
            }
        }
        if (amount == 56) {
            return true;
        } else {
            return false;
        }
    }

    private void countOuterMines() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cells[i][j].content == 0) {
                    int nearMines = countCoord(i, j);
                    cells[i][j].content = nearMines;
                }
            }
        }
    }

    private int countCoord(int row, int col) {
        int mines = 0;

        if (row - 1 >= 0 && col - 1 >= 0) {
            if (cells[row - 1][col - 1].content == 100) {
                mines++;
            }
        }

        if (row - 1 >= 0) {
            if (cells[row - 1][col].content == 100) {
                mines++;
            }
        }

        if (row - 1 >= 0 && col + 1 < 8) {
            if (cells[row - 1][col + 1].content == 100) {
                mines++;
            }
        }

        if (col + 1 < 8) {
            if (cells[row][col + 1].content == 100) {
                mines++;
            }
        }

        if (row + 1 < 8 && col + 1 < 8) {
            if (cells[row + 1][col + 1].content == 100) {
                mines++;
            }
        }

        if (row + 1 < 8) {
            if (cells[row + 1][col].content == 100) {
                mines++;
            }
        }

        if (row + 1 < 8 && col - 1 >= 0) {
            if (cells[row + 1][col - 1].content == 100) {
                mines++;
            }
        }

        if (col - 1 >= 0) {
            if (cells[row][col - 1].content == 100) {
                mines++;
            }
        }

        return mines;
    }

    private void track(int row, int col) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            if (cells[row][col].content == 0) {

                cells[row][col].visible = true;
                cells[row][col].content = 50;

                track(row - 1, col - 1);
                track(row - 1, col);
                track(row - 1, col + 1);
                track(row, col + 1);
                track(row + 1, col + 1);
                track(row + 1, col);
                track(row + 1, col - 1);
                track(row, col - 1);

            }
        } else if (cells[row][col].content <= 8) {
            cells[row][col].visible = true;
        }
    }

    public class Board extends View {

        public Board(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas) {
            canvas.drawRGB(0, 0, 0);
            int width = 0;
            if (canvas.getWidth() < canvas.getHeight()) {
                width = background.getWidth();
            } else {
                width = background.getHeight();
            }

            int cellWidth = width / 8;

            Paint paint = new Paint();
            paint.setTextSize(40);

            Paint paint2 = new Paint();
            paint2.setTextSize(40);
            paint2.setTypeface(Typeface.DEFAULT_BOLD);
            paint2.setARGB(255, 0, 0, 255);

            Paint paintLine1 = new Paint();
            paintLine1.setARGB(255, 255, 255, 255);

            int actualRow = 0;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    cells[i][j].setXY(j * cellWidth, actualRow, cellWidth);

                    if (cells[i][j].visible) {
                        paint.setARGB(255, 153, 153, 153);
                    } else {
                        paint.setARGB(153, 204, 204, 204);
                    }

                    canvas.drawRect(j * cellWidth, actualRow, j * cellWidth + cellWidth - 1,
                            actualRow + cellWidth - 2, paint);
                    canvas.drawLine(j * cellWidth, actualRow, j * cellWidth + cellWidth,
                            actualRow, paintLine1);
                    canvas.drawLine(j * cellWidth + cellWidth - 1, actualRow,
                            j * cellWidth + cellWidth - 1, actualRow + cellWidth, paintLine1);


                    //cell don't have a mine but some near cells do
                    if (cells[i][j].content >= 1 &&
                            cells[i][j].content < 8 &&
                            cells[i][j].visible) {
                        canvas.drawText(
                                String.valueOf(cells[i][j].content),
                                j * cellWidth + (cellWidth / 2) - 8,
                                actualRow + cellWidth / 2,
                                paint2);
                    }

                    //cell with a mine
                    if (cells[i][j].content == 100 &&
                            cells[i][j].visible) {
                        Paint mine = new Paint();
                        mine.setARGB(255, 255, 0, 0);
                        canvas.drawCircle(
                                j * cellWidth + cellWidth / 2,
                                actualRow + cellWidth / 2,
                                20,
                                mine);
                    }
                }
                actualRow += cellWidth;
            }
        }
    }


}


