package com.example.cynthia.relax.presenters;

import android.os.Handler;
import com.example.cynthia.relax.services.RetrofitServiceManager;
import com.example.cynthia.relax.services.IUserService;
import com.example.cynthia.relax.activitis.login.LoginView;
import com.example.cynthia.relax.utils.BaseJson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter {
    private LoginView loginView;
    private Handler mHandler;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
        this.mHandler = new Handler();
    }

    public void login(String phone,String password) {
        loginView.showLoading();
        IUserService userService = RetrofitServiceManager.getInstance().create(IUserService.class);
        Call<Integer> call = userService.getUserInfo(phone, password);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response){
                try {
                    int userId = response.body();
                    switch (userId){
                        case -1:
                            loginView.showFailedMsg("该账户不存在");
                            break;
                        case 0:
                            loginView.showFailedMsg("账号或密码错误");
                            break;
                        default:
                            loginView.saveUserIdToSharedPreferences(userId);
                            loginView.showSuccessMsg(userId);
                            break;
                    }
                    loginView.hideLoading();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                loginView.showFailedMsg("登录失败");
                loginView.hideLoading();
            }
        });
    }
}
