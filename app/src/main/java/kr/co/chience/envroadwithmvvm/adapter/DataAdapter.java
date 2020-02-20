package kr.co.chience.envroadwithmvvm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.co.chience.envroadwithmvvm.R;
import kr.co.chience.envroadwithmvvm.databinding.ItemDataBinding;
import kr.co.chience.envroadwithmvvm.model.Data;
import kr.co.chience.envroadwithmvvm.viewmodel.MainViewModel;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {

    private List<Data> datas;
    private MainViewModel viewModel;

    public DataAdapter(List<Data> datas, MainViewModel viewModel) {
        this.datas = datas;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemDataBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_data, parent, false);

        return new DataViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        Data data = datas.get(position);
        holder.setBinding(data, viewModel);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {

        ItemDataBinding binding;

        public DataViewHolder(@NonNull ItemDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setBinding(Data data, MainViewModel viewModel) {
            binding.setData(data);
            binding.setDataViewModel(viewModel);
            binding.executePendingBindings();
        }

    }



}
