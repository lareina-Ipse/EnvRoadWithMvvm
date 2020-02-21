package kr.co.chience.envroadwithmvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import kr.co.chience.envroadwithmvvm.adapter.DataAdapter;
import kr.co.chience.envroadwithmvvm.databinding.ActivityMainBinding;
import kr.co.chience.envroadwithmvvm.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getData().observe(this, data -> binding.recyclerView.setAdapter(new DataAdapter(data, viewModel)));

//        viewModel.setData();
        binding.setDataViewModel(viewModel);


        binding.buttonStart.setOnClickListener(v -> {
            viewModel.stratScan();

        });

        binding.buttonEnd.setOnClickListener(v -> viewModel.stopScan());


    }

}
