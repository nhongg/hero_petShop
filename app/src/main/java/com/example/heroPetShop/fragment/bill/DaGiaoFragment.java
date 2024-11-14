package com.example.heroPetShop.fragment.bill;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.heroPetShop.Adapter.HoaDonAdapter;
import com.example.heroPetShop.Models.HoaDon;
import com.example.heroPetShop.Presenter.HoaDonPreSenter;
import com.example.heroPetShop.R;
import com.example.heroPetShop.View.CTHDActivity;
import com.example.heroPetShop.my_interface.HoaDonView;
import com.example.heroPetShop.my_interface.IClickCTHD;

import java.util.ArrayList;


public class DaGiaoFragment extends Fragment implements HoaDonView {

    private TextView tvNullDaGiao;
    private View view;
    private RecyclerView rcvBill;
    private HoaDonPreSenter hoaDonPreSenter;
    private HoaDonAdapter hoaDonAdapter;
    private ArrayList<HoaDon> listHoadon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_da_giao, container, false);

        tvNullDaGiao = view.findViewById(R.id.tv_null_dagiao);
        rcvBill = view.findViewById(R.id.rcv_bill_dagiao);
        hoaDonPreSenter = new HoaDonPreSenter(this);
        listHoadon = new ArrayList<>();
        hoaDonPreSenter.HandleReadDataHDStatus(3);

        return view;
    }

    @Override
    public void getDataHD(String id, String uid, String ghichu, String diachi, String hoten, String ngaydat, String phuongthuc, String sdt, String tongtien, Long type) {
        listHoadon.add(new HoaDon(id,uid,ghichu,diachi,hoten,ngaydat,phuongthuc,sdt,tongtien,type));
        if (listHoadon.size() != 0){
            tvNullDaGiao.setVisibility(View.GONE);
        } else tvNullDaGiao.setVisibility(View.VISIBLE);
        hoaDonAdapter = new HoaDonAdapter();
        hoaDonAdapter.setDataHoadon(listHoadon, new IClickCTHD() {
            @Override
            public void onClickCTHD(int pos) {
                HoaDon hoaDon = listHoadon.get(pos);
                Intent intent = new Intent(getContext(), CTHDActivity.class);
                intent.putExtra("HD",hoaDon);
                intent.putExtra("CM", true);
                startActivity(intent);
            }
        });
        rcvBill.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvBill.setAdapter(hoaDonAdapter);
    }

    @Override
    public void OnFail() {

    }

    @Override
    public void OnSucess() {

    }
}