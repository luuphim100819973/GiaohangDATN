package com.example.giaohang.History;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.giaohang.R;
import com.example.giaohang.SettingsActivity;
import com.example.giaohang.StatusActivity;

public class HistoryViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView rideId;
    public TextView time;
    public HistoryViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        rideId=(TextView) itemView.findViewById(R.id.rideId2);
        time=(TextView) itemView.findViewById(R.id.timestamp2);
    }
    public void onClick(View view) {
        String status = rideId.getText().toString();
        Intent intent= new Intent(view.getContext(),HistorySingleActivity2.class);
        intent.putExtra("rideId2",status);
        view.getContext().startActivity(intent);
      }
}
