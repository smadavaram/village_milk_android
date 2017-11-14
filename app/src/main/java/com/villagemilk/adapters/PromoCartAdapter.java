package com.villagemilk.adapters;

        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.text.SpannableString;
        import android.text.Spanned;
        import android.text.style.StrikethroughSpan;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import com.villagemilk.BaseApplication;
        import com.villagemilk.R;
        import com.villagemilk.beans.ProductMaster;
        import com.villagemilk.util.Constants;
        import com.nostra13.universalimageloader.core.ImageLoader;

        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by akash.mercer on 28-Jul-16.
 */
public class PromoCartAdapter extends RecyclerView.Adapter<PromoCartAdapter.ViewHolder> {

    private Context context;
    private List<ProductMaster> productMasterList = new ArrayList<>();
    private boolean applyPromo;

    ImageLoader imageLoader = ImageLoader.getInstance();

    private PromoCartAdapter mAdapter;

    public PromoCartAdapter(Context context, boolean applyPromo) {

        this.context = context;
        this.productMasterList = new ArrayList<>();
        updateItems();
        this.applyPromo = applyPromo;

        mAdapter = this;
    }


    public void updateItems(){

        productMasterList.clear();
        List<ProductMaster> productMasters = BaseApplication.getInstance().getCart().getCartProductList();

        for(int i=0;i<productMasters.size();i++){
            if(productMasters.get(i).getSubscriptionId()==null)
                productMasterList.add(productMasters.get(i));
        }

    }

    public void addItem(int position, ProductMaster productMaster){

        productMasterList.add(position,productMaster);


    }

    public void removeItem(int position){

        productMasterList.remove(position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rootView;
        private ImageView ivProduct;
        private TextView tvProductName;
        private TextView tvProductUnitSize;
        private TextView tvProductUnitPrice;
        private TextView tvStrikePrice;
        private View viewSeparator;
        private TextView tvSpecialText;
        private LinearLayout llQuantityModification;
        private ImageView ivSubtract;
        private TextView tvProductQuantity;
        private LinearLayout llAdd;
        private ImageView ivAdd;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = (RelativeLayout) itemView.findViewById(R.id.rootView);
            ivProduct = (ImageView) itemView.findViewById(R.id.ivProduct);
            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvProductUnitSize = (TextView) itemView.findViewById(R.id.tvProductUnitSize);
            tvProductUnitPrice = (TextView) itemView.findViewById(R.id.tvProductUnitPrice);
            tvStrikePrice = (TextView) itemView.findViewById(R.id.tvStrikePrice);
            viewSeparator = itemView.findViewById(R.id.viewSeparator);
            tvSpecialText = (TextView) itemView.findViewById(R.id.tvSpecialText);
            llQuantityModification = (LinearLayout) itemView.findViewById(R.id.llQuantityModification);
            llAdd = (LinearLayout) itemView.findViewById(R.id.llAdd);
            ivSubtract = (ImageView) itemView.findViewById(R.id.ivSubtract);
            tvProductQuantity = (TextView) itemView.findViewById(R.id.tvProductQuantity);
            ivAdd = (ImageView) itemView.findViewById(R.id.ivAdd);

            llAdd.setVisibility(View.GONE);
            ivAdd.setVisibility(View.GONE);
            ivSubtract.setVisibility(View.GONE);

        }
    }

    @Override
    public PromoCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_cart_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PromoCartAdapter.ViewHolder holder, int position) {
        ProductMaster productMaster = productMasterList.get(position);

        if(productMaster.getProductImage()!=null) {
            imageLoader.displayImage(productMaster.getProductImage(),holder.ivProduct);
//            Picasso.with(context).load(productMaster.getProductImage()).into(holder.ivProduct);
        }else{
            holder.ivProduct.setImageResource(R.drawable.product_default);
        }

        holder.tvProductName.setText(productMaster.getProductName());

        holder.tvProductUnitSize.setText(productMaster.getProductUnitSize());

        StringBuilder priceString = new StringBuilder().append("\u20B9").append(Constants.priceDisplay.format(productMaster.getProductUnitPrice()));
        holder.tvProductUnitPrice.setText(priceString);

        if (productMaster.getStrikePrice() != null) {
            StringBuilder strikePriceString = new StringBuilder().append("\u20B9").append(Constants.priceDisplay.format(productMaster.getStrikePrice()));

            SpannableString spannableString = new SpannableString(strikePriceString);
            spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvStrikePrice.setText(spannableString);

            holder.tvStrikePrice.setVisibility(View.VISIBLE);
        } else {
            holder.tvStrikePrice.setVisibility(View.GONE);
        }

        if(productMaster.getSpecialText() != null && productMaster.getSpecialText().length() > 0){
            holder.tvSpecialText.setText(productMaster.getSpecialText());

            holder.tvSpecialText.setVisibility(View.VISIBLE);
            holder.viewSeparator.setVisibility(View.VISIBLE);
        } else {
            holder.tvSpecialText.setVisibility(View.GONE);
            holder.viewSeparator.setVisibility(View.GONE);
        }

        holder.tvProductQuantity.setText(String.valueOf(productMaster.getProductQuantity()));

        if(applyPromo && (productMaster.getProductType() != null && productMaster.getProductType().getType() == 53)){

//            holder.rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.promo_color));
//            holder.llAdd.setVisibility(View.GONE);
            holder.llQuantityModification.setVisibility(View.VISIBLE);
//            holder.ivAdd.setVisibility(View.GONE);
//            holder.ivSubtract.setVisibility(View.GONE);
        } else {
            if(productMaster.getProductQuantity() == 0){
                holder.llQuantityModification.setVisibility(View.GONE);
//                holder.llAdd.setVisibility(View.VISIBLE);
            } else {
//                holder.llQuantityModification.setVisibility(View.VISIBLE);
//                holder.llAdd.setVisibility(View.GONE);
//                holder.ivAdd.setVisibility(View.VISIBLE);
//                holder.ivSubtract.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return productMasterList.size();
    }
}

