package ua.nure.romanik.notes.presentation.ui.main;

import ua.nure.romanikvladislav.common.notes.domain.repository.INoteRepository;
import ua.nure.romanikvladislav.common.notes.presentation.ui.main.MainViewModelAbs;

public class MainViewModel extends MainViewModelAbs {
    public MainViewModel(INoteRepository noteRepository) {
        super(noteRepository);
    }
}
