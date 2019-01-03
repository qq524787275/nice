package com.zhuzichu.uikit.message.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.zhuzichu.library.base.BaseFragment;
import com.zhuzichu.uikit.R;

import me.yokeyword.fragmentation.anim.DefaultNoAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by wb.zhuzichu18 on 2018/11/27.
 */
public class MessageEmotionFragment extends BaseFragment {

    public static MessageEmotionFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MessageEmotionFragment fragment = new MessageEmotionFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public Object setLayout() {
        return R.layout.fragment_message_emotion;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultNoAnimator();
    }
}
