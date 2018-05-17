package com.hydbest.skinboot.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hydbest.skinboot.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csz on 2018/5/2.
 */

public class TextAdapter extends BaseQuickAdapter<String> {
    private static final ArrayList<String> list;

    static {
        list = new ArrayList<>();
        for (int i=0;i<30;i++){
            list.add(i+"");
        }
    }
    public TextAdapter(List<String> data) {
        super(R.layout.item_text,list);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tv,"只是偶尔会想你");
    }
}
