package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import java.util.Random;


class sud_cell {
    private boolean change;
    private boolean error;
    private int n;
    sud_cell(int _n) {
        change = false;
        error = false;
        n = _n;
    }
    sud_cell() {
        change = true;
        error = false;
        n = 0;
    }
    public boolean isChange() {
        return change;
    }
    public boolean isError() {
        return error;
    }
    public int getN() {
        return n;
    }
    public void setN(int _n) {
        n = _n;
    }
    public void setError(boolean e) {
        error = e;
    }
}

class Sudoku {
    static int[] array = new int[81];
    public static sud_cell[] create() {
        generate_sudoky();
        sud_cell[] sud_cells = new sud_cell[81];
        for(int i=0;i<81;++i) {
            if(array[i]!=0) sud_cells[i] = new sud_cell(array[i]);
            else sud_cells[i] = new sud_cell();
        }
        return sud_cells;
    }



    private static void fill_array() {
        for(int i=0;i<9;++i) {
            for(int j=0;j<9;++j) {
                array[i*9+j] = ((i*3+i/3+j)%(9)+1);
            }

        }
    }


    private static void  transposing() {
        int[] tr = new int[81];
        for(int y=0;y<9;++y) {
            for(int x=0;x<9;++x) {
                tr[y*9+x]  = array[x*9+y];
            }
        }
        array = tr;
    }

    private static void swap_rows_small() {
        int area = new Random().nextInt(3);
        int line1 = new Random().nextInt(3);
        int N1 = area*3+line1;

        int line2 = new Random().nextInt(3);
        while (line1==line2) {
            line2 = new Random().nextInt(3);
        }

        int N2 = area*3+line2;

        for(int i=0;i<9;++i) {
            int n = array[N1*9+i];
            array[N1*9+i] = array[N2*9+i];
            array[N2*9+i] = n;
        }
    }


    private static void swap_colums_small() {
        transposing();
        swap_rows_small();
        transposing();
    }


    private static void swap_rows_area() {
        int area1 = new Random().nextInt(3);
        int area2 = new Random().nextInt(3);
        while (area1==area2) {
            area2 = new Random().nextInt(3);
        }

        for(int line=0;line<3;++line) {
            for(int i=0;i<9;++i) {
                int n = array[(area1*3+line)*9+i];
                array[(area1*3+line)*9+i] = array[(area2*3+line)*9+i];
                array[(area2*3+line)*9+i] = n;
            }
        }
    }

    private static void swap_colums_area() {
        transposing();
        swap_rows_area();
        transposing();
    }


    private static void generate_sudoky() {
        int N = 10;
        fill_array();

        for(int i=0;i<N;++i) {
            int r = new Random().nextInt(1);
            switch (r) {
                case 0:
                    transposing();
                    break;
                case 1:
                    swap_rows_small();
                    break;
                case 2:
                    swap_colums_small();
                    break;
                case 3:
                    swap_rows_area();
                    break;
                case 4:
                    swap_colums_area();
                    break;
            }
        }


        int count = 1;//new Random().nextInt(11) + 45;
        while (count>=0) {
            --count;
            int n = new Random().nextInt(81);
            array[n] = 0;
        }
    }
}




public class table extends AppCompatActivity {

    TextView choosen_text;
    sud_cell[] array;


    private boolean is_wrong(int n) {
        int x = n%9;
        int y = (n-x)/9;
        int N = array[n].getN();
        if(N==0) {
            return true;
        }
        for(int i=0;i<9;++i) {
            if((i!=x) && (array[y*9+i].getN()==N)) {
                return false;
            }
            if((i!=y)&&(array[i*9+x].getN()==N)) {
                return false;
            }
        }
        return true;
    }


    private boolean is_end() {
        for(int i=0;i<81;++i) {
            if(array[i].isError()||array[i].getN()==0) {
                return false;
            }
        }
        return true;
    }


    private TableLayout create_table() {
        TableLayout table = new TableLayout(this);
        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(true);
        table.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int id = 0;
        for(int i =0;i<3;++i) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            for(int j =0;j<3;++j) {
                TableLayout tl = new TableLayout(this);
                tl.setStretchAllColumns(true);
                tl.setShrinkAllColumns(true);
                Drawable td = getResources().getDrawable(R.drawable.tb_border);
                tl.setBackground(td);
                for(int y=0;y<3;++y) {
                    TableRow tr = new TableRow(this);
                    TableLayout.LayoutParams tr_layout = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    if(y==0) {
                        tr_layout.setMargins(3,3,3,0);
                    }
                    else if(y==1) {
                        tr_layout.setMargins(3,0,3,0);
                    }
                    else {
                        tr_layout.setMargins(3,0,3,3);
                    }


                    tr.setLayoutParams(tr_layout);

                    for(int x=0;x<3;++x) {
                        TextView txt = new TextView(this);
                        txt.setTextSize(30);
                        if(array[id].getN()!=0) {
                            txt.setText(Integer.toString(array[id].getN()));
                        }
                        else {
                            txt.setText("  ");
                        }
                        Drawable drawable = getResources().getDrawable(R.drawable.tb_cell);
                        txt.setBackground(drawable);
                        txt.setGravity(Gravity.CENTER);
                        txt.setId(id);
                        ++id;
                        txt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView tv = (TextView) v;
                                if(choosen_text==null&&!array[v.getId()].isChange()) {
                                    return;
                                }
                                if(choosen_text!=null&&!array[choosen_text.getId()].isError()) {
                                    Drawable drawable2 = getResources().getDrawable(R.drawable.tb_cell);
                                    choosen_text.setBackground(drawable2);

                                }
                                if(choosen_text!=null&&!array[v.getId()].isChange()) {
                                    if(array[choosen_text.getId()].isError()) {
                                        Drawable drawable2 = getResources().getDrawable(R.drawable.er_cell);
                                        choosen_text.setBackground(drawable2);
                                    }
                                    choosen_text = null;
                                    return;
                                }

                                if(choosen_text!=null&&array[choosen_text.getId()].isError()) {
                                    Drawable drawable2 = getResources().getDrawable(R.drawable.er_cell);
                                    choosen_text.setBackground(drawable2);

                                }
                                Drawable drawable1 = getResources().getDrawable(R.drawable.ch_cell);
                                tv.setBackground(drawable1);
                                choosen_text = tv;
                            }
                        });
                        tr.addView(txt,x);

                    }
                    tl.addView(tr,y);
                }
                tableRow.addView(tl,j);
            }
            table.addView(tableRow,i);
        }
        return table;
    }

    private TableLayout create_numbers() {
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setStretchAllColumns(true);
        tableLayout.setShrinkAllColumns(true);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        tableLayout.setLayoutParams(layoutParams);


        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        for(int i = 1; i < 10; ++i) {
            Button button = new Button(this);
            button.setText(Integer.toString(i));
            button.setTextSize(30);
            button.setTextColor(Color.BLACK);
            button.setId(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(choosen_text != null) {
                        array[choosen_text.getId()].setN(v.getId());
                        choosen_text.setText(Integer.toString(v.getId()));
                        Drawable drawable;
                        if(!is_wrong(choosen_text.getId())) {
                            array[choosen_text.getId()].setError(true);
                            drawable = getResources().getDrawable(R.drawable.er_cell);
                        }
                        else {
                            array[choosen_text.getId()].setError(false);
                            drawable = getResources().getDrawable(R.drawable.tb_cell);
                        }
                        choosen_text.setBackground(drawable);
                        choosen_text = null;
                        if(is_end()) {
                            final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            setContentView(R.layout.again);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(intent);
                                }
                            }, 3000);
                        }
                    }
                }
            });
            tableRow.addView(button,i-1);
        }
        tableLayout.addView(tableRow,0);
        return tableLayout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        choosen_text = null;
        array = Sudoku.create();

        linearLayout.addView(create_table(),0);
        linearLayout.addView(create_numbers(),1);
        setContentView(linearLayout);
    }
}
