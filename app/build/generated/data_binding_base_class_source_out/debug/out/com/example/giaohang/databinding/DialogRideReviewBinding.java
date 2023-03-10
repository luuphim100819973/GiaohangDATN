// Generated by view binder compiler. Do not edit!
package com.example.giaohang.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.example.giaohang.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogRideReviewBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final Button confirm;

  @NonNull
  public final ImageView image;

  @NonNull
  public final TextView name;

  @NonNull
  public final RatingBar rate;

  private DialogRideReviewBinding(@NonNull RelativeLayout rootView, @NonNull Button confirm,
      @NonNull ImageView image, @NonNull TextView name, @NonNull RatingBar rate) {
    this.rootView = rootView;
    this.confirm = confirm;
    this.image = image;
    this.name = name;
    this.rate = rate;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogRideReviewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogRideReviewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_ride_review, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogRideReviewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.confirm;
      Button confirm = rootView.findViewById(id);
      if (confirm == null) {
        break missingId;
      }

      id = R.id.image;
      ImageView image = rootView.findViewById(id);
      if (image == null) {
        break missingId;
      }

      id = R.id.name;
      TextView name = rootView.findViewById(id);
      if (name == null) {
        break missingId;
      }

      id = R.id.rate;
      RatingBar rate = rootView.findViewById(id);
      if (rate == null) {
        break missingId;
      }

      return new DialogRideReviewBinding((RelativeLayout) rootView, confirm, image, name, rate);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
