// Generated by view binder compiler. Do not edit!
package com.example.giaohang.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import com.example.giaohang.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class UserSingleLayoutBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final TextView useSingleName;

  @NonNull
  public final TextView userSingleData;

  @NonNull
  public final CircleImageView userSingleImage;

  private UserSingleLayoutBinding(@NonNull CardView rootView, @NonNull TextView useSingleName,
      @NonNull TextView userSingleData, @NonNull CircleImageView userSingleImage) {
    this.rootView = rootView;
    this.useSingleName = useSingleName;
    this.userSingleData = userSingleData;
    this.userSingleImage = userSingleImage;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static UserSingleLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static UserSingleLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.user_single_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static UserSingleLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.use_single_name;
      TextView useSingleName = rootView.findViewById(id);
      if (useSingleName == null) {
        break missingId;
      }

      id = R.id.user_single_data;
      TextView userSingleData = rootView.findViewById(id);
      if (userSingleData == null) {
        break missingId;
      }

      id = R.id.user_single_image;
      CircleImageView userSingleImage = rootView.findViewById(id);
      if (userSingleImage == null) {
        break missingId;
      }

      return new UserSingleLayoutBinding((CardView) rootView, useSingleName, userSingleData,
          userSingleImage);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}