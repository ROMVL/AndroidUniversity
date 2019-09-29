package un.nure.romanik.notes.presentation.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ua.nure.romanikvladislav.common.notes.domain.repository.INoteRepository;

public class ModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final INoteRepository repository;

    public ModelFactory(INoteRepository repository) {
        super();
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == MainViewModel.class) {
            return (T) new MainViewModel(repository);
        }
        return null;
    }
}
