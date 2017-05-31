// Generated code from Butter Knife. Do not modify!
package com.rdproj.vli.rdproject.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.rdproj.vli.rdproject.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyoStateFragment_ViewBinding implements Unbinder {
  private MyoStateFragment target;

  private View view2131427426;

  private View view2131427427;

  @UiThread
  public MyoStateFragment_ViewBinding(final MyoStateFragment target, View source) {
    this.target = target;

    View view;
    target.txtRightMyoName = Utils.findRequiredViewAsType(source, R.id.txt_right_myo_name, "field 'txtRightMyoName'", TextView.class);
    view = Utils.findRequiredView(source, R.id.start_connection_proc, "field 'startConnectionProc' and method 'startSearchingProcedure'");
    target.startConnectionProc = Utils.castView(view, R.id.start_connection_proc, "field 'startConnectionProc'", ImageView.class);
    view2131427426 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.startSearchingProcedure();
      }
    });
    view = Utils.findRequiredView(source, R.id.start_disconnect_proc, "field 'startDisConnectionProc' and method 'freeHub'");
    target.startDisConnectionProc = Utils.castView(view, R.id.start_disconnect_proc, "field 'startDisConnectionProc'", ImageView.class);
    view2131427427 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.freeHub();
      }
    });
    target.txtDevices = Utils.findRequiredViewAsType(source, R.id.txt_devices, "field 'txtDevices'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyoStateFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.txtRightMyoName = null;
    target.startConnectionProc = null;
    target.startDisConnectionProc = null;
    target.txtDevices = null;

    view2131427426.setOnClickListener(null);
    view2131427426 = null;
    view2131427427.setOnClickListener(null);
    view2131427427 = null;
  }
}
