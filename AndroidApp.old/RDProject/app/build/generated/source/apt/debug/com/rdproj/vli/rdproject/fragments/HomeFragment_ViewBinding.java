// Generated code from Butter Knife. Do not modify!
package com.rdproj.vli.rdproject.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.rdproj.vli.rdproject.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HomeFragment_ViewBinding implements Unbinder {
  private HomeFragment target;

  private View view2131427421;

  private View view2131427418;

  @UiThread
  public HomeFragment_ViewBinding(final HomeFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.img_add_new_word, "field 'imgAddNewWord' and method 'addNewWord'");
    target.imgAddNewWord = Utils.castView(view, R.id.img_add_new_word, "field 'imgAddNewWord'", ImageView.class);
    view2131427421 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.addNewWord();
      }
    });
    view = Utils.findRequiredView(source, R.id.img_start_recording, "field 'btnStartRecording' and method 'startRecording'");
    target.btnStartRecording = Utils.castView(view, R.id.img_start_recording, "field 'btnStartRecording'", ImageView.class);
    view2131427418 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.startRecording();
      }
    });
    target.spinner = Utils.findRequiredViewAsType(source, R.id.known_words_list, "field 'spinner'", Spinner.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HomeFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imgAddNewWord = null;
    target.btnStartRecording = null;
    target.spinner = null;

    view2131427421.setOnClickListener(null);
    view2131427421 = null;
    view2131427418.setOnClickListener(null);
    view2131427418 = null;
  }
}
