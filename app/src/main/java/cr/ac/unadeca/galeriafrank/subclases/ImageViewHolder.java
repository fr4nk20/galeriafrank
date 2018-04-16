package cr.ac.unadeca.galeriafrank.subclases;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.flexbox.FlexboxLayoutManager;

import cr.ac.unadeca.galeriafrank.R;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;
    public ImageViewHolder(View itemView) {
        super(itemView);
        image =  itemView.findViewById(R.id.image);
    }
    public void bindTo() {
        ViewGroup.LayoutParams lp = image.getLayoutParams();
        if (lp instanceof FlexboxLayoutManager.LayoutParams) {
            FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams)lp;
            flexboxLp.setFlexGrow(1.0f);
        }
    }
}