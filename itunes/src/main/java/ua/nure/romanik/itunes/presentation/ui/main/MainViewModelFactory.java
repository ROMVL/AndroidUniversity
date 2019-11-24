package ua.nure.romanik.itunes.presentation.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Application application;

    MainViewModelFactory(Application application) {
        super();
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == MainViewModel.class) {
            return (T) new MainViewModel(application);
        }
        return null;
    }
}
