package com.example.myapplication.util;

import android.support.v7.widget.RecyclerView;

/**
 * Interface permettant de dire à une activité quel a une action Swap.
 */
public interface RecyclerItemTouchHelperListner {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
